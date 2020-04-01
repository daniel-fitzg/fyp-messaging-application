import React from "react"

/* 
Creates a RegisterForm Component to receive user input for user registration
*/

class RegisterForm extends React.Component {

  constructor() {
    super()
    this.state = {
      email: '',
      password: '',
      firstName: '',
      lastName: ''
    }

    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange(event) {
    // Sets either one of the 4 field's state based on the 'name' of the input field triggering change event
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  handleSubmit(event) {
    event.preventDefault()
    this.props.registerUser(event, this.state.email, this.state.password, this.state.firstName, this.state.lastName)
  }

  render() {
    return (
      <div>
        <form  className="register-form" onSubmit={this.handleSubmit}>
          <input
            className="user-input"
            name="email"
            placeholder="Username"
            type="text"
            onChange={this.handleChange}
          />

          <input
            className="user-input"
            name="password"
            placeholder="Password"
            type="password"
            onChange={this.handleChange}
          />

          <input
            className="user-input"
            name="firstName"
            placeholder="First Name"
            type="text"
            onChange={this.handleChange}
          />

          <input
            className="user-input"
            name="lastName"
            placeholder="Last Name"
            type="text"
            onChange={this.handleChange}
          />

          <button className="form-register-button">REGISTER</button>
        </form>
      </div>
    )
  }
}

export default RegisterForm
