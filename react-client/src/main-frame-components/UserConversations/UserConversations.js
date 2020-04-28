import React from 'react'
import TitleConversationsList from "./TitleConversationsList"
import Conversation from "./Conversation"

class UserConversations extends React.Component {
  constructor() {
    super()
    this.state = {
      conversationList: [],
      conversationId: ""
    }
    
    this.logoutUser = this.logoutUser.bind(this)
    this.getConversation = this.getConversation.bind(this)
    this.getConversations = this.getConversations.bind(this)
  }
  
  componentDidMount() {
    this.getConversations()
  }
  
  logoutUser() {
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/LogoutUser', true);
     request.onreadystatechange = () => {
         // Server response has been received and ready to be processed
         if (request.readyState === 4 && request.status === 200) {
           console.log("User logged out")
         }
     };

     request.send(JSON.stringify({
       userId: this.props.userId
     }))
  }
  
  // HTTP request to server to get a list of possible users the current user can have a conversation with
  // Called when the conversation list screen is activated
  getConversations() {
    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetUserConversations', true);
    request.onreadystatechange = () => {
        // Server response has been received and ready to be processed
        if (request.readyState === 4 && request.status === 200) {
          var conversationList = JSON.parse(request.responseText)
          this.setState({
            conversationList: conversationList
          })
          this.setState({
            showLoadingScreen: false
          })
        }
     };

     request.send(JSON.stringify({
       userId: this.props.userId
     }))
  }

  // HTTP request to server to retreive the conversation of which both users are a participant
  getConversation(authorId, secondaryAuthorId) {
    this.setState({
      secondaryUserId: secondaryAuthorId
    })

    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetConversation', true);
    request.onreadystatechange = () => {
        // Server response has been received and ready to be processed
        if (request.readyState === 4 && request.status === 200) {
          var conversation = JSON.parse(request.responseText)
          this.setState({
            conversationId: conversation.conversationId
          })
          
          this.props.setConversationId(conversation.conversationId)
        }
     };

     request.send(JSON.stringify({
       authorId: authorId,
       secondaryAuthorId: secondaryAuthorId
     }))
  }
  
  getComponent() {
    var list = this.state.conversationList.map((conversation) =>
      <Conversation
        conversation={conversation}
        conversationId={this.state.conversationId}
        currentUserId={this.props.userId}
        getConversations={this.getConversations}
        updateLoadingScreen={this.props.updateLoadingScreen}
        loadMessagesScreen={this.props.loadMessagesScreen}
        getConversation={this.getConversation}
      />);

    if (this.props.showLoadingScreen) {
      return (
        <div style={{backgroundColor: "white"}}>
          <h1 className="loading-screen">LOADING</h1>
        </div>
      )
     } else {
       // Display list of available conversations
       return (
        <div style={{backgroundColor: "white"}}>
          <TitleConversationsList
            loadWelcomeScreen={this.props.loadWelcomeScreen}
            firstName={this.props.firstName}
            lastName={this.props.lastName}
            logoutUser={this.logoutUser}
          />
          {list}
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

export default UserConversations