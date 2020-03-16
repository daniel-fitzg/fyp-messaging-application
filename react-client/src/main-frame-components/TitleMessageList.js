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
    //this.props.updateLoadingScreen()
    this.props.loadConversationsScreen(this.state.userId)
  }

  render() {
    return (
      <div className="title-header">
        <div>
          <button className="title-back-button" onClick={this.handleClick}>BACK</button>
        </div>
        <div>
          <p>Messaging Application</p>
        </div>
      </div>
    )
  }
}

export default TitleMessageList
