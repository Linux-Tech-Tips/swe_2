/**
    @author Ethan
 */

/**
    So what's needed in this file is the ability to have a form that takes in a
    - module code
    - lecturer staff id -> retrieved from api
    - list of ids of students (optional) -> retrieved from api
    - list of ids of ta's (optional) -> retrieved from api

    so what need's to be sent back is a page with the required traits.

    Oh and it returns the response from the backend api.
 */

function UploadModule(formData) {
    const moduleCode = formData.get("moduleCode");
    const staffId = formData.get("lecturerId");

    // Both of these need to be split by a delimiter of some sort and the resulting array is what's gonna be sent to the backend.
    const studentIds = formData.get("studentIds");
    const taStudentIds = formData.get("taStudentIds");

    // this calls the api in order to submit the data to the backend
    // todo: add a small little message that returns the status of API call.
    await fetch("https:localhost:8080/api/module/createModule", moduleCode, staffId, studentIds, taStudentIds);
}

const LecturerModule = () => {
    // make a small form that takes in the data.
   return (<form action={UploadModule}>
             <label>
               Name:
               <input type="text" name="name" />
             </label>
             <input type="submit" value="Submit" />
             <input type="submit" value="moduleCode" />
             <input type="submit" value="lecturerId" />
             <input type="submit" value="studentIds"/>
             <input type="submit" value="taStudentIds" />
           </form>);
};

export default LecturerModule;