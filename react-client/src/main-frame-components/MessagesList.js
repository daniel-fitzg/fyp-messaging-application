import React from "react"
import SendMessage from "./SendMessage"

class MessagesList extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      userId: this.props.userId,
      secondaryAuthorId: this.props.secondaryUserId
    }
    
    this.handleClick = this.handleClick.bind(this)
  }
  
  handleClick() {
    this.props.updateLoadingScreen()
    this.props.getConversationEntries(this.state.userId, this.state.secondaryAuthorId)
  }
  
  render() {
   return (
     <div className="message-list">
     <button className="refresh-button" onClick={this.handleClick}>REFRESH</button>
       {this.props.messages.map(message => {
         console.log(message.authorId + " : " + this.props.messages.userId) 
         if (message.authorId === this.state.userId) {
           return (
             <div>
               <div>
                 <p className="author-name">{message.authorName}</p>
               </div>
               <div>
                 <p><span style={{backgroundColor: "#3366ff", boxShadow: "2px 8px 5px #000"}}>{message.content}</span></p>
               </div>
             </div>
           )
         } else {
           return (
             <div className="wha">
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
