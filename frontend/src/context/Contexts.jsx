/* Library imports */
import { createContext, useState, useEffect } from 'react';

import { userManager } from './UserManagerConfig.jsx';
import { GetRequest, PostRequest } from '../services/Requests.jsx';

/* Auth context, with auth information, the login function, loginCallback function and logout function */
const AuthContext = createContext({auth: null, login: null, loginCallback: null, logout: null});

/* AuthProvider component */
const AuthProvider = ({children}) => {
    /* Getting initial auth state from session if any saved */
    const [auth, setAuth] = useState(null);

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
        if(!auth) {
            userManager.signinRedirect().then(() => {});
        }
    };

    /* Completes the OIDC login process */
    const loginCallback = () => {
        userManager.signinCallback().then(async (user) => {
            /* Get the authenticated user details */
            let details = getAuthDetails(user);
            /* Check if user exists in database and create if not */
            let checkPath = "api/" + (details.isLecturer ? "lecturer/getlecturer" : "student/getstudent");
            let content = (details.isLecturer ? {staffId: details.id} : {studentId: details.id});
            let getResult = await GetRequest(checkPath, content, {auth: details});
            if(getResult[0] != 200) {
                /* Attempt to create User if GET fails */
                let addPath = "api/" + (details.isLecturer ? "lecturer/createlecturer" : "student/createstudent");
                let content = {name: details.name, email: details.email};
                if(details.isLecturer)
                    content.staffId = details.id;
                else
                    content.studentId = details.id;
                /* Send actual POST request to create user */
                PostRequest(addPath, content, {auth: details}).then((response) => {
                    if(response[0] == 200) {
                        /* On successful login and user creation, update the auth details */
                        console.log("User created successfully!");
                        console.log("Signing in...");
                        setAuth(details);
                    } else {
                        console.log(details, response);
                        setAuth(null);
                    }
                }).catch((err) => {
                    console.log("Error creating user:");
                    console.log(err);
                    setAuth(null);
                });
            } else {
                console.log("Signing in...");
                setAuth(details);
            }
        }).catch((err) => {
            console.log(err);
            setAuth(null);
        });
    };

    /* Logs out user */
    const logout = () => {
        try {
            userManager.signoutRedirect().then(() => {
                console.log("Logged out");
            });
            userManager.clearStaleState();
        } catch(err) {
            console.log("Error signing out");
            console.log(err);
        }
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
