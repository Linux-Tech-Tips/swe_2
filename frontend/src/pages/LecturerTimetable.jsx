import React, { useState } from 'react';
import { Cog } from 'lucide-react';
import styles from './LecturerTimetable.module.css';
import otherStyles from "../assets/BlockOutGrid.module.css"
import PlusIcon from '../components/PlusIcon.jsx';
import UserIcon from '../components/UserIcon.jsx';
import UsersIcon from '../components/UsersIcon.jsx';
import UserCogIcon from '../components/UserCogIcon.jsx';
import GridCell from "../components/GridCell.jsx";

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

const LecturerTimetable = () => {
  const dummyModules = [
  { id: 'CS101', name: 'CS101 - Introduction to Computer Science', students: 45, studentLimit: 0, minTAs: 0, blockedTimes: getInitialAvailability},
  { id: 'MA203', name: 'MA203 - Discrete Mathematics', students: 30, studentLimit: 0, minTAs: 0, blockedTimes: getInitialAvailability },
  { id: 'SE301', name: 'SE301 - Software Engineering Project', students: 20, studentLimit: 0, minTAs: 0, blockedTimes: getInitialAvailability },
];

  const [selectedModuleId, setSelectedModuleId] = useState(dummyModules[0].id);

  const currentModule = dummyModules.find(mod => mod.id === selectedModuleId);

  const [currentStudentLimit, setCurrentStudentLimit] = useState(dummyModules[0].studentLimit);
  const [currentMinTAs, setCurrentMinTAs] = useState(dummyModules[0].minTAs);

  const [openAlgorithmSettings, setOpenAlgorithmSettings] = useState(false);

  // Function to make clicking the 'Algorithm Settings' button pop up a settings menu
  function OpenAlgorithmSettings() {
    return (
      <div className={styles.algorithmSettings}>
        {"Maximum number of students per session:"}
        <input defaultValue={currentStudentLimit} onKeyDown={(e) => {if (e.key === "Enter") {ValidateConstraintsInput(e.target.value, "students")}}} />
        <br />
        {"Minimum number of TAs per session: "}
        <input defaultValue={currentMinTAs} onKeyDown={(e) => {if (e.key === "Enter") {ValidateConstraintsInput(e.target.value, "tas")}}} />
      </div>
    );
  }

  //function to check that the inputted constraints are natural numbers (including 0)
  function ValidateConstraintsInput(e, type) {
    if(Number(e)>=0 && Number.isInteger(Number(e))) {
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

  const [scheduled, setScheduled] = useState(false);
  function Schedule() {
    //CONVERT WHATEVER FORM AVAILABILITY DATA CURRENTLY IS IN INTO an array of integers (probably need to convert to arraylist in backend? that or just take an array in the backend idk) 
    //CALL THE SCHEDULER API WITH ALL GIVEN DATA (selectedModuleId, studentLimit, minTAs, blockedTimes)
  }

  function DisplayTimes() {
    if(!scheduled) {
      return(
      <div>{"Please run the scheduling algorithm to see suggested tutorial times"}</div>
      );
    }
    return (
      <div>
        {"The optimal times are 3 and 4"}
      </div>
    );
  }

  //stuff for displaying the blocking, is called availability due to GridCell
  const [blockingTimes, setBlockingTimes] = useState(false);
  const [currentAvailability, setCurrentAvailability] = useState(dummyModules[0].blockedTimes);

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

  return (
    <div className={styles.container}>
      {/* Top Header */}
      <header className={styles.header}>
        <h1 className={styles.title}>Tutorial Time - Lecturer</h1>
        <nav className={styles.navbarNav}>
          <button className={styles.navButton}>
            <PlusIcon className={styles.navIcon} />
            Create New Module
          </button>
          <button className={styles.navButton}>
            <UserIcon className={styles.navIcon} />
            Preferences
          </button>
        </nav>
      </header>

      {/* Module Selection Card */}
      <div className={styles.card}>
        <h2 className={styles.cardTitle}>Module Selection</h2>
        <p className={styles.cardDescription}>Choose a module to schedule tutorials. Note that switching modules will erase any inputted algorithm settings data</p>
        <div className={styles.moduleSelector}>
          <select
            className={styles.moduleDropdown}
            value={selectedModuleId}
            onChange={(e) => {setSelectedModuleId(e.target.value); setBlockingTimes(false); setOpenAlgorithmSettings(false); setScheduled(false); setCurrentStudentLimit(currentModule?.studentLimit), setCurrentMinTAs(currentModule?.minTAs), setCurrentAvailability(currentModule?.blockedTimes)}}
            aria-label="Select module"
          >
            {dummyModules.map(module => (
              <option key={module.id} value={module.id}>
                {module.name}
              </option>
            ))}
          </select>
          <span className={styles.studentCount}>{currentModule?.students || 0} Students</span>
        </div>
        <div className={styles.actionButtons}>
          <button className={styles.actionButton}>
            <UsersIcon className={styles.actionIcon} />
            Edit Students
          </button>
          <button className={styles.actionButton}>
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
          <button onClick={() => {setScheduled(true); setBlockingTimes(false); setOpenAlgorithmSettings(false)}}className={`${styles.actionButton} ${styles.primaryButton}`}>
            SCHEDULE
            <Schedule/>
          </button>
        </div>
      </div>
     <div> <BlockTimes/> </div>
     <div> <DisplayTimes/> </div>
    </div>
  );
};

export default LecturerTimetable;
