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
    const obtainedModuleCode = formData.get("moduleCode");
    const lecturerId = parseInt(formData.get("lecturerId"));

    // Both of these need to be split by a delimiter of some sort and the resulting array is what's gonna be sent to the backend.
    const enrolledStudentIds = formData.get("studentIds").split(" ").map((id) => parseInt(id));
    const taStudentIds = formData.get("taStudentIds").split(" ").map((id) => parseInt(id));

    // this calls the api in order to submit the data to the backend
    // todo: add a small little message that returns the status of API call.
    //await fetch("https:localhost:8080/api/module/createModule", moduleCode, staffId, studentIds, taStudentIds);

    try{
        const params = {
            moduleCode: obtainedModuleCode,
            staffId: lecturerId,
            studentIds: enrolledStudentIds,
            taStudentIds: taStudentIds,
         };

         const response = await fetch( "http://localhost:8080/api/module/createModule" + new URLSearchParams(params).toString(), {
            mode: "cors",
            method: "POST",
          });
         const data = await response.text();
         if(!response.ok) {
           alert("Error: " + data);
           //do exception handling here
         }
         else {
         }
       }
       catch (error) {
          alert(error);
       }

const LecturerModule = () => {
    // make a small form that takes in the data.
   return (<div>
       <form action={UploadModule}>
           <div>
               <label>
                   Name:
                <input type="text" name="name" />
               </label>
           </div>
           <div>
               <label>
                   Module course code:
                   <input type="text" name="moduleCode" />
                   </label>
               </div>
           <div>
               Lecturer's ID:
               <input type="text" name="lecturerId" />
               </div>
           <div>
               <label>
                   The ids of the students that will be enrolled.
                    <input type="text" name="studentIds"/>
               </label>
               </div>
           <div>
               <label>
                   The id's of the TA's of this module.
                   <input type="text" name="taStudentIds" />
               </label>
               </div>
                    <input onClick type="submit" value="Submit" />
                  </form>
       </div>);
};

export default LecturerModule;