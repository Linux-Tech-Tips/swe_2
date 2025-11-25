import styles from './GridCell.module.css';
/**
 * A single cell within the timetable grid UI.
 * 
 * Each cell represents a (day, time) slot and toggles between
 * "available" and "unavailable" when clicked or activated via keyboard.
 */
const GridCell = ({ day, time, isUnavailable, toggleAvailability }) => {
    const cellId = `${day}-${time}`;
    const className = isUnavailable
        ? `${styles.gridCell} ${styles.unavailableBox}`
        : `${styles.gridCell} ${styles.availableBox}`;

    return (
        <div
            id={cellId}
            tabIndex="0"
            onClick={() => toggleAvailability(day, time)}
            onKeyDown={(e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    toggleAvailability(day, time);
                }
            }}
            className={className}
            role="button"
            aria-label={`Toggle availability for ${day} at ${time}`}/>
    );
};

export default GridCell;
