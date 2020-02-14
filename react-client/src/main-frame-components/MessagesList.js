import React from "react"

class MessagesList extends React.Component {
render() {
 return (
   <div className="message-list">
     {this.props.messages.map(message => {
       return (
         <div>
           <div>
             <p className="sender-id">{message.senderId}</p>
           </div>
           <div>
             <p><span>{message.text}</span></p>
           </div>
         </div>
       )
     })}
   </div>
 )
}
}

export default MessagesList
