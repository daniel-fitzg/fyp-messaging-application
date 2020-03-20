const {app, BrowserWindow} = require('electron')

function createWindow () {
  // Create the browser window.
	win = new BrowserWindow({width: 600, height: 600})

	win.loadURL('http://localhost:3000/')

	// win.webContents.openDevTools();
}

app.on('ready', createWindow)
