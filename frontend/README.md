# Frontend Overview

This section of the document details the frontend overview. After reading this section you should have a basic understanding of the frontend components of the project.

## Frontend Plan Summary

- **React Set-Up**
    - **Framework:** Vite
    - **Language:** JavaScript
- **Styling:** CSS Modules (if issues arise Tailwind)
- **Component Structure:** Seperate Looks from Function (more details below)
- **Routing:** React Router DOM
- **API:** Native fetch API

### React SetUp
The frontend is build using **React** and is running on the **Vite** framework.

- **React:** React is a JavaScript library that is used to build single-page applications (SPA's).

    - **Component-Based Architecture:** React builds pages out of small, self-contained parts called **Components**. Instead of building one giant HTML page, React lets you build a page from small, reusable parts.

    - **Declaritive UI:** React is characterised by **States**, which is what informs React what the UI should look like at a given point in time.

    - **Virtual DOM:** React maintaines a virtual DOM to optimise efficiency. When data changes, React only applies the minimum updates to the actual browser DOM rather than updating the entire DOM.


- **Vite Framework:** Speeds up development with quick server start-up and hot module replacement (HMR). When a frontend file changes, the browser updates without a full page reload.

- **JavaScript Language:** JavaScript has been chosen over TypeScript for this project due to the project size. The time taken to learn TypeScript will cancel out the time saved when using it considering we only have a semester to develop this web-app.

### Styling
Will use basic CSS, along with CSS modules to automatically scope class names locally which elimates style conflicts.

### Component Structure MOST IMPORTANT PLS READ
Structures to seperate responsibilities. 

| Directory | Content Type | Role in the Application | Component Type |
| :--- | :--- | :--- | :--- |
| **`src/App.jsx`** | hihjk | ghjghj. | hjkhjk |
| **`src/pages`** | hjkhjk | hjkhjk. | hjkhjk |
| **`src/components`** | hjkhjk | jjkljkl. | hjjkl |
| **`src/context`** | ghjghj | hjkhjk. | hjkhjk |
| **`src/services`** | hjkhjk | hjkhjk. | ghjghj |
| **`src/hooks`** | Chjkhjk | ghjghj. | ghjghj |

### State Management
React Context, maybe Zustland if needed

### Routing
Exlain mechanism here

### API Communication (Native Fetch API)
Makes http requests, focus here cause info is needed for backend interactivitys

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
