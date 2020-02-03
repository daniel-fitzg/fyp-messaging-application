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
      }

      this.sendMessage = this.sendMessage.bind(this)
  }

  sendMessage() {
    alert("in sendMessage")

    // Not working. Re-render appears to reload dummy data into messages array
    const newMessages = this.state.messages.map(message => Object.assign({}, message))
    newMessages[1].text = "Good Day!"
    this.setState({messages: newMessages})
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
