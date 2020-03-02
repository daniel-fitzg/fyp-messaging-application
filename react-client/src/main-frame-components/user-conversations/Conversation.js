import React from "react"

class Conversation extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      conversationId: this.props.conversation.conversationId,
      authorId: this.props.conversation.userId,
      secondaryAuthorId: this.props.conversation.secondaryUserId
    }

    this.handleClick = this.handleClick.bind(this)
  }

  handleClick() {
    this.props.loadMessagesScreen(this.state.conversationId, this.state.authorId, this.state.secondaryAuthorId)
  }

  render() {
    return (
      <div className="conversation">
        <p>{this.props.conversation.secondaryUserName}</p>
        <p>{"Last updated: " + this.props.conversation.lastUpdated}</p>
        <button
          className="select-conversation-button"
          onClick={this.handleClick}>SELECT</button>
      </div>
    )
  }
}

export default Conversation
