import React from "react"

class TitleMessageList extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      userId: this.props.userId
    }

    this.handleClick = this.handleClick.bind(this)
  }

  handleClick() {
    this.props.loadConversationsScreen(this.state.userId)
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
