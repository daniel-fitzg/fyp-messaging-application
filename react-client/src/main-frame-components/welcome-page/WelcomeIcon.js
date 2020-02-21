import React from "react"
import LoginButton from "./LoginButton"
import RegisterButton from "./RegisterButton"

class WelcomeIcon extends React.Component {

  constructor() {
    super()
    this.state = {
      showLoginButton: true,
      showRegisterButton: true
    }

    this.renderLoginButton = this.renderLoginButton.bind(this)
    this.renderRegisterButton = this.renderRegisterButton.bind(this)
    this.changeLoginButtonState = this.changeLoginButtonState.bind(this)
    this.changeRegisterButtonState = this.changeRegisterButtonState.bind(this)
  }

  renderLoginButton() {
    if (this.state.showLoginButton) {
      return (
        <LoginButton
          changeRegisterButtonState={this.changeRegisterButtonState} />
      )
    }
  }

  renderRegisterButton() {
    if (this.state.showRegisterButton) {
      return (
        <RegisterButton
          changeLoginButtonState={this.changeLoginButtonState} />
      )
    }
  }

  changeLoginButtonState() {
    this.setState({
      showLoginButton: !this.state.showLoginButton
    })
  }

  changeRegisterButtonState() {
    this.setState({
      showRegisterButton: !this.state.showRegisterButton
    })
  }

  render() {
    return (
      <div>
        <img className="welcome-icon" src={require(`${"./speech-bubble.jpg"}`)} />
          {this.renderLoginButton()}
          {this.renderRegisterButton()}
      </div>
    )
  }
}

export default WelcomeIcon
