/* Library imports */
import { createContext, useState, useEffect } from 'react';

import { userManager } from './UserManagerConfig.jsx';

/* Auth context, with auth information, the login function, loginCallback function and logout function */
const AuthContext = createContext({auth: null, login: null, loginCallback: null, logout: null});

/* AuthProvider component */
const AuthProvider = ({children}) => {
    /* Getting initial auth state from session if any saved */
    const [auth, setAuth] = useState(null);

    /* Updates the Auth context (with setAuth) to the state in the given UserManager User */
    const updateAuth = (user) => {
        if(user) {
            setAuth({
                id: user.profile.uni_id,
                name: user.profile.name,
                email: user.profile.email,
                isLecturer: user.profile.groups.includes("Lecturer"),
                jwt: user.access_token
            });
        }
    }
    
    /* Starts the OIDC login process */
    const login = () => {
        if(!auth) {
            userManager.signinRedirect().then(() => {});
        }
    };

    /* Completes the OIDC login process */
    const loginCallback = () => {
        userManager.signinCallback().then((user) => {
            updateAuth(user);
        }).catch((err) => {
            console.log(err);
        });
    };

    /* Logs out user */
    const logout = () => {
        userManager.removeUser().then(() => {
            setAuth(null);
        });
        console.log("Logged out user");
    };

    /* Wraps any given child elements in an AuthContext, any of auth, login and logout can be accessed */
    return (
        <AuthContext value={{auth, login, loginCallback, logout}}>
            { children }
        </AuthContext>
    );
}

export { AuthContext, AuthProvider };
