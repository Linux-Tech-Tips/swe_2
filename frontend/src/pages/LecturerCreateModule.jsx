/**
    @author Ethan1
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

function retrieveStudents(studentIds) {
    return await fetch("https:localhost:8080/api/student/get", studentIds)
 }

function UploadModule() => {

    }

const LecturerModule = () => {

    // need to put this as part of the form tbh.
    const staffId = 20;
    const lecturer = await fetch("https:localhost:8080/api/lecturer/get", staffId);
    // add the option of adding students
    const studentIds = [];
    const enrollingStudents = retrieveStudents(studentIds);

    //add the option of adding ta's
    const taStudentIds = [];
    const tasForModule = retrieveStudents(taStudentIds);


    // make a small form that takes in the data.
    // if

   return (<form>
             <label>
               Name:
               <input type="text" name="name" />
             </label>
             <input type="submit" value="Submit" />
           </form>);
};

export default LecturerModule;