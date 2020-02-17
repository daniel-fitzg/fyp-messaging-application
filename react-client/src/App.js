import React from 'react';
import './App.css';
import TitleHeader from "./main-frame-components/TitleHeader"
import MessagesList from "./main-frame-components/MessagesList"
import SendMessage from "./main-frame-components/SendMessage"
import dummyMessagesData from "./dummyMessagesData"

class App extends React.Component {

  constructor() {
      super()
      this.state = {
        messages: dummyMessagesData,
        text: []
      }

      this.sendMessage = this.sendMessage.bind(this)
  }

  sendMessage(event, message) {
    alert("Sending Request")
    //event.preventDefault()

    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      if (this.readyState === 4) {
        var data = request.responseText
        alert(data)
        console.log(data)
        var parsedJSON = JSON.parse(data)
      }
    });

    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetUserConversations', true)
    // request.setRequestHeader("Access-Control-Allow-Origin", "*")
    // request.setRequestHeader("Content-Type", "text/plain;charset=UTF-8")
    //request.send(JSON.stringify({ message: message}))
    request.send(JSON.stringify({ userId: "51dca0e3-f008-4dd6-baf8-63b60348a119" }))
    //request.send(message)
    //request.send();

  }

  render() {
    return (
      <div className="app">
        <TitleHeader />
        <MessagesList
          messages={this.state.messages} />
        <SendMessage
          sendMessage={this.sendMessage} />
      </div>
    )
  }
}

export default App;
