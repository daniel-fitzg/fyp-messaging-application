import React from "react"

/* 
Creates a MessagesList Component from data returned from server
A list of messages from server is parsed to print to User Interface
Messages are from both participants and in order from newest to oldest
*/

class MessagesList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: this.props.userId,
      secondaryAuthorId: this.props.secondaryUserId
    }
  }

  // Timer - getConversationEntries will be called every 2 seconds to update message list of the current conversation
  componentDidMount() {
    this.interval = setInterval(() => {
      this.props.getConversationEntries(this.state.userId, this.state.secondaryAuthorId)
    }, 2000);
  }

  // Timer is cleared when the message list screen is no longer active/being rendered
  componentWillUnmount() {
   clearInterval(this.interval)
 }
  
  // Name tag of each message and message colour is filtered the user ID of each individual message
  render() {
   return (
     <div className="message-list">
       {this.props.messages.map(message => {
         if (message.authorId === this.state.userId) {
           return (
             <div>
               <div>
                 <p className="author-name">{message.authorName}</p>
               </div>
               <div>
                 <p><span style={{backgroundColor: "#525252", boxShadow: "2px 8px 5px #000"}}>{message.content}</span></p>
               </div>
             </div>
           )
         } else {
           return (
             <div>
               <div>
                 <p className="author-name">{message.authorName}</p>
               </div>
               <div>
                 <p><span style={{backgroundColor: "#327da8", boxShadow: "2px 8px 5px #000"}}>{message.content}</span></p>
               </div>
             </div>
           )
         }
       })}
     </div>
   )
  }
}

export default MessagesList
