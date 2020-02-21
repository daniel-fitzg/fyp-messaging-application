import React from "react"

class LoginForm extends React.Component {

  constructor() {
    super()
    this.state = {
      email: '',
      password: ''
    }

    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  // Updates the value of email and password as user enters input characters
  handleChange(event) {
    // setState() does not immediately update the component, there is a delay
    this.setState({
      email: event.target.value
    })
  }

  handleSubmit(event) {
    //alert("Message: " + this.state.message)
    this.props.sendMessage(event, this.state.message)
  }

  render() {
    return (
      <div>
        <form  className="login-form" onSubmit={this.handleSubmit}>
          <input
            className="email-input"
            id="email-input"
            placeholder="Email"
            type="text"
            onChange={event => this.handleChange}
          />
        </form>
      </div>
    )
  }
}

export default LoginForm
