import React, { useState } from 'react';
import { Cog } from 'lucide-react';
import styles from './LecturerTimetable.module.css';
import PlusIcon from '../components/PlusIcon.jsx';
import UserIcon from '../components/UserIcon.jsx';
import UsersIcon from '../components/UsersIcon.jsx';
import UserCogIcon from '../components/UserCogIcon.jsx';
import PercentageGridCell from '../components/PercentageGridCell.jsx';
import EditStudentsModal from '../components/EditStudentsModal.jsx';

const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
const times = ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00'];

const dummyModules = [
  { id: 'CS101', name: 'CS101 - Introduction to Computer Science', students: 45 },
  { id: 'MA203', name: 'MA203 - Discrete Mathematics', students: 30 },
  { id: 'SE301', name: 'SE301 - Software Engineering Project', students: 20 },
];

const dummyAvailability = {
  'CS101': {
    'Monday': { '09:00': 85, '10:00': 92, '11:00': 78, '12:00': null, '13:00': null, '14:00': null, '15:00': null, '16:00': null },
    'Tuesday': { '09:00': null, '10:00': 88, '11:00': null, '12:00': null, '13:00': null, '14:00': 95, '15:00': null, '16:00': null },
    'Wednesday': { '09:00': 82, '10:00': null, '11:00': null, '12:00': null, '13:00': 90, '14:00': null, '15:00': null, '16:00': null },
    'Thursday': { '09:00': null, '10:00': null, '11:00': 87, '12:00': null, '13:00': null, '14:00': null, '15:00': null, '16:00': null },
    'Friday': { '09:00': null, '10:00': 75, '11:00': null, '12:00': null, '13:00': null, '14:00': null, '15:00': null, '16:00': null },
  },
  'MA203': {
    'Monday': { '09:00': 70, '10:00': 80, '11:00': null, '12:00': null, '13:00': null, '14:00': null, '15:00': null, '16:00': null },
    'Tuesday': { '09:00': null, '10:00': 75, '11:00': null, '12:00': null, '13:00': null, '14:00': 85, '15:00': null, '16:00': null },
    'Wednesday': { '09:00': 65, '10:00': null, '11:00': null, '12:00': null, '13:00': 70, '14:00': null, '15:00': null, '16:00': null },
    'Thursday': { '09:00': null, '10:00': null, '11:00': 80, '12:00': null, '13:00': null, '14:00': null, '15:00': null, '16:00': null },
    'Friday': { '09:00': null, '10:00': 60, '11:00': null, '12:00': null, '13:00': null, '14:00': null, '15:00': null, '16:00': null },
  },
};

const LecturerTimetable = () => {
  const [selectedModuleId, setSelectedModuleId] = useState(dummyModules[0].id);
  const [showEditStudentsModal, setShowEditStudentsModal] = useState(false);

    // State to manage student IDs for each module
  const [moduleStudents, setModuleStudents] = useState({
    'CS101': ['S001', 'S002', 'S003', 'S004', 'S005'],
    'MA203': ['S006', 'S007', 'S008'],
    'SE301': ['S009', 'S010'],
  });

  const currentModule = dummyModules.find(mod => mod.id === selectedModuleId);
  const currentAvailability = dummyAvailability[selectedModuleId] || {};
  const currentEnrolledStudents = moduleStudents[selectedModuleId] || [];

  // Placeholder which highlights 2 highest percentages
  const allPercentages = Object.values(currentAvailability)
    .flatMap(daySlots => Object.values(daySlots))
    .filter(p => p !== null)
    .sort((a, b) => b - a);

  const optimalPercentages = allPercentages.slice(0, 2);

  const isOptimalSlot = (percentage) => {
    return percentage !== null && optimalPercentages.includes(percentage);
  };

  const refreshStudentList = () => {
    console.log(`[DATA REFRESH] Module ${selectedModuleId} students need to be re-fetched.`);
    setShowEditStudentsModal(false);
  };

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
        <p className={styles.cardDescription}>Choose a module to view student availability data</p>
        <div className={styles.moduleSelector}>
          <select
            className={styles.moduleDropdown}
            value={selectedModuleId}
            onChange={(e) => setSelectedModuleId(e.target.value)}
            aria-label="Select module"
          >
            {dummyModules.map(module => (
              <option key={module.id} value={module.id}>
                {module.name}
              </option>
            ))}
          </select>
          <span className={styles.studentCount}>{currentEnrolledStudents.length} Students</span>
        </div>
        <div className={styles.actionButtons}>
          <button 
            className={styles.actionButton}
            onClick={() => setShowEditStudentsModal(true)}
          >
            <UsersIcon className={styles.actionIcon} />
            Edit Students
          </button>
          <button className={styles.actionButton}>
            <UserCogIcon className={styles.actionIcon} />
            Manage TAs
          </button>
          <button className={`${styles.actionButton} ${styles.primaryButton}`}>
            <Cog className={styles.actionIcon} />
            Algorithm Settings
          </button>
        </div>
      </div>

      {/* Student Availability Card */}
      <div className={styles.card}>
        <h2 className={styles.cardTitle}>Student Availability</h2>
        <p className={styles.cardDescription}>
          Higher percentages indicate more students available. Highlighted slots are the 2 most optimal.
        </p>

        <div className={styles.gridWrapper}>
          <div className={styles.availabilityGrid}>
            {/* Time and Days */}
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
                {days.map(day => {
                  const percentage = currentAvailability[day]?.[time] || null;
                  return (
                    <PercentageGridCell
                      key={`${day}-${time}`}
                      percentage={percentage}
                      isOptimal={isOptimalSlot(percentage)}
                    />
                  );
                })}
              </React.Fragment>
            ))}
          </div>
        </div>
      </div>
      <EditStudentsModal
        isOpen={showEditStudentsModal}
        onClose={() => setShowEditStudentsModal(false)}
        moduleCode={selectedModuleId} 
        moduleTitle={currentModule?.name || 'Selected Module'}
        enrolledStudents={currentEnrolledStudents}
        onUpdateSuccess={refreshStudentList}
      />      
    </div>
  );
};

export default LecturerTimetable;