import React from "react"

class RegisterForm extends React.Component {

  constructor() {
    super()
    this.state = {
      email: ''
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
    this.props.authenticateUser(event, this.state.email)
  }

  render() {
    return (
      <div>
        <form  className="register-form" onSubmit={this.handleSubmit}>
          <input
            className="email-input"
            id="email-input"
            placeholder="Email"
            type="text"
            onChange={this.handleChange}
          />
        </form>
      </div>
    )
  }  
}

export default RegisterForm