import React from 'react';
import './App.css';
import TitleHeader from "./main-frame-components/TitleHeader"
import MessagesList from "./main-frame-components/MessagesList"
import SendMessage from "./main-frame-components/SendMessage"

class App extends React.Component {
  render() {
    return (
      <div>
        <TitleHeader />
        <MessagesList />
        <SendMessage />
      </div>
    )
  }
}

export default App;
