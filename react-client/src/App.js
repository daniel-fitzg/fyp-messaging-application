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

  sendMessage(event) {
    alert("Sending Request")
    //event.preventDefault()

    var xhr = new XMLHttpRequest();

    xhr.addEventListener("readystatechange", function () {
      if (this.readyState === 4) {
        var data = xhr.responseText
        alert(data)
        console.log(data)
        var parsedJSON = JSON.parse(data)
      }
    });

    xhr.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/GetUserConversations', true);
    xhr.send();
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
