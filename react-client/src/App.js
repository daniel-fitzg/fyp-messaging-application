import React from 'react';
import './App.css';
import LoginForm from "./main-frame-components/welcome-page/LoginForm"
import RegisterForm from "./main-frame-components/welcome-page/RegisterForm"
import TitleHeader from "./main-frame-components/TitleHeader"
import Conversation from "./main-frame-components/user-conversations/Conversation"
import MessagesList from "./main-frame-components/MessagesList"
import SendMessage from "./main-frame-components/SendMessage"

class App extends React.Component {

  constructor() {
      super()
      this.state = {
        showWelcomeScreen: true,
        showConversationsScreen: false,
        showMessagesScreen: false,
        showLoginForm: false,
        showRegisterForm: false,
        showLoadingScreen: false,
        userId: "",
        conversationList: [],
        conversationId: "",
        messages: []
      }

      this.loadWelcomeScreen = this.loadWelcomeScreen.bind(this)
      this.loadConversationsScreen = this.loadConversationsScreen.bind(this)
      this.loadMessagesScreen = this.loadMessagesScreen.bind(this)
      this.loadLoginForm = this.loadLoginForm.bind(this)
      this.loadRegisterForm = this.loadRegisterForm.bind(this)
      this.updateLoadingScreen = this.updateLoadingScreen.bind(this)
      this.updateConversationId = this.updateConversationId.bind(this)
      this.authenticateUser = this.authenticateUser.bind(this)
      this.registerUser = this.registerUser.bind(this)
      this.getConversations = this.getConversations.bind(this)
      this.getConversationEntries = this.getConversationEntries.bind(this)
      this.addConversationEntry = this.addConversationEntry.bind(this)
  }

  loadWelcomeScreen(event) {
    this.setState({
      showWelcomeScreen: true,
      showConversationsScreen: false,
      showMessagesScreen: false
    })
  }

  loadConversationsScreen(userId) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: true,
      showMessagesScreen: false,
      userId: userId
    })

    this.getConversations()
  }

  loadMessagesScreen(conversationId, authorId, secondaryAuthorId) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: false,
      showMessagesScreen: true
    })

    this.getConversationEntries(conversationId, authorId, secondaryAuthorId)
  }

  loadLoginForm() {
    this.setState({
      showLoginForm: !this.state.showLoginForm
    })
  }

  loadRegisterForm() {
    this.setState({
      showRegisterForm: !this.state.showRegisterForm
    })
  }
  
  updateLoadingScreen() {
   this.setState({
     showLoadingScreen: !this.state.showLoadingScreen
   })  
  }

  updateConversationId(conversationId) {
    this.setState({
      conversationId: conversationId
    })

    this.getConversationEntries(this.state.conversationId)
  }

  authenticateUser(event, email, password) {
    alert("Request params: " + email + password)

    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true);
     request.onreadystatechange = () => {
         if (request.readyState === 4 && request.status === 200) {
           console.log(request.responseText)
           var user = JSON.parse(request.responseText)

           if (user.userId != null) {
             console.log("User login successful")
             this.updateLoadingScreen()
             this.loadConversationsScreen(user.userId)
           } else {
             console.log("User login failed")
             alert("Login failed")
           }
         }
     };

     request.send(JSON.stringify({
         email: email,
         password: password
       }));
  }

  registerUser(event, email, password, firstName, lastName) {
    alert("Request params" + email + ", " + password + ", " + firstName + ", " + lastName)

    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/RegisterUser', true);
     request.onreadystatechange = () => {
         if (request.readyState === 4 && request.status === 200) {
           var result = JSON.parse(request.responseText)

           if (result.serverResponse) {
             console.log("User registraton successful")
             this.loadConversationsScreen()
           } else {
             alert("User registration failed")
           }
         }
     };

     request.send(JSON.stringify({
       email: email,
       password: password,
       firstName: firstName,
       lastName: lastName
     }))
  }

  getConversations() {
    alert("Getting conversations for " + this.state.userId)

    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetUserConversations', true);
    request.onreadystatechange = () => {
        if (request.readyState === 4 && request.status === 200) {
          var conversationList = JSON.parse(request.responseText)
          this.setState({
            conversationList: conversationList
          })
          this.updateLoadingScreen()
          console.log(conversationList)
        }
     };

     request.send(JSON.stringify({
       userId: this.state.userId
     }))
  }

  getConversationEntries(conversationId, authorId, secondaryAuthorId) {
    alert("Get convos for: " + conversationId + ", " + authorId + ", " + secondaryAuthorId)

    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetConversationEntries', true);
    request.onreadystatechange = () => {
        if (request.readyState === 4 && request.status === 200) {
          var messages = JSON.parse(request.responseText)
          this.setState({
            messages: messages
          })
          console.log(messages)
        }
     };

     request.send(JSON.stringify({
       conversationId: conversationId,
       authorId: authorId,
       secondaryAuthorId: secondaryAuthorId
     }))
  }

  addConversationEntry(event, message) {
    alert("Message: " + message)

    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AddConversationEntry', true);
    request.onreadystatechange = () => {
        if (request.readyState === 4 && request.status === 200) {
          var updatedMessages = JSON.parse(request.responseText)
          this.setState({
            messages: updatedMessages
          })
          console.log(updatedMessages)
        }
     };

     request.send(JSON.stringify({
       content: message,
       createDate: new Date()
     }))
  }

  /* One way of keeping the send message bar on screen even when scrolling down woul be poisition: fixed ???*/

  getComponent() {
    if (this.state.showWelcomeScreen) {
      if (this.state.showLoginForm) {
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/welcome-page/speech-bubble.jpg"}`)} />
            <LoginForm authenticateUser={this.authenticateUser}/>
          </div>
        )
      } else if (this.state.showRegisterForm) {
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/welcome-page/speech-bubble.jpg"}`)} />
            <RegisterForm registerUser={this.registerUser}/>
          </div>
        )
      }
      return (
        <div>
          <img className="welcome-icon" src={require(`${"./main-frame-components/welcome-page/speech-bubble.jpg"}`)} />
          <button className="login-button" onClick={this.loadLoginForm}>LOGIN</button>
          <button className="register-button" onClick={this.loadRegisterForm}>REGISTER</button>
        </div>
      )
    } else if (this.state.showConversationsScreen) {
      var list = this.state.conversationList.map((conversation) =>
        <Conversation conversation={conversation} loadMessagesScreen={this.loadMessagesScreen}/>);
      
      if (this.state.showLoadingScreen) {
        return (
          <div style={{backgroundColor: "white"}}>
            <TitleHeader />
            <h1 className="loading-screen">LOADING</h1>
          </div>
        )
       } else {
        return (
          <div style={{backgroundColor: "white"}}>
            <TitleHeader />
            {list}
          </div>
        )
      }
    } else if (this.state.showMessagesScreen) {
      return (
        <div>
          <TitleHeader />
          <MessagesList userId={this.state.userId} messages={this.state.messages}/>
          <SendMessage addConversationEntry={this.addConversationEntry}/>
        </div>
      )
    }
  }


  render() {
    return (
      <div className="app">
        {this.getComponent()}
      </div>
    )
  }
}

export default App;
