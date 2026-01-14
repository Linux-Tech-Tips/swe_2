/* Library imports */
import React, { useState, useEffect, useContext } from 'react';
import { Cog } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

/* Import local page components */
import styles from './LecturerTimetable.module.css';
import otherStyles from "../assets/BlockOutGrid.module.css"
import PlusIcon from '../components/PlusIcon.jsx';
import UserIcon from '../components/UserIcon.jsx';
import UsersIcon from '../components/UsersIcon.jsx';
import UserCogIcon from '../components/UserCogIcon.jsx';
import GridCell from "../components/GridCell.jsx";
import EditStudentsModal from '../components/EditStudentsModal.jsx';
import {GetRequest} from "../services/Requests.jsx"
import {PostRequest} from "../services/Requests.jsx"
import {ErrorMessage} from "../services/ErrorParsing.jsx"
import { AuthContext } from '../context/Contexts.jsx';


const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
const times = ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00'];

//initial state function for blocked times, is called Availability everywhere bc of GridCell
const getInitialAvailability = () => days.reduce((acc, day) => {
  acc[day] = times.reduce((timeAcc, time) => {
    timeAcc[time] = false; // 'true' means BLOCKED
     return timeAcc;
  }, {});
  return acc;
}, {});

const LecturerTimetable = ({ onLogout }) => {
  const [selectedModuleId, setSelectedModuleId] = useState('NO MODULE SELECTED');
  const [modulesList, setModulesList] = useState([]);
  const [modulesListLoaded, setModulesListLoaded] = useState(false);

  const [currentStudentLimit, setCurrentStudentLimit] = useState(0);
  const [currentMinTAs, setCurrentMinTAs] = useState(0);

  const [openAlgorithmSettings, setOpenAlgorithmSettings] = useState(false);
  const [showEditStudentsModal, setShowEditStudentsModal] = useState(false);
  const [showEditTAsModal, setShowEditTAsModal] = useState(false);
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  /* Loading all modules registered under the currently logged in lecturer */
  useEffect(() => {
    async function fetchModulesList() {
      const result = await GetRequest("api/module/getmodules", {lecturerId: authContext.auth.id}, authContext);
      setModulesList(JSON.parse(result[1]));
      setModulesListLoaded(true);
    }
    fetchModulesList();
  }, []);

  // Function to make clicking the 'Algorithm Settings' button pop up a settings menu
  function OpenAlgorithmSettings() {
    return (
      <div className={styles.algorithmSettings}>
        {"Maximum number of students per session:  "}
        <input className={styles.constraintsInput} defaultValue={currentStudentLimit} onKeyDown={(e) => {if (e.key === "Enter") {ValidateConstraintsInput(e.target.value, "students")}}} />
        <br />
        {"Minimum number of TAs per session:  "}
        <input className={styles.constraintsInput} defaultValue={currentMinTAs} onKeyDown={(e) => {if (e.key === "Enter") {ValidateConstraintsInput(e.target.value, "tas")}}} />
      </div>
    );
  }

  //function to check that the inputted constraints are natural numbers (including 0)
  function ValidateConstraintsInput(e, type) {
    if(e!='' && Number(e)>=0 && Number.isInteger(Number(e))) {
      if(type === "students") {
        setCurrentStudentLimit(e);
      }
      if(type == "tas") {
        setCurrentMinTAs(e);
      }
    }
    else {
      alert("Please enter a natural number, where 0 indicates no limit");
      if(type === "students") {
        setCurrentStudentLimit(0);
      }
      if(type === "tas") {
        setCurrentMinTAs(0);
      }
    }
  }

  //stuff for displaying the blocking, is called availability due to GridCell
  const [blockingTimes, setBlockingTimes] = useState(false);
  const [currentAvailability, setCurrentAvailability] = useState(getInitialAvailability);

  const toggleAvailability = (day, time) => {
    setCurrentAvailability(prev => ({
            ...prev,
            [day]: {
                ...prev[day],
                [time]: !prev[day][time] // Toggle the boolean state
            }
        }));
  };
 
  function BlockTimes() {
    if(!blockingTimes) {
      return null;
    }
    return(
      <div>
        <div className={styles.card}>
          <h2 className={styles.cardTitle}>Block off Times</h2>  
            <div className={styles.cardDescription}>
               <p>
                 {"Click on time slots to mark them as blocked off for the scheduler"}.
               </p>
             </div>
  
        {/* Grid */}
          <div className={otherStyles.gridWrapper}>
            <div className={otherStyles.availabilityGrid}>
             {/* Header Row (Time and Days) */}
              <div className={otherStyles.gridHeaderCell}>Time</div>
                {days.map(day => (
                   <div key={day} className={otherStyles.gridHeaderCell}>{day}</div>
                ))}

         {/* Time Slot Rows */}
             {times.map(time => (
               <React.Fragment key={time}> 
               {/* Time Column */}
               <div className={otherStyles.timeColumnCell}>{time}</div>
               {/* Day Columns */}
                 {days.map(day => (
                   <GridCell
                     key={day}
                     day={day}
                     time={time}
                     isUnavailable={currentAvailability[day][time]}
                     toggleAvailability={toggleAvailability}
                   />
                 ))}
               </React.Fragment>
             ))}
            </div>
         </div>
       </div>
     </div>
    );
  }

  //variable which is either 0 (no scheduler run), 1 (scheduler ran successfully) or 2 (scheduler ran unsuccessfully)
  const [scheduled, setScheduled] = useState(0);
  //scheduledTimes contains all optimal times separated by a , after Schedule() has been called
  const [scheduledTimes, setScheduledTimes] = useState('');
  //function to assemble constraints and call scheduler API
  async function Schedule() {
    let blockedTimes = [];
    for(let i=0; i<5; i++) {
      for(let j=0; j<8; j++) {
        if(currentAvailability[days[i]][times[j]]) {
          blockedTimes.push(j*5+i);
        }
      }
    }

    try{
      const params = {
        moduleCode: selectedModuleId,
        studentLimit: currentStudentLimit,
        minTAs: currentMinTAs,
        blockedTimes: blockedTimes
      };
      const path = "api/scheduler/schedule";

      const [statusCode, result] = await PostRequest(path, params, authContext);
    
      //if there was an error --> set scheduled back to false, display the error
      if(!(statusCode===200)) {
        setScheduled(2);
        setScheduledTimes("HTTP status: " + statusCode + "\nError message: " + ErrorMessage(result));
      }
      else {
        //data is in the format of a string '{"times": [data]}'
        const times = result.slice(10, -2);
        setScheduledTimes(times);
        }
    }
    catch (error) {
       alert(error);
    }
  }

  const [currentStudents, setCurrentStudents] = useState("");
  const [currentTAs, setCurrentTAs] = useState("");
  const [moduleError, setModuleError] = useState("");
  //student and TA lists are saved in the format of a String like: 1111,2222,3333,4444

  //call the getModule api to load module data
  async function ChangeModule(moduleId) {
    if(moduleId == "NO MODULE SELECTED") {
        setSelectedModuleId("NO MODULE SELECTED");
        setBlockingTimes(false);
        setOpenAlgorithmSettings(false);
        setScheduled(0);
        setCurrentStudentLimit(0);
        setCurrentMinTAs(0);
        setCurrentAvailability(getInitialAvailability);
        setCurrentStudents("");
        setCurrentTAs("");
        return;
    }
    /* Try to get the actual module if a real module selected */
    try {
      //get current user id: authContext.auth.id
      const path = "api/module/getmodule";
      const params = {moduleCode: moduleId, lecturerId: authContext.auth.id};
      const [statusCode, result] = await GetRequest(path, params, authContext);
      if(statusCode===200) {
        setSelectedModuleId(moduleId);
        setBlockingTimes(false); 
        setOpenAlgorithmSettings(false); 
        setScheduled(0);
        setCurrentStudentLimit(0);
        setCurrentMinTAs(0);
        setCurrentAvailability(getInitialAvailability);
        let moduleData = result.split(":");
        setSelectedModuleId(moduleData[1].slice(1, -12));
        //these if statements are checking if the student/ta lists are empty, as it can disturb the editStudents display
        if(moduleData[2].slice(1, -12)==="") {
          setCurrentStudents("");
        }
        else{
          setCurrentStudents(moduleData[2].slice(1, -12).split(','));
        }
        if(moduleData[4].slice(1, -2)==="") {
          setCurrentTAs("");
        }
        else{
          setCurrentTAs(moduleData[4].slice(1, -2).split(','));
        }
        setModuleError("");
      }
      //the format of the result is: {"moduleCode":"testModule","students":[1111,2222,3333,4444],"lecturer":10000,"tastudents":[]}
      else {
        setModuleError("HTTP status: " + statusCode + "\nError Message: " + ErrorMessage(result));
      }
    }
    catch (error) {
      setModuleError(error);
      return;
    }
  }

  function DisplayModuleError() {
    if(moduleError==="") {
      return;
    }
    return (<div className={styles.errorMessage}> {moduleError} </div>)
  }

  //function to display the scheduled times
  function DisplayTimes() { 
    if(scheduled===0) {
      return(
      <div>{"Please run the scheduling algorithm to see suggested tutorial times"}</div>
      );
    }
    if(scheduled===2) {
      return(<div className={styles.errorMessage}>Scheduling algorithm not run due to an error!<br/>{scheduledTimes}</div>);
    }
    if(scheduledTimes.length>0) {
      let splitTimes = scheduledTimes.split("\\n"); 
      let divs=[];
      for(let i=0; i<splitTimes.length; i++) {
        divs.push(<div>{splitTimes[i]}</div>);
      }
      return (
        <div>{divs}</div>
      );
    }
   return <div> {"Scheduling..."} </div>
  }

  //call ChangeModule to fetch the new student/ta lists
  const refreshStudentList = () => {
    ChangeModule(selectedModuleId);
    console.log(`[DATA REFRESH] Module ${selectedModuleId} students need to be re-fetched.`);
  };

  return (
    <div className={styles.container}>
      {/* Top Header */}
      <header className={styles.header}>
        <h1 className={styles.title}>Tutorial Time - Lecturer</h1>
        <nav className={styles.navbarNav}>
          <button onClick={() => navigate("/create-module")} className={styles.navButton}>
            <PlusIcon className={styles.navIcon} />
            Create New Module
          </button>
          <button onClick={onLogout} className={styles.navButton}>
            <UserIcon className={styles.navIcon} />
            Sign Out
          </button>
        </nav>
      </header>

      {/* Module Selection Card */}
      <div className={styles.card}>
        <h2 className={styles.cardTitle}>Module Selection</h2>
        <p className={styles.cardDescription}>Choose a module to schedule tutorials. Note that switching modules will erase any inputted algorithm settings data <br/> Press Enter to confirm any inputs </p>
        <div> <DisplayModuleError/> </div>
        <div>
          {modulesListLoaded ? (
            <select className={styles.moduleInputDropdown} value={selectedModuleId} onChange={(e) => {ChangeModule(e.target.value)}}>
              <option value="NO MODULE SELECTED">SELECT MODULE</option>

              {modulesList.map((value) => (
                <option key={value} value={value}>
                  {value}
                </option>
              ))}
            </select>
          ) : (
            <select className={styles.moduleInputDropdown} value="LOADING" disabled>
              <option value="LOADING">Loading Modules...</option>
            </select>
          )}
        <span className={styles.studentCount}>{currentStudents.length} Students</span>
        </div>
        <div>
          The current module you have selected is: {selectedModuleId} <br/> 
        </div>
        {/* The Buttons */}
        <div className={styles.actionButtons}>
          <button className={styles.actionButton}
          onClick={() => setShowEditStudentsModal(true)}>
            <UsersIcon className={styles.actionIcon} />
            Edit Students
          </button>
          <button className={styles.actionButton}
          onClick={() => setShowEditTAsModal(true)}>
            <UserCogIcon className={styles.actionIcon} />
            Manage TAs
          </button>
          <button onClick={() => setBlockingTimes(!blockingTimes)} className={styles.actionButton}>
            <UserCogIcon className={styles.actionIcon} />
            Manage Times
          </button>
          {/* Algorithm Settings Menu */}
          <button onClick={() => setOpenAlgorithmSettings(true)} className={styles.actionButton}>
            <Cog className={styles.actionIcon} />
            Algorithm Settings {"\n"}
            {openAlgorithmSettings && OpenAlgorithmSettings()}
          </button>
          <button onClick={() => {Schedule(); setScheduled(true); setBlockingTimes(false); setOpenAlgorithmSettings(false)}}className={`${styles.actionButton} ${styles.primaryButton}`}>
              SCHEDULE
          </button>
        </div>
      </div>
    <div> <BlockTimes/> </div>
    <div> <DisplayTimes/> </div>
   <EditStudentsModal
        isOpen={showEditStudentsModal}
        onClose={() => setShowEditStudentsModal(false)}
        moduleCode={selectedModuleId} 
        enrolledStudents={currentStudents}
        onUpdateSuccess={refreshStudentList}
        type={'student'}
      />      
   <EditStudentsModal
        isOpen={showEditTAsModal}
        onClose={() => setShowEditTAsModal(false)}
        moduleCode={selectedModuleId} 
        enrolledStudents={currentTAs}
        onUpdateSuccess={refreshStudentList}
        type={'ta'}
      />       
  </div>
  );
};

export default LecturerTimetable;
