import React from "react"

class TitleConversationsList extends React.Component {
  constructor(props) {
    super(props)
    
    this.handleClick = this.handleClick.bind(this)
  }
  
  handleClick() {
    this.props.loadWelcomeScreen()
  }

  render() {
    return (
      <div className="title-header">
        <div>
          <button className="title-back-button" onClick={this.handleClick}>LOGOUT</button>
        </div>
        <div>
          <p>Messaging Application</p>
        </div>
      </div>
    )
  }
}

export default TitleConversationsList
