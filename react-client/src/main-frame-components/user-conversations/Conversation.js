import React from "react"

class Conversation extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      authorId: this.props.currentUserId,
      secondaryAuthorId: this.props.conversation.userId,
    }

    this.handleClick = this.handleClick.bind(this)
  }

  handleClick() {
    // this.props.loadMessagesScreen(this.state.conversationId, this.state.authorId, this.state.secondaryAuthorId)
    this.props.updateLoadingScreen()
    this.props.loadMessagesScreen(this.state.authorId, this.state.secondaryAuthorId)
  }

  render() {
    return (
      <div className="conversation">
        <p>
          {this.props.conversation.firstName} {this.props.conversation.lastName}
          <button className="select-conversation-button" onClick={this.handleClick}>SELECT</button>
        </p>
      </div>
    )
  }
}

export default Conversation
