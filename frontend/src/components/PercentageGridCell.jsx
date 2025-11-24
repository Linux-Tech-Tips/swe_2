import styles from './PercentageGridCell.module.css';
/**
 * A grid cell that displays a percentage value and optionally highlights
 * itself if the value represents an optimal or recommended choice.
 *
 * Typically used in timetable or scheduling optimizers where each cell
 * represents the suitability of a given (day/time) slot.
 *
 * @param {Object} props
 * @param {number|null} props.percentage   The percentage value to display.
 *                                         If null, the cell renders empty.
 * @param {boolean} props.isOptimal        Whether this cell represents an
 *                                         optimal option and should be highlighted.
 */
const PercentageGridCell = ({ percentage, isOptimal }) => {
    const className = `${styles.gridCell} ${isOptimal ? styles.optimal : ''}`;

    return (
        <div className={className}>
            {percentage !== null ? `${percentage}%` : ''}
        </div>
    );
};

export default PercentageGridCell;
