/* Library imports */
import React, { useState, useEffect, useContext } from 'react';
import { Cog } from 'lucide-react';

/* Import local page components */
import GridCell from "../components/GridCell.jsx";
import gridStyles from '../components/GridCell.module.css';
import styles from './StudentTimetable.module.css';

import { PostRequest } from '../services/Requests.jsx';
import { AuthContext } from '../context/Contexts.jsx';

const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
const times = ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00'];

const getInitialAvailability = () => days.reduce((acc, day) => {
    acc[day] = times.reduce((timeAcc, time) => {
        timeAcc[time] = false; // 'true' means UNAVAILABLE
        return timeAcc;
    }, {});
    return acc;
}, {});

/* NavItem Component (Legend Item) */
const NavItem = ({ colorClass, label }) => (
    <div className={styles.navItem}>
        <div className={`${styles.navItemBox} ${colorClass}`}></div>
        <span>{label}</span>
    </div>
);

const StudentTimetable = ({ onLogout }) => {
    const [availability, setAvailability] = useState(getInitialAvailability);
    const [saveMessage, setSaveMessage] = useState(null);

    const authContext = useContext(AuthContext);

    const toggleAvailability = (day, time) => {
        setAvailability(prev => ({
            ...prev,
            [day]: {
                ...prev[day],
                [time]: !prev[day][time] // Toggle the boolean state
            }
        }));
    };

    let loaded = false;
    const loadDatabaseTimetable = () => {
        if(!loaded) {
            PostRequest("api/student/gettimetable", {studentId: authContext.auth.id}, authContext).then((result) => {
                let availArray = new URLSearchParams(result[1]).getAll("availability");
                for(let dayIdx in days) {
                    for(let timeIdx in times) {
                        let flatIdx = parseInt(parseInt(timeIdx) * 5) + parseInt(dayIdx);
                        if(availArray[flatIdx] == "0") {
                            toggleAvailability(days[dayIdx], times[timeIdx]);
                        }
                    }
                }
            });
            loaded = true;
        }
    };

    useEffect(() => {
        loadDatabaseTimetable();
    }, []);

    const handleSave = () => {
        const unavailableSlotsCount = Object.values(availability).flatMap(Object.values).filter(Boolean).length;

        // Saving availability into array as per the backend requirement
        let availArray = [];
        for(let i = 0; i < 8; ++i) {
            for(let j = 0; j < 5; ++j) {
                let val = Object.values(Object.values(availability)[j])[i];
                availArray.push(val ? 0 : 1);
            }
        }
        PostRequest("api/student/createtimetable", {studentId: authContext.auth.id, availability: availArray}, authContext).then((result) => {
            console.log(result);
            setSaveMessage({
                type: 'success',
                text: `Availability saved! You marked ${unavailableSlotsCount} slots as unavailable.`
            });
        });

        setTimeout(() => setSaveMessage(null), 4000);
    };

    return (
        <div className={styles.container}>

            {/* Top Header */}
            <header className={styles.header}>
                <h1 className={styles.title}>Tutorial Time - Student</h1>
                <button
                    onClick={onLogout}
                    className={styles.settingsButton}
                    title="Settings/Logout"
                >

                    <Cog className={styles.actionIcon} />
                    <span className={styles.settingsText}>Settings</span>
                </button>
            </header>

            {/* Main Card/Container */}
            <div className={styles.mainCard}>
                <div className={styles.topSection}>
                    <div className={styles.instructionsContainer}>
                        <h2 className={styles.markTitle}>Mark Your Unavailable Times</h2>
                        <p className={styles.markInstructions}>
                            Click on time slots when you are <strong className={styles.unavailableText}>UNAVAILABLE</strong> for tutorials. Use <kbd className={styles.kbd}>Tab</kbd> to navigate between slots and <kbd className={styles.kbd}>Enter</kbd> or <kbd className={styles.kbd}>Space</kbd> to toggle.
                        </p>
                    </div>
                    <button
                        onClick={handleSave}
                        className={styles.saveButton}
                    >
                        Save
                    </button>
                </div>

                {/* Save Message */}
                {saveMessage && (
                    <div className={styles.saveMessageSuccess}>
                        {saveMessage.text}
                    </div>
                )}

                {/* Keyboard Navigation Instructions */}
                <div className={styles.keyboardNav}>
                    <h3 className={styles.keyboardNavTitle}>Keyboard Navigation:</h3>
                    <ul className={styles.keyboardNavList}>
                        <li>• Press <kbd className={styles.kbd}>Tab</kbd> to move between time slots</li>
                        <li>• Press <kbd className={styles.kbd}>Enter</kbd> or <kbd className={styles.kbd}>Space</kbd> to mark a slot as unavailable/available</li>
                        <li>• Press <kbd className={styles.kbd}>Shift</kbd> + <kbd className={styles.kbd}>Tab</kbd> to move backwards</li>
                    </ul>
                </div>

                {/* Grid */}
                <div className={styles.gridWrapper}>
                    <div className={styles.availabilityGrid}>
                        {/* Header Row (Time and Days) */}
                        <div className={styles.gridHeaderCell}>Time</div>
                        {days.map(day => (
                            <div key={day} className={styles.gridHeaderCell}>{day}</div>
                        ))}

                        {/* Time Slot Rows */}
                        {times.map(time => (
                            <React.Fragment key={time}>
                                {/* Time Column */}
                                <div className={styles.timeColumnCell}>{time}</div>

                                {/* Day Columns */}
                                {days.map(day => (
                                    <GridCell
                                        key={day}
                                        day={day}
                                        time={time}
                                        isUnavailable={availability[day][time]}
                                        toggleAvailability={toggleAvailability}
                                    />
                                ))}
                            </React.Fragment>
                        ))}
                    </div>
                </div>

                {/* Legend */}
                <div className={styles.legend}>
                    <h3 className={styles.legendTitle}>Legend</h3>
                    <div className={styles.legendItems}>
                        <NavItem colorClass={styles.unavailableBox} label="Unavailable" />
                        <NavItem colorClass={styles.availableBox} label="Available" />
                    </div>
                </div>

            </div>
        </div>
    );
};

export default StudentTimetable;
