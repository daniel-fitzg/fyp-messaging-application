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
      [event.target.name]: event.target.value
    })
  }

  handleSubmit(event) {
    event.preventDefault()
    alert("Email being sent: " + this.state.email + ", Password: " + this.state.password)
    //alert("Message: " + this.state.message)
    this.props.authenticateUser(event, this.state.email, this.state.password)
  }

  render() {
    return (
      <div>
        <form  className="login-form" onSubmit={this.handleSubmit}>
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
          
            <button className="form-login-button">LOGIN</button>
        </form>        
      </div>
    )
  }
}

export default LoginForm
