import React from 'react'
import TitleMessageList from "./TitleMessageList"
import MessagesList from "./MessagesList"
import SendMessage from "./SendMessage"

class UserMessages extends React.Component {
  
  constructor(props) {
    super(props)  
    this.state = {
      messages: []
    }
    
    this.getConversationEntries = this.getConversationEntries.bind(this)
    this.addConversationEntry = this.addConversationEntry.bind(this)
  }  
  
  componentDidMount() {
    this.getConversationEntries(this.props.authorId, this.props.secondaryAuthorId)
  }
  
  // HTTP request to server to get all message entries belonging to the conversation
  getConversationEntries(authorId, secondaryAuthorId) {
    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetConversationEntries', true);
    request.onreadystatechange = () => {
        // Server response has been received and ready to be processed
        if (request.readyState === 4 && request.status === 200) {
          var messages = JSON.parse(request.responseText)
          this.setState({
            messages: messages,
            showLoadingScreen: false
          })
        }
     };

     request.send(JSON.stringify({
       authorId: authorId,
       secondaryAuthorId: secondaryAuthorId
     }))
  }

  // HTTP request to server with user input text to add an entry to the current conversation
  addConversationEntry(event, message) {
    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AddConversationEntry', true);
    request.onreadystatechange = () => {
        // Server response has been received and ready to be processed
        if (request.readyState === 4 && request.status === 200) {
          var updatedMessages = JSON.parse(request.responseText)
          this.setState({
            messages: updatedMessages
          })
        }
     };

     request.send(JSON.stringify({
       conversationId: this.props.conversationId,
       authorId: this.props.userId,
       content: message,
       createDate: new Date(),
       secondaryAuthorId: this.props.secondaryAuthorId
     }))
  }
  
  getComponent() {
      if (this.state.showLoadingScreen) {
        return (
          <div style={{backgroundColor: "white"}}>
            <h1 className="loading-screen">LOADING</h1>
          </div>
        )
       } else {
        return (
          <div>
            <TitleMessageList
              loadConversationsScreen={this.props.loadConversationsScreen}
              updateLoadingScreen={this.props.updateLoadingScreen}
              userId={this.props.userId}
              firstName={this.props.firstName}
              lastName={this.props.lastName}
            />
            <MessagesList
              userId={this.props.userId}
              secondaryUserId={this.props.secondaryUserId}
              messages={this.state.messages}
              getConversationEntries={this.getConversationEntries}
              updateLoadingScreen={this.updateLoadingScreen}
            />
            <SendMessage 
              addConversationEntry={this.addConversationEntry} 
              messages={this.state.messages}
            />
          </div>
        )
      }
  }
  
  render() {
    return (
      <div>
        {this.getComponent()}  
      </div>
    )
  }
}

export default UserMessages