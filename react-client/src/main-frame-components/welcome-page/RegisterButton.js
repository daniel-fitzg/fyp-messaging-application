import React from "react"
import RegisterForm from "./RegisterForm"

class RegisterButton extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      showRegisterForm: false
    }

    this.handleClick = this.handleClick.bind(this)
    this.getComponent = this.getComponent.bind(this)
  }

  handleClick() {
    this.setState({
      showRegisterForm: !this.state.showRegisterForm
    })

    this.props.updateShowLoginButton()
    this.props.updateShowRegisterButton()
  }

  renderRegisterForm() {
    if (this.state.showRegisterForm) {
      return (
        <RegisterForm registerUser={this.props.registerUser}/>
      )
    }
  }

  getComponent() {
    if (this.props.showRegisterButton) {
      return (
        <div>
          <button className="register-button" onClick={this.handleClick}>Register</button>
        </div>
      )
    } else {
      return (
        <div>
          {this.renderRegisterForm()}
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

export default RegisterButton
