
import {PostRequest} from "../services/Requests.jsx"
import React, { useState, useContext } from 'react';
import styles from './LecturerCreateModule.module.css';
import { AuthContext, AuthProvider } from '../context/Contexts.jsx';
import { useNavigate } from "react-router-dom";
import HomeIcon from "../components/HomeIcon.jsx";

const LecturerCreateModule = () => {

    const [moduleSuccess, setModuleSuccess] = useState("");
    const [moduleError, setModuleError] = useState("");

    function DisplayModuleStatus() {
        if (moduleError !== "" && moduleSuccess === "") {
            return (<div className={styles.errorMessage}> {moduleError} </div>)
        } else if (moduleError === "" && moduleSuccess !== "") {
            return (<div className={styles.successMessage}> {moduleSuccess} </div>)   
        } else {
            return;
        }
    }

    function ErrorParsing(error) {
        let errorArray = error.split(":");
        return errorArray[errorArray.length-1].trim();
    }
    
    const authContext = useContext(AuthContext);
    const navigate = useNavigate();

    function parseStudentIds(ids) {
        var ids = ids.split(",").map((id) => id.trim()).filter((id) => {return (id !== "");});
        var result = ids.filter((id) => { return (!isNaN(id)); });
        
        // to compute the ids that aren't valid.
        var nullIds = new Set(ids).difference(new Set(result));

        // convert the remaining valid ids into longs so that the backend can handle them correctly.
        result = result.map((id) => BigInt(id));
        return [result, Array.from(nullIds)];
    }

    async function UploadModule(formData) {
         // resetting the error and success messages before request has been attempted.
        setModuleError("");
        setModuleSuccess("");

        const obtainedModuleCode = formData.get("moduleCode");
        if (obtainedModuleCode.trim() === "") {
            setModuleError("Error: The provided module code was empty");
            return;
        }
        
        const [enrolledStudentIds, invalidStudentIds] = parseStudentIds(formData.get("studentIds"));
        if (invalidStudentIds.length != 0) {
            setModuleError("Error: The provided student ids are invalid: " + invalidStudentIds.toString());
            return;
        }

        const [taStudentIds, invalidTaStudentIds] = parseStudentIds(formData.get("taStudentIds"));
        if (invalidTaStudentIds.length != 0) {
            setModuleError("Error: The provided ta ids are invalid: " + invalidTaStudentIds.toString());
            return;
        }
    
        try{
                const params = {
                    lecturerId: authContext.auth.id,
                    moduleCode: obtainedModuleCode,
                    studentIds: enrolledStudentIds,
                    taStudentIds: taStudentIds,
                };

                const path = "api/module/createmodule";

                PostRequest(path, params, authContext).then((result) => {

                    const [statusCode, response] = result;
                    if (statusCode == 200) {
                        setModuleSuccess("The module has been added correctly");
                    } else {
                        setModuleError("HTTP status " + statusCode + "\nError: " + ErrorParsing(response));
                    }
                });
            
            }
            catch (error) {
                setModuleError(error);
            }
        }

    // make a small form that takes in the data.
   return (

   <div className={styles.container}> 
    <header className={styles.header}>
        <h1 className={styles.title}>Tutorial Time - Lecturer</h1>
        <nav className={styles.navbarNav}>
         <button onClick={() => navigate("/lecturer-timetable")} className={styles.navButton}> 
            <HomeIcon className={styles.actionIcon}/> Home </button>
        </nav>
      </header>

    <div className={styles.card}>
        <h2 className={styles.cardTitle}>Module Creation</h2>
        <p className={styles.cardDescription}>Enter a module code to associate with the new course. Note that the student & ta ids fields can be empty.<br/></p>
       
       <div> <DisplayModuleStatus /> </div>
    
       <form action={UploadModule}>
           <div>
               <label htmlFor="moduleCode">
                   Module course code: <br/>
                  <input type="text" className={styles.moduleInput} id="moduleCode" name="moduleCode" />
               </label> 
               </div>
           <div>
               <label htmlFor="studentIds">
                   Enrolled student ids: <br/>
                    <input type="text" className={styles.moduleInput} id="studentIds" name="studentIds"/>
               </label>
               </div>
           <div>
               <label htmlFor="taStudentIds">
                   Module TA ids: <br/>
                   <input type="text" id="taStudentIds" className={styles.moduleInput}  name="taStudentIds" />
               </label>
               </div>
                    <input type="submit" className={`${styles.actionButton} ${styles.primaryButton}`} value="Submit" />
                  </form>
            </div>
       </div>);
};

export default LecturerCreateModule;
