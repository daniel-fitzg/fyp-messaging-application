import React from 'react';
import './App.css';
import LoginForm from "./main-frame-components/LoginForm"
import RegisterForm from "./main-frame-components/RegisterForm"
import TitleMessageList from "./main-frame-components/TitleMessageList"
import TitleConversationsList from "./main-frame-components/TitleConversationsList"
import Conversation from "./main-frame-components/Conversation"
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

      // Bind statements that set the context of 'this' when passed to Components are properties (arguments)
      this.loadWelcomeScreen = this.loadWelcomeScreen.bind(this)
      this.loadConversationsScreen = this.loadConversationsScreen.bind(this)
      this.loadMessagesScreen = this.loadMessagesScreen.bind(this)
      this.loadLoginForm = this.loadLoginForm.bind(this)
      this.loadRegisterForm = this.loadRegisterForm.bind(this)
      this.resetWelcomeScreen = this.resetWelcomeScreen.bind(this)
      this.updateLoadingScreen = this.updateLoadingScreen.bind(this)
      this.authenticateUser = this.authenticateUser.bind(this)
      this.registerUser = this.registerUser.bind(this)
      this.getConversations = this.getConversations.bind(this)
      this.getConversationEntries = this.getConversationEntries.bind(this)
      this.addConversationEntry = this.addConversationEntry.bind(this)
  }

  /*
  The following load functions set falgs that control which screen/elements are rendered for the user
  */

  loadWelcomeScreen(event) {
    this.setState({
      showWelcomeScreen: true,
      showConversationsScreen: false,
      showMessagesScreen: false
    })

    // Does not show the login or registration button
    this.resetWelcomeScreen()
  }

  loadConversationsScreen(userId) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: true,
      showMessagesScreen: false,
      userId: userId
    })

    // Upon load, conversations are retreived from the server
    this.getConversations()
  }

  loadMessagesScreen(authorId, secondaryAuthorId) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: false,
      showMessagesScreen: true
    })

    // Gets the conversation ID needed to identify the messages/entries belonging to the conversation
    this.getConversation(authorId, secondaryAuthorId)
    // Gets the relevant entries
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

  // Inverts current boolean value of the showLoadingScreen flag
  updateLoadingScreen() {
   this.setState({
     showLoadingScreen: !this.state.showLoadingScreen
   })
  }

  // HTTP request to server with user input data to authenticate that user is in system and password if correct
  authenticateUser(event, email, password) {
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true);
     request.onreadystatechange = () => {
         // Server response has been received and ready to be processed
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

  // HTTP request to server with user input data to register a new user in the system
  registerUser(event, email, password, firstName, lastName) {
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/RegisterUser', true);
     request.onreadystatechange = () => {
         // Server response has been received and ready to be processed
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
       userId: this.state.userId
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
        }
     };

     request.send(JSON.stringify({
       authorId: authorId,
       secondaryAuthorId: secondaryAuthorId
     }))
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
       conversationId: this.state.conversationId,
       authorId: this.state.userId,
       content: message,
       createDate: new Date(),
       secondaryAuthorId: this.state.secondaryUserId
     }))
  }

  // Determines which component and elements are currently rendered to the User Interface
  getComponent() {
    
    if (this.state.showWelcomeScreen) {
      // Welcome screen is active
      if (this.state.showLoginForm) {
        // User has clicked login button
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/NewLogo.jpg"}`)} />
            <LoginForm authenticateUser={this.authenticateUser}/>
            <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
          </div>
        )
      } else if (this.state.showRegisterForm) {
        // User has clicked register button
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/NewLogo.jpg"}`)} />
            <RegisterForm registerUser={this.registerUser}/>
            <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
          </div>
        )
      } else {
        // Neither login nor register button has yet been pressed
        return (
          <div>
            <img className="welcome-icon" src={require(`${"./main-frame-components/NewLogo.jpg"}`)} />
            <button className="login-button" onClick={this.loadLoginForm}>LOGIN</button>
            <button className="register-button" onClick={this.loadRegisterForm}>REGISTER</button>
            <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
          </div>
        )
      }
      // User has logged in/registered - conversation list screen now active
    } else if (this.state.showConversationsScreen) {

      // Compiles conversation list from data returned from server
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
         // Display list of available conversations
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
    
    // User has selected a conversation and messages from the conversation are displayed
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
