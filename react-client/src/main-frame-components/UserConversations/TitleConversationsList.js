import React from "react"

/* 
Creates a TitleConversationList Component 
This is the header component for the conversation list screen
It contains a logout button that will log a user out of the application
*/

class TitleConversationsList extends React.Component {
  constructor(props) {
    super(props)
    
    this.handleClick = this.handleClick.bind(this)
  }
  
  handleClick() {
    this.props.logoutUser()
    // Brings user back to login screen when logout button is pressed
    this.props.loadWelcomeScreen()
  }

  // Renders name of logged-in user and a logout button
  render() {
    return (
      <div className="title-header">
        <div>
          <button className="title-back-button" onClick={this.handleClick}>LOGOUT</button>
        </div>
        <div>
          <p>{this.props.firstName} {this.props.lastName}</p>
        </div>
      </div>
    )
  }
}

export default TitleConversationsList
