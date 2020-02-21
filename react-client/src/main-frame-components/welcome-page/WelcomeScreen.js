import React from "react"
import LoginButton from "./LoginButton"
import RegisterButton from "./RegisterButton"

class WelcomeScreen extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      showLoginButton: true,
      showRegisterButton: true
    }

    this.renderLoginButton = this.renderLoginButton.bind(this)
    this.renderRegisterButton = this.renderRegisterButton.bind(this)
    this.updateShowLoginButton = this.updateShowLoginButton.bind(this)
    this.updateShowRegisterButton = this.updateShowRegisterButton.bind(this)
    this.authenicateUser = this.authenticateUser.bind(this)
  }

  authenticateUser(event, emailCredentials) {
    alert("Authenticating User Email: " + emailCredentials)
    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      if (this.readyState === 4 && this.status === 200) {
        var user = JSON.parse(request.responseText)

        if (user.userId == null) {
          alert("User not found")

        } else {
          alert(user.registerDate)
        }
      }
    })

    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true)
    request.send(JSON.stringify({
      email: emailCredentials
    }))
  }

  updateShowLoginButton() {
    this.setState({
      showLoginButton: !this.state.showLoginButton
    })
  }

  updateShowRegisterButton() {
    this.setState({
      showRegisterButton: !this.state.showRegisterButton
    })
  }

  renderLoginButton() {
    if (this.state.showLoginButton) {
      return (
        <LoginButton
          changeRegisterButtonState={this.changeRegisterButtonState}
          authenticateUser={this.authenticateUser}
        />
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

  getComponent() {
    if (this.props.showWelcomeScreen) {
      return (
        <div>
          <img className="welcome-icon" src={require(`${"./speech-bubble.jpg"}`)} />
          <LoginButton
          authenticateUser={this.authenticateUser}
          updateShowLoginButton={this.updateShowLoginButton}
          updateShowRegisterButton={this.updateShowRegisterButton}
          showLoginButton={this.state.showLoginButton} />

          <RegisterButton
          authenticateUser={this.authenticateUser}
          updateShowLoginButton={this.updateShowLoginButton}
          updateShowRegisterButton={this.updateShowRegisterButton}
          showRegisterButton={this.state.showRegisterButton} />
        </div>
      )
    } else {
      return <p>nah</p>
    }
  }

  render() {
    return (
      <div>
        {this.getComponent()}
      </div>
    )
  }
}

export default WelcomeScreen
