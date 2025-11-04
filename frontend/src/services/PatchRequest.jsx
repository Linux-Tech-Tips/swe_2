import BackendConfig from "./BackendConfig.jsx";

/* Sends JSON-body PATCH request to backend
 *
 * path: relative path from the server domain
 * content: js object to be sent as JSON body
 */
const PatchRequest = async (path, content) => {

    const url = `${BackendConfig.domainName}${path}`;
    
    const response = await fetch(url, {
        method: "PATCH",
        headers: {
            'Content-Type': 'application/json', 
        },
        body: JSON.stringify(content),
        mode: "cors"
    });

    const result = await response.text();
    
    try {
        const jsonResult = JSON.parse(result);
        return [response.status, jsonResult];
    } catch (e) {
        return [response.status, result];
    }
}

export default PatchRequest;