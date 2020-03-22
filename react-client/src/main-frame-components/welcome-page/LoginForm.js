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

  handleChange(event) {
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  handleSubmit(event) {
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
