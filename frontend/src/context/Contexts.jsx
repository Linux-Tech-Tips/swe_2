/* Library imports */
import { createContext, useState, useEffect } from 'react';

import { userManager } from './UserManagerConfig.jsx';
import { GetRequest, PostRequest } from '../services/Requests.jsx';

/* Auth context, with auth information, the login function, loginCallback function and logout function */
const AuthContext = createContext({
    auth: null, 
    login: null, 
    loginCallback: null, 
    logout: null,
    loginError: null
});

/* AuthProvider component */
const AuthProvider = ({children}) => {
    /* Getting initial auth state from session if any saved */
    const [auth, setAuth] = useState(null);

    /* State to hold login errors */
    const [loginError, setLoginError] = useState(null);

    /* Gets the Auth details from the given UserManager User */
    const getAuthDetails = (user) => {
        if(user) {
            return {
                id: user.profile.uni_id,
                name: user.profile.name,
                email: user.profile.email,
                isLecturer: user.profile.groups.includes("Lecturer"),
                jwt: user.access_token
            };
        }
        return null;
    }
    
    /* Starts the OIDC login process */
    const login = () => {
        setLoginError(null); // Clear previous errors on new login attempt
        if(!auth) {
            userManager.signinRedirect().then(() => {}).catch((err) => {
                setLoginError("Could not redirect to login provider.");
                console.error(err);
            });
        }
    };

    /* Completes the OIDC login process */
    const loginCallback = () => {
        setLoginError(null); // Clear errors
        userManager.signinCallback().then(async (user) => {
            /* Get the authenticated user details */
            let details = getAuthDetails(user);
            
            /* Check if user exists in database and create if not */
            let checkPath = "api/" + (details.isLecturer ? "lecturer/getlecturer" : "student/getstudent");
            let content = (details.isLecturer ? {staffId: details.id} : {studentId: details.id});
            
            try {
                let getResult = await GetRequest(checkPath, content, {auth: details});
                
                if(getResult[0] != 200) {
                    /* Attempt to create User if GET fails */
                    let addPath = "api/" + (details.isLecturer ? "lecturer/createlecturer" : "student/createstudent");
                    let createContent = {name: details.name, email: details.email};
                    
                    if(details.isLecturer)
                        createContent.staffId = details.id;
                    else
                        createContent.studentId = details.id;

                    /* Send actual POST request to create user */
                    PostRequest(addPath, createContent, {auth: details}).then((response) => {
                        if(response[0] == 200) {
                            /* On successful login and user creation, update the auth details */
                            console.log("User created successfully!");
                            console.log("Signing in...");
                            setAuth(details);
                        } else {
                            console.log(details, response);
                            setLoginError("Failed to create user account. Please contact support.");
                            setAuth(null);
                        }
                    }).catch((err) => {
                        console.log("Error creating user:");
                        console.log(err);
                        setLoginError("Network error while creating user.");
                        setAuth(null);
                    });
                } else {
                    console.log("Signing in...");
                    setAuth(details);
                }
            } catch (err) {
                console.log("Error verifying user:", err);
                setLoginError("Error connecting to server.");
                setAuth(null);
            }

        }).catch((err) => {
            console.log(err);
            setLoginError("Authentication failed. Please try again.");
            setAuth(null);
        });
    };

    /* Logs out user */
    const logout = () => {
        userManager.removeUser().then(() => {
            setAuth(null);
            setLoginError(null);
        });
        console.log("Logged out user");
    };

    /* Wraps any given child elements in an AuthContext, any of auth, login and logout can be accessed */
    return (
        <AuthContext value={{auth, login, loginCallback, logout, loginError}}>
            { children }
        </AuthContext>
    );
}

export { AuthContext, AuthProvider };
