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

    this.props.updateShowRegisterButton()
    this.props.updateShowLoginButton()
  }
  
  renderLoginForm() {
    if (this.state.showLoginForm) {
      return (
        <LoginForm authenticateUser={this.props.authenticateUser}/>
      )  
    }  
  }

  getComponent() {
    if (this.props.showLoginButton) {
      return (
        <div>
          <button className="login-button" onClick={this.handleClick}>Login</button>
        </div>
      )
    } else {
      return (
        <div>
          {this.renderLoginForm()}
        </div>
      )  
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

export default LoginButton
