import { useContext } from 'react';

import BackendConfig from "../services/BackendConfig.jsx";
import { AuthContext, AuthProvider } from '../context/Contexts.jsx';

/* Utility function to get JWT from auth context */
const GetToken = () => {
    /* Getting JWT from current auth context */
    const authContext = useContext(AuthContext);

    /* Getting JWT from the authContext user information */
    if(authContext.auth)
        return authContext.auth.jwt;
    return null;
}

/* Sends a GET request to the backend server
 *
 * path: relative path from the server domain, for example "api/student/gettimetable" 
 * content: js object of request parameters, for example {param1: "value1", param2: "value2", ...}
 */
const GetRequest = async (path, content) => {

    let token = GetToken();

    let url = `${BackendConfig.domainName}${path}`;
    if(content) {
        url += "?" + new URLSearchParams(content);
    }
    const response = await fetch(url, {
        method: "GET",
        mode: "cors",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const result = await response.text();
    return [response.status, result];
}

/* Sends a POST request to the backend server
 *
 * path: relative path from the server domain, for example "api/student/gettimetable" 
 * content: js object of request parameters, for example {param1: "value1", param2: "value2", ...}
 */
const PostRequest = async (path, content) => {

    let token = GetToken();

    const response = await fetch(`${BackendConfig.domainName}${path}`, {
        method: "POST",
        body: new URLSearchParams(content),
        mode: "cors",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const result = await response.text();
    return [response.status, result];
}

export {GetRequest, PostRequest,};
