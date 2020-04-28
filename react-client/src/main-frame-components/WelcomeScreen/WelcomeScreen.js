import React from 'react'
import LoginForm from "./LoginForm"
import RegisterForm from "./RegisterForm"

class WelcomeScreen extends React.Component {
  
  constructor(props) {
    super(props)
    this.state = {
      showLoginForm: false,
      showRegsiterForm: false,
      firstName: "",
      lastName: "" 
    }
    
    this.loadLoginForm = this.loadLoginForm.bind(this)
    this.loadRegisterForm = this.loadRegisterForm.bind(this)
    this.resetWelcomeScreen = this.resetWelcomeScreen.bind(this)
    this.authenticateUser = this.authenticateUser.bind(this)
    this.registerUser = this.registerUser.bind(this)
  }
  
  loadLoginForm() {
    this.setState({
      showLoginForm: !this.state.showLoginForm
    })
  }

  loadRegisterForm() {
    this.setState({
      showRegisterForm: !this.state.showRegisterForm
    })
  }
  
  resetWelcomeScreen() {
    this.setState({
      showLoginForm: false,
      showRegisterForm: false
    })
  }
  
  // HTTP request to server with user input data to authenticate that user is in system and password if correct
  authenticateUser(event, email, password) {
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/AuthenticateUser', true);
     request.onreadystatechange = () => {
         // Server response has been received and ready to be processed
         if (request.readyState === 4 && request.status === 200) {
           var user = JSON.parse(request.responseText)

           if (user.userId != null) {
             console.log("User login successful")
             this.setState({
               firstName: user.firstName,
               lastName: user.lastName,
               showLoadingScreen: true
             })
             
             this.props.loadConversationsScreen(user.userId, this.state.firstName, this.state.lastName)
           } else {
             console.log("User login failed")
             alert("Login failed")
           }
         }
     };

     request.send(JSON.stringify({
         email: email,
         password: password
       }));
  }
  
  // HTTP request to server with user input data to register a new user in the system
  registerUser(event, email, password, firstName, lastName) {
    const request = new XMLHttpRequest()
     request.open('POST', 'http://localhost:8080/tomcat_server_war_exploded/RegisterUser', true);
     request.onreadystatechange = () => {
         // Server response has been received and ready to be processed
         if (request.readyState === 4 && request.status === 200) {
           var result = JSON.parse(request.responseText)

           if (result.registeredUserId != false) {
             console.log("User registraton successful")
             this.setState({
               firstName: firstName,
               lastName: lastName,
               showLoadingScreen: true
             })
             
             this.props.loadConversationsScreen(result.registeredUserId, this.state.firstName, this.state.lastName)
           } else {
             alert("User registration failed")
           }
         }
     };

     request.send(JSON.stringify({
       email: email,
       password: password,
       firstName: firstName,
       lastName: lastName
     }))
  }
    
  getComponent() {
    if (this.state.showLoginForm) {
      // User has clicked login button
      return (
        <div>
          <img className="welcome-icon" src={require(`${"./NewLogo.jpg"}`)} />
          <LoginForm authenticateUser={this.authenticateUser}/>
          <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
        </div>
      )
    } else if (this.state.showRegisterForm) {
      // User has clicked register button
      return (
        <div>
          <img className="welcome-icon" src={require(`${"./NewLogo.jpg"}`)} />
          <RegisterForm registerUser={this.registerUser}/>
          <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
        </div>
      )
    } else {
    return (
      <div>
        <img className="welcome-icon" src={require(`${"./NewLogo.jpg"}`)} />
        <button className="login-button" onClick={this.loadLoginForm}>LOGIN</button>
        <button className="register-button" onClick={this.loadRegisterForm}>REGISTER</button>
        <button className="cancel-button" onClick={this.resetWelcomeScreen}>CANCEL</button>
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

export default WelcomeScreen