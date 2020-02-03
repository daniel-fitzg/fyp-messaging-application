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
        messages: dummyMessagesData
      }
  }

  render() {
    return (
      <div className="app">
        <TitleHeader />
        <MessagesList messages={this.state.messages}/>
        <SendMessage />
      </div>
    )
  }
}

export default App;
