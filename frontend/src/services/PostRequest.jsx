import BackendConfig from "../services/BackendConfig.jsx";

/* Sends a POST request to the backend server
 *
 * path: relative path from the server domain, for example "api/student/gettimetable" 
 * content: js object of request parameters, for example {param1: "value1", param2: "value2", ...}
 */
const PostRequest = async (path, content) => {

    const response = await fetch(`${BackendConfig.domainName}${path}`, {
        method: "POST",
        body: new URLSearchParams(content),
        mode: "cors"
    });

    const result = await response.text();
    return [response.status, result];
}

export default PostRequest;
