import React from "react"

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

  // Updates the value of email and password as user enters input characters
  handleChange(event) {
    // setState() does not immediately update the component, there is a delay
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  handleSubmit(event) {
    alert("Text: " + this.state.email + this.state.password + this.state.firstName + this.state.lastName)
    this.props.registerUser(event, this.state.email, this.state.password, this.state.firstName, this.state.lastName)
  }

  render() {
    return (
      <div>
        <form  className="register-form" onSubmit={this.handleSubmit}>
          <input
            className="user-input"
            name="email"
            placeholder="Email"
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