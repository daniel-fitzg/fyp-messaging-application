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

  // Updates the value of message as user enters input characters
  handleChange(event) {
    // setState() does not immediately update the component, there is a delay
    this.setState({
      message: event.target.value
    })
  }

  handleSubmit(event) {
    alert("Message: " + this.state.message)
  }

  render() {
    return (
      <div>
        <form  className="send-message" onSubmit={this.handleSubmit}>
          <input
            placeholder="Input message here"
            type="text"
            onChange={this.handleChange}
          />
        </form>
      </div>
    )
  }
}

export default SendMessage
