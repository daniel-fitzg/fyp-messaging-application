import React from 'react';
import './App.css';
import TitleHeader from "./main-frame-components/TitleHeader"

class App extends React.Component {
  render() {
    return (
      <div>
        <TitleHeader />
        <h3>Main frame for the app</h3>
      </div>
    )
  }
}

export default App;
