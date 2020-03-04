import React from "react"
import SendMessage from "./SendMessage"

class MessagesList extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      userId: this.props.userId
    }
  }
  
  render() {
   return (
     <div className="message-list">
       {this.props.messages.map(message => {
         console.log(message.authorId + " : " + this.props.messages.userId) 
         if (message.authorId === "7db251f0-a3ef-4787-830a-9bc0b1dbd0de") {
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
