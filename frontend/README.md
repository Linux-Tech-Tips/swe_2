# Frontend Setup

This document contains the instructions to get the frontend webserver up and running on your local machine.

The frontend is built with React. Node.js is needed because it includes `npm`, which is used to install the dependencies and run the development server.

Please note that this document doesn't detail how to install `NVM` (Node Version Manager) as I don't deem it necessary for this project. If issues arise with Node.js versions (which I can't see happening) then the Node Version Manager can be installed in that scenario.

This tutorial is split into three sections:
- How to Install Node.js
- How to Install React
- How to Run the Development Web Server

## How to Install Node.js

This installation tutorial will be divided into two parts based on operating system:

- Install on Linux (Arch Linux & Fedora)
- How to Install on MacOS

### How to Install on Linux
**1. Ensure system is up to date:**

Use `sudo dnf update` for Fedora

Use `sudo pacman -Syu` for Arch Linux

**2. Install Node.js and npm**

Use `sudo dnf install nodejs` for Fedora

Use `sudo pacman -S nodejs npm` for Arch Linux

**3. Verify the installation**

Run `node -v` and `npm -v`

Output should be similar to this:
```
lily@fedora:~$ node -v
v22.19.0
lily@fedora:~$ npm -v
10.9.3
```

### How to Install on MacOS
This tutorial uses Homebrew to install Node.js.

**1. Ensure Homebrew is up-to-date**

Use `brew update` in terminal

**2. Install Node.js**

Use `brew install node`

**3. Verify the installation**

Run `node -v` and `npm -v`

Output should be similar to this:
```
lily@fedora:~$ node -v
v22.19.0
lily@fedora:~$ npm -v
10.9.3
```

## How to Install React

React relies on various libraries listed in `package.json`. These need to be downloaded and installed. The commands are universal across all UNIX based systems.

**1. Navigate to the directory**

Run `cd ~/location/of/swe_2/frontend` for Fedora/MacOS/Arch

Replace `~/location/of/swe_2/frontend` with the actual location of the frontend folder of the project.

**2. Install the React Dependencies**

Run `npm install` for Fedora/MacOS/Arch


## How to Run the Development Web Server

**1. Navigate to the directory**

Ensure you are in `cd ~/location/of/swe_2/frontend`

**2. Start the Web Server**

Run `npm run dev`

**3. View the App**

Use link provided in the terminal of the output of the previous command to view the app.
