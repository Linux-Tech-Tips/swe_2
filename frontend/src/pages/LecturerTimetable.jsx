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
  const [selectedModuleId, setSelectedModuleId] = useState('Enter a module code here');

  const [currentStudentLimit, setCurrentStudentLimit] = useState(0);
  const [currentMinTAs, setCurrentMinTAs] = useState(0);

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

  const [scheduled, setScheduled] = useState(false);
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
       //moduleCode: selectedModuleId,
       moduleCode: 'testModule',
       studentLimit: currentStudentLimit,
       minTAs: currentMinTAs,
       blockedTimes: blockedTimes 
     };

     const response = await fetch( "http://localhost:8080/api/scheduler/schedule?" + new URLSearchParams(params).toString(), {
        mode: "cors",
        method: "POST",
      });
     const data = await response.text();
     if(!response.ok) {
       alert("Error: " + data);
       //do exception handling here
     }
     else {
       //data is in the format of a string '{"times": [t1,t2,...,tn]}'
       const times = data.slice(10, -2);
       setScheduledTimes(times);
     }
   }
   catch (error) {
      alert(error);
   }
    //exception handling???  
  }

  async function ChangeModule(moduleId) {
    try {
      const response = await fetch("http://localhost:8080/api/module/getModule?" + new URLSearchParams({moduleCode: moduleId}).toString(), {mode: "cors", method: "GET",});
      if(response.ok) {
         setSelectedModuleId(moduleId);
         setBlockingTimes(false); 
         setOpenAlgorithmSettings(false); 
         setScheduled(false);
         setCurrentStudentLimit(0);
         setCurrentMinTAs(0);
         setCurrentAvailability(getInitialAvailability);
         alert("A-OKAY");
      }
     else {
       const data = await response.text();
       alert(data); 
     }
   } catch (error) {
      alert(error);
      return;
    }
  }

  const [convertedTimes, setConvertedTimes] = useState("Times have not been scheduled yet");

  function DisplayTimes() {
    if(!scheduled) {
      return(
      <div>{"Please run the scheduling algorithm to see suggested tutorial times"}</div>
      );
    }
   if(scheduledTimes.length>0) { 
    //given a time ID t --> t%5 = index of its day in the day array, t//5 = index of its time in the time array
    let stringTimes = "";
    let splitScheduledTimes = scheduledTimes.split(",");
    for(let i=0; i<splitScheduledTimes.length; i++) {
      stringTimes+=days[parseInt(splitScheduledTimes[i])%5] + " at " + times[Math.floor(parseInt(splitScheduledTimes[i])/5)] + "\n";
    }
    setConvertedTimes(stringTimes);
    return (
      <div> {convertedTimes} </div>
    );
   }
  return <div> {"Scheduling..."} </div>
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
            <input defaultValue={selectedModuleId} onKeyDown={(e) => {if (e.key === "Enter") {ChangeModule(e.target.value)}}}/>
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
          <button onClick={() => {Schedule(); setScheduled(true); setBlockingTimes(false); setOpenAlgorithmSettings(false)}}className={`${styles.actionButton} ${styles.primaryButton}`}>
            SCHEDULE
          </button>
        </div>
      </div>
     <div> <BlockTimes/> </div>
     <div> <DisplayTimes/> </div>
    </div>
  );
};

export default LecturerTimetable;
