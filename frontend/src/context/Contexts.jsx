import { createContext, useState, useEffect } from 'react';

/* AuthContext - context containing the authentication details if user authenticated
    Format:
      {
       isLoggedIn: <true | false>
       id: <UNIVERSITY ID>
       name: <USER DISPLAY NAME>
       email: <UNIVERSITY EMAIL>
       isLecturer: <true | false>
      } */
const AuthContext = createContext({auth: null, login: null, logout: null});

/* Function to load any initial state saved in a session on page refresh */
const getSessionState = () => {
    const auth = sessionStorage.getItem("auth");
    return auth ? JSON.parse(auth) : null;
}

/* AuthProvider component */
const AuthProvider = ({children}) => {
    /* Getting initial auth state from session if any saved */
    const [auth, setAuth] = useState(getSessionState);

    /* Updating auth data in session storage whenever auth data updated */
    useEffect(() => {
        sessionStorage.setItem("auth", JSON.stringify(auth));
    }, [auth]);

    /* Takes in user data consisting of id, name, email and type */
    const login = (userData) => {
        setAuth({
            id: userData.id,
            name: userData.name,
            email: userData.email,
            isLecturer: userData.isLecturer
        });
        console.log("Logged in user");
    }

    /* Logs out user */
    const logout = () => {
        setAuth(null);
        console.log("Logged out user");
    }

    /* Wraps any given child elements in an AuthContext, any of auth, login and logout can be accessed */
    return (
        <AuthContext value={{auth, login, logout}}>
            { children }
        </AuthContext>
    );
}

export { AuthContext, AuthProvider };
