import React from 'react';
import './App.css';
import WelcomeScreen from "./main-frame-components/WelcomeScreen/WelcomeScreen"
import UserConversations from "./main-frame-components/UserConversations/UserConversations"
import UserMessages from "./main-frame-components/UserMessages/UserMessages"

class App extends React.Component {

  constructor() {
      super()
      this.state = {
        showWelcomeScreen: true,
        showConversationsScreen: false,
        showMessagesScreen: false,
        showLoadingScreen: false,
        authorId: "",
        secondaryAuthorId: "",
        userId: "",
        secondaryUserId: "",
        firstName: "",
        lastName: "",
        conversationId: "",
      }

      // Bind statements that set the context of 'this' when passed to Components are properties (arguments)
      this.loadWelcomeScreen = this.loadWelcomeScreen.bind(this)
      this.loadConversationsScreen = this.loadConversationsScreen.bind(this)
      this.loadMessagesScreen = this.loadMessagesScreen.bind(this)
      this.updateLoadingScreen = this.updateLoadingScreen.bind(this)
      this.setConversationId = this.setConversationId.bind(this)
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
  }

  loadConversationsScreen(userId, firstName, lastName) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: true,
      showMessagesScreen: false,
      userId: userId,
      firstName: firstName,
      lastName: lastName
    })
  }

  loadMessagesScreen(authorId, secondaryAuthorId) {
    this.setState({
      showWelcomeScreen: false,
      showConversationsScreen: false,
      showMessagesScreen: true,
      authorId: authorId,
      secondaryAuthorId: secondaryAuthorId
    })
  }

  // Inverts current boolean value of the showLoadingScreen flag
  updateLoadingScreen() {
   this.setState({
     showLoadingScreen: !this.state.showLoadingScreen
   })
  }

  setConversationId(conversationId) {
    this.setState({
      conversationId: conversationId
    })
  }

  // Determines which component and elements are currently rendered to the User Interface
  getComponent() {
    if (this.state.showWelcomeScreen) {
      return (
      // Welcome screen is active
      <WelcomeScreen loadConversationsScreen={this.loadConversationsScreen} />
      )
    } else if (this.state.showConversationsScreen) {
      return (
        // User has logged in/registered - conversation list screen now active
        <UserConversations
          loadWelcomeScreen={this.loadWelcomeScreen}
          loadMessagesScreen={this.loadMessagesScreen}
          updateLoadingScreen={this.updateLoadingScreen}
          showConversationsScreen={this.state.showConversationsScreen}
          userId={this.state.userId}
          firstName={this.state.firstName}
          lastName={this.state.lastName}
          setConversationId={this.setConversationId} />
      )
    } else if (this.state.showMessagesScreen) {
      return (
        // User has selected a conversation and messages from the conversation are displayed
        <UserMessages
          authorId={this.state.authorId}
          secondaryAuthorId={this.state.secondaryAuthorId}
          updateLoadingScreen={this.updateLoadingScreen}
          loadConversationsScreen={this.loadConversationsScreen}
          userId={this.state.userId}
          firstName={this.state.firstName}
          lastName={this.state.lastName}
          conversationId={this.state.conversationId} />
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
