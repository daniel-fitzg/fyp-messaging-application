import React from "react"
import LoginForm from "./LoginForm"

class LoginButton extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      showLoginForm: false
    }

    this.handleClick = this.handleClick.bind(this)
    this.getComponent = this.getComponent.bind(this)
  }

  handleClick() {
    this.setState({
      showLoginForm: !this.state.showLoginForm
    })

    this.props.changeRegisterButtonState()
  }

  getComponent() {
    if (this.state.showLoginForm) {
      return (
        <div>
          <LoginForm />
        </div>
      )
    } else {
      return (
        null
      )
    }
  }

  render() {
    return (
      <div>
        <button className="login-button" onClick={this.handleClick}>Login</button>
        {this.getComponent()}
      </div>
    )
  }
}

export default LoginButton
