import React from 'react';
import './App.css';
import LoginForm from "./main-frame-components/welcome-page/LoginForm"
import RegisterForm from "./main-frame-components/welcome-page/RegisterForm"
import TitleMessageList from "./main-frame-components/TitleMessageList"
import TitleConversationsList from "./main-frame-components/TitleConversationsList"
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
        firstName: "",
        lastName: "",
        secondaryUserId: "",
        conversationList: [],
        conversationId: "",
        messages: []
      }

      this.loadWelcomeScreen = this.loadWelcomeScreen.bind(this)
      this.loadConversationsScreen = this.loadConversationsScreen.bind(this)
      this.loadMessagesScreen = this.loadMessagesScreen.bind(this)
      this.loadLoginForm = this.loadLoginForm.bind(this)
      this.loadRegisterForm = this.loadRegisterForm.bind(this)
      this.resetWelcomeScreen = this.resetWelcomeScreen.bind(this)
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

    this.resetWelcomeScreen()
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

  loadMessagesScreen(authorId, secondaryAuthorId) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: false,
      showMessagesScreen: true
    })

    this.getConversation(authorId, secondaryAuthorId)
    this.getConversationEntries(authorId, secondaryAuthorId)
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

  resetWelcomeScreen() {
    this.setState({
      showLoginForm: false,
      showRegisterForm: false
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
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true);
     request.onreadystatechange = () => {
         if (request.readyState === 4 && request.status === 200) {
           var user = JSON.parse(request.responseText)

           if (user.userId != null) {
             console.log("User login successful")
             this.setState({
               firstName: user.firstName,
               lastName: user.lastName,
               showLoadingScreen: true
             })
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
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/RegisterUser', true);
     request.onreadystatechange = () => {
         if (request.readyState === 4 && request.status === 200) {
           var result = JSON.parse(request.responseText)

           if (result.registeredUserId != false) {
             console.log("User registraton successful")
             this.setState({
               firstName: firstName,
               lastName: lastName,
               showLoadingScreen: true
             })
             this.loadConversationsScreen(result.registeredUserId)
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

  // Called when the conversation list screen is activated
  getConversations() {
    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetUserConversations', true);
    request.onreadystatechange = () => {
        if (request.readyState === 4 && request.status === 200) {
          var conversationList = JSON.parse(request.responseText)
          this.setState({
            conversationList: conversationList
          })
          //this.updateLoadingScreen()
          this.setState({
            showLoadingScreen: false
          })
          console.log("Marker " + conversationList)
        }
     };

     request.send(JSON.stringify({
       userId: this.state.userId
     }))
  }

  getConversation(authorId, secondaryAuthorId) {
    this.setState({
      secondaryUserId: secondaryAuthorId
    })

    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetConversation', true);
    request.onreadystatechange = () => {
        if (request.readyState === 4 && request.status === 200) {
          var conversation = JSON.parse(request.responseText)
          this.setState({
            conversationId: conversation.conversationId
          })
          // this.setState({
          //   showLoadingScreen: true
          // })
          console.log("Convo retrieved " + conversation.conversationId)
        }
     };

     request.send(JSON.stringify({
       authorId: authorId,
       secondaryAuthorId: secondaryAuthorId
     }))
  }

  getConversationEntries(authorId, secondaryAuthorId) {
    const request = new XMLHttpRequest()
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetConversationEntries', true);
    request.onreadystatechange = () => {
        if (request.readyState === 4 && request.status === 200) {
          var messages = JSON.parse(request.responseText)
          this.setState({
            messages: messages
          })
          console.log(messages)
          //this.updateLoadingScreen()
          this.setState({
            showLoadingScreen: false
          })
        }
     };

     request.send(JSON.stringify({
       authorId: authorId,
       secondaryAuthorId: secondaryAuthorId
     }))
  }

  addConversationEntry(event, message) {
    alert("Convo: " + this.state.conversationId + " Message: " + message)

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
       conversationId: this.state.conversationId,
       authorId: this.state.userId,
       content: message,
       createDate: new Date(),
       secondaryAuthorId: this.state.secondaryUserId
     }))
  }

  /* One way of keeping the send message bar on screen even when scrolling down woul be poisition: fixed ???*/

  getComponent() {
    if (this.state.showWelcomeScreen) {
      if (this.state.showLoginForm) {
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/welcome-page/NewLogo.jpg"}`)} />
            <LoginForm authenticateUser={this.authenticateUser}/>
            <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
          </div>
        )
      } else if (this.state.showRegisterForm) {
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/welcome-page/NewLogo.jpg"}`)} />
            <RegisterForm registerUser={this.registerUser}/>
            <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
          </div>
        )
      }
      return (
        <div>
          <img className="welcome-icon" src={require(`${"./main-frame-components/welcome-page/NewLogo.jpg"}`)} />
          <button className="login-button" onClick={this.loadLoginForm}>LOGIN</button>
          <button className="register-button" onClick={this.loadRegisterForm}>REGISTER</button>
          <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
        </div>
      )
    } else if (this.state.showConversationsScreen) {
      var list = this.state.conversationList.map((conversation) =>
        <Conversation
        conversation={conversation}
        currentUserId={this.state.userId}
        updateLoadingScreen={this.updateLoadingScreen}
        loadMessagesScreen={this.loadMessagesScreen}/>);

      if (this.state.showLoadingScreen) {
        return (
          <div style={{backgroundColor: "white"}}>
            <h1 className="loading-screen">LOADING</h1>
          </div>
        )
       } else {
        return (
          <div style={{backgroundColor: "white"}}>
            <TitleConversationsList
              loadWelcomeScreen={this.loadWelcomeScreen}
              firstName={this.state.firstName}
              lastName={this.state.lastName}
            />
            {list}
          </div>
        )
      }
    } else if (this.state.showMessagesScreen) {
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
              loadConversationsScreen={this.loadConversationsScreen}
              updateLoadingScreen={this.updateLoadingScreen}
              userId={this.state.userId}
              firstName={this.state.firstName}
              lastName={this.state.lastName}
            />
            <MessagesList
              userId={this.state.userId}
              secondaryUserId={this.state.secondaryUserId}
              messages={this.state.messages}
              getConversationEntries={this.getConversationEntries}
              updateLoadingScreen={this.updateLoadingScreen}
            />
            <SendMessage addConversationEntry={this.addConversationEntry} messages={this.state.messages}/>
          </div>
        )
      }
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
