import React from "react"

class SendMessage extends React.Component {

  constructor() {
    super()
    this.state = {
      message: ''
    }

    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange(event) {
    this.setState({
      message: event.target.value
    })
  }

  handleSubmit(event) {
    event.preventDefault()
    // Need to send a secondary author id here
    this.props.addConversationEntry(event, this.state.message)
    this.setState({
      message: ""
    })
  }

  render() {
    return (
      <div>
        <form  className="send-message" onSubmit={this.handleSubmit}>
          <input
            placeholder="Input message here"
            type="text"
            value={this.state.message}
            onChange={this.handleChange}
          />
        </form>
      </div>
    )
  }
}

export default SendMessage
