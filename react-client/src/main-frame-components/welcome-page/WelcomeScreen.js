import React from "react"
import LoginButton from "./LoginButton"
import RegisterButton from "./RegisterButton"

class WelcomeScreen extends React.Component {

  constructor() {
    super()
    this.state = {
      showLoginButton: true,
      showRegisterButton: true
    }

    this.updateShowLoginButton = this.updateShowLoginButton.bind(this)
    this.updateShowRegisterButton = this.updateShowRegisterButton.bind(this)
    this.authenicateUser = this.authenticateUser.bind(this)
    this.registerUser = this.registerUser.bind(this)
  }

  authenticateUser(event, email, password) {
    alert("Authenticating User Email: " + email + " " + password)
    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      if (this.readyState === 4 && this.status === 200) {
        var user = JSON.parse(request.responseText)

        if (user.userId == null) {
          alert("Login failed")

        } else {
          alert(user.registerDate)
        }
      }
    })

    //alert(this.props.myString)
    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true)
    request.send(JSON.stringify({
      email: email,
      password: password
    }))
  }

  registerUser(event, email, password, firstName, lastName) {
    alert("RU fields: " + email + " " + password + " " + firstName + " " + lastName)
    var request = new XMLHttpRequest();

    request.addEventListener("readystatechange", function () {
      if (this.readyState === 4 && this.status === 200) {
        var result = JSON.parse(request.responseText)

        if (result.serverResponse) {
          alert("Registraton successful")

        } else {
          alert("Registration failed")
        }
      }
    })

    request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/RegisterUser', true)
    request.send(JSON.stringify({
      email: email,
      password: password,
      firstName: firstName,
      lastName: lastName
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

  getComponent() {
  //  if (this.props.showWelcomeScreen) {
      return (
        <div>
          <img className="welcome-icon" src={require(`${"./speech-bubble.jpg"}`)} />
          <LoginButton
          authenticateUser={this.authenticateUser}
          updateShowLoginButton={this.updateShowLoginButton}
          updateShowRegisterButton={this.updateShowRegisterButton}
          showLoginButton={this.state.showLoginButton} />

          <RegisterButton
          registerUser={this.registerUser}
          updateShowLoginButton={this.updateShowLoginButton}
          updateShowRegisterButton={this.updateShowRegisterButton}
          showRegisterButton={this.state.showRegisterButton} />
        </div>
      )
  //  }
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
