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

    this.props.changeLoginButtonState()
  }

  getComponent() {
    if (this.state.showRegisterForm) {
      return (
        <div>
          <RegisterForm />
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
        <button className="register-button" onClick={this.handleClick}>Register</button>
        {this.getComponent()}
      </div>
    )
  }
}

export default RegisterButton
