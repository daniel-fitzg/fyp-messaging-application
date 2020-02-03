import React from "react"

class SendMessage extends React.Component {
  render() {
    return (
      <div>
        <form  className="send-message">
          <input
            placeholder="Input message here"
            type="text"
          />
        </form>
      </div>
    )
  }
}

export default SendMessage
