/* Library imports */
import { createContext, useState, useEffect } from 'react';
import { UserManager, WebStorageStateStore } from 'oidc-client-ts';

/* Auth context, with auth information, the login function, loginCallback function and logout function */
const AuthContext = createContext({auth: null, login: null, loginCallback: null, logout: null});

/* Function to load any initial state saved in a session on page refresh */
const getSessionState = () => {
    const auth = sessionStorage.getItem("auth");
    return auth ? JSON.parse(auth) : null;
}

/* AuthProvider component */
const AuthProvider = ({children}) => {
    /* Getting initial auth state from session if any saved */
    const [auth, setAuth] = useState(getSessionState);

    /* OpenID Client setup */
    const userManager = new UserManager({
        authority: "https://oauth2.unixusergroup.org/application/o/tutorial-time/",
        client_id: "MHgBwaU8hJhWfXahSXf4ciB9Uf7DOyL4ZHY9JOBB",
        redirect_uri: `${window.location.origin}/auth-callback`,
        scope: "openid profile email uni_id",
        userStore: new WebStorageStateStore({ store: window.sessionStorage })
    });


    /* Updating auth data in session storage whenever auth data updated */
    useEffect(() => {
        sessionStorage.setItem("auth", JSON.stringify(auth));
    }, [auth]);

    /* Starts the OIDC login process */
    const login = () => {
        userManager.signinRedirect().then(() => {});
    };

    /* Completes the OIDC login process */
    const loginCallback = () => {
        console.log("loginCallback reached");
        userManager.signinCallback().then((user) => {
            setAuth({
                id: user.profile.uni_id,
                name: user.profile.name,
                email: user.profile.email,
                isLecturer: user.profile.groups.includes("Lecturer"),
                jwt: user.access_token
            });
            console.log("Logged in user");
            window.location.href = window.location.origin;
        }).catch((err) => {
            console.log("Error Caught in Login Callback");
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
