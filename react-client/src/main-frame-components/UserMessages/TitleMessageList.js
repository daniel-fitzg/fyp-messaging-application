import React from "react"

/* 
Creates a TitleMessageList Component 
This is the header component for the messages list screen
It contains a back button that will bring a user back to the conversation list screen from the messages screen
*/

class TitleMessageList extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      userId: this.props.userId
    }

    this.handleClick = this.handleClick.bind(this)
  }

  handleClick() {
    this.props.updateLoadingScreen()
    this.props.loadConversationsScreen(this.props.userId, this.props.firstName, this.props.lastName)
  }

  render() {
    return (
      <div className="title-header">
        <div>
          <button className="title-back-button" onClick={this.handleClick}>BACK</button>
        </div>
        <div>
          <p>{this.props.firstName} {this.props.lastName}</p>
        </div>
      </div>
    )
  }
}

export default TitleMessageList
