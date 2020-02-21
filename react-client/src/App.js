import React from 'react';
import './App.css';
import TitleHeader from "./main-frame-components/TitleHeader"
import MessagesList from "./main-frame-components/MessagesList"
import SendMessage from "./main-frame-components/SendMessage"
import WelcomeIcon from "./main-frame-components/welcome-page/WelcomeIcon"
import LoginButton from "./main-frame-components/welcome-page/LoginButton"
import dummyMessagesData from "./dummyMessagesData"

class App extends React.Component {

  constructor() {
      super()
      this.state = {
        messages: dummyMessagesData,
        text: []
      }

      this.authenicateUser = this.authenticateUser.bind(this)
      this.getUserConversations = this.getUserConversations.bind(this)
      this.sendMessage = this.sendMessage.bind(this)
  }

  authenticateUser(event, emailCredentials) {
    alert("Authenticating User Email: " + emailCredentials)
    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      if (this.readyState === 4 && this.status === 200) {
        var user = JSON.parse(request.responseText)

        if (user.userId == null) {
          alert("User not found")

        } else {
          alert(user.registerDate)
        }
      }
    })

    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true)
    request.send(JSON.stringify({
      email: emailCredentials
    }))
  }

  getUserConversations() {
    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      var data = request.responseText
      console.log(data)
      var parsedJSON = JSON.parse(data)
    })

    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetUserConversations', true)
    request.send(JSON.stringify({
      userId: "51dca0e3-f008-4dd6-baf8-63b60348a119"
    }))
  }

  sendMessage(event, message) {
    alert("Sending Request" + message)
    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      if (this.readyState === 4) {
        var data = request.responseText
        var parsedJSON = JSON.parse(data)
      }
    })

    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AddConversationEntry', true)
    request.send(JSON.stringify({
      conversationId: "2cd9c01a-dc32-460f-bb8d-735c07d2d130",
      authorId: "e6d290bc-a9d0-4906-be17-6bb694b43a8f",
      createDate: new Date(),
      content: message
    }))
  }

  render() {
    return (
      <div className="app">
        <WelcomeIcon
          authenticateUser={this.authenticateUser}/>
      </div>
    )
  }
}

export default App;
