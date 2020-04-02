import React from "react"

/*
Creates a Conversation Component from data returned from server
A list of users from server is parsed to create selectable conversation
*/

class Conversation extends React.Component {
  constructor(props) {
    super(props)
    // IDs of current user and secondary user
    this.state = {
      authorId: this.props.currentUserId,
      secondaryAuthorId: this.props.conversation.userId
    }

    this.handleClick = this.handleClick.bind(this)
  }

  handleClick() {
    this.props.updateLoadingScreen()
    this.props.loadMessagesScreen(this.state.authorId, this.state.secondaryAuthorId)
  }

  // Displays div with the secondary user's name and a selectable button
  render() {
    return (
      <div>
        <button className="conversation-button" onClick={this.handleClick}>
          <h3 className="conversation-username">{this.props.conversation.firstName} {this.props.conversation.lastName}</h3>
          <p className="conversation-status">ONLINE</p>
          {this.props.conversation.lastUpdated != null &&
            <p className="conversation-updated">Updated: {this.props.conversation.lastUpdated}</p>
          }

        </button>
      </div>
    )
  }
}

export default Conversation
