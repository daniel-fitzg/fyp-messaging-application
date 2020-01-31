import React from 'react';
import './App.css';
import TitleHeader from "./main-frame-components/TitleHeader"
import MessagesList from "./main-frame-components/MessagesList"

class App extends React.Component {
  render() {
    return (
      <div>
        <TitleHeader />
        <h3>Main frame for the app</h3>
        <MessagesList />
      </div>
    )
  }
}

export default App;
