import React from "react"

class TitleHeader extends React.Component {
  constructor(props) {
    super(props)  
    
    this.handleClick = this.handleClick.bind(this)
  }
  
  handleClick() {
    this.props.updateLoadingScreen()
    this.props.loadConversationsScreen()
  }
  
  render() {
    return (
      <div className="title-header">
        <button onClick={this.handleClick}>BACK</button>
        <p>Messaging Application</p>
      </div>
    )
  }
}

export default TitleHeader
