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

  componentDidMount() {
    this.interval = setInterval(() => {
      this.props.getConversations()
    }, 2000);
  }

  // Timer is cleared when the message list screen is no longer active/being rendered
  componentWillUnmount() {
   clearInterval(this.interval)
 }

  handleClick() {
    this.props.updateLoadingScreen()
    this.props.getConversation(this.state.authorId, this.state.secondaryAuthorId)
    this.props.loadMessagesScreen(this.state.authorId, this.state.secondaryAuthorId)
  }

  // Displays div with the secondary user's name and a selectable button
  render() {
    return (
      <div>
        <button className="conversation-button" onClick={this.handleClick}>
          <h3 className="conversation-username">{this.props.conversation.firstName} {this.props.conversation.lastName}</h3>
          {this.props.conversation.onlineStatus &&
            <p className="online-status">ONLINE</p>
          }
          {!this.props.conversation.onlineStatus &&
            <p className="offline-status">OFFLINE</p>
          }
          <p className="conversation-updated">Registered: {this.props.conversation.registerDate}</p>
        </button>
      </div>
    )
  }
}

export default Conversation
