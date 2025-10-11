# Frontend Overview

This section of the document details the frontend overview, detailing the basic information needed to contribute to the frontend implementation. This document assumes you have little to no understanding of **React**. This document does assume the reader has basic frontend development knowledge.

This document is split into two sections:
- Frontend Plan
- Frontend Setup

## Frontend Plan

- **React Set-Up**
    - **Framework:** Vite
    - **Language:** JavaScript
- **Styling:** CSS Modules (if issues arise Tailwind)
- **Component Structure:** Separate Looks from Function
- **Routing:** React Router DOM
- **API:** Native fetch API

### React SetUp
The frontend is built using **React** and is running on the **Vite** framework.

- **React:** React is a JavaScript library that is used to build single-page applications (SPAs).

    - **Component-Based Architecture:** React builds pages out of small, self-contained parts called **Components**. Instead of building one giant HTML page, React lets you build a page from small, reusable parts.

    - **Declarative UI:** React is characterised by **States**, which is what informs React what the UI should look like at a given point in time.

    - **Virtual DOM:** React maintains a virtual DOM to optimise efficiency. When data changes, React only applies the minimum updates to the actual browser DOM rather than updating the entire DOM.


- **Vite Framework:** Speeds up development with quick server start-up and hot module replacement (HMR). When a frontend file changes, the browser updates without a full page reload.

- **JavaScript Language:** JavaScript has been chosen over TypeScript for this project due to the project size. The time taken to learn TypeScript will cancel out the time saved when using it considering we only have a semester to develop this web-app.

### Component Structure
Structured to separate responsibilities. 

| Directory | Content Type | Role in the Application | Component Type |
| :--- | :--- | :--- | :--- |
| **`src/App.jsx`** | Main Application | Entry point; configures router and global wrappers | N/A |
| **`src/pages`** | High Level View | Handles routing, data fetching, state access, and overall view. | Smart/ Container |
| **`src/components`** | Reusable UI Components | Presentation components for UI | Dumb/ Presentational |
| **`src/context`** | Global State Logic | React Context API stores for global data | State Management |
| **`src/services`** | Network Interface | Communication with backend API using Native Fetch API | Data Layer |
| **`src/hooks`** | Custom Logic (if needed) | Reusable custom hooks to abstract complex logic | Logic Abstraction |

### Styling
Will use basic CSS, along with CSS modules to automatically scope class names locally which eliminates style conflicts.

### State Management
Data needs to be stored globally since it is accessed by multiple components. The React Context API will be used to create channels for sharing this global data. If performance suffers, migration to Zustland is possible. Unlikely to be necessary given project scope.

### Routing
React Router DOM allows the application to behave like a multi-page web-app by managing browser's URL history without page reloading. React Router DOM will swap out a component in the main application area for another when a request is sent. Instead of fetching a new HTML file from the server, the DOM will just load the requested component.

### API Communication (Native Fetch API)
Project will the use native JavaScript function `fetch()` to make requests to the backend. When frontend needs data or needs to send data to a REST endpoint, the function in `src/services` will handle the interaction.

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
