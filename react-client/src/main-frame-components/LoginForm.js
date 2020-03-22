import React from "react"

/* 
Creates a LoginForm Component to receive user input for user login
*/

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

  handleChange(event) {
    // Sets either email or password state based on the 'name' of the input field triggering change event
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  handleSubmit(event) {
    // Prevents re-render of React
    event.preventDefault()
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
