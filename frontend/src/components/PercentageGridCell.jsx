import styles from './PercentageGridCell.module.css';

const PercentageGridCell = ({ percentage, isOptimal }) => {
  const className = `${styles.gridCell} ${isOptimal ? styles.optimal : ''}`;

  return (
    <div className={className}>
      {percentage !== null ? `${percentage}%` : ''}
    </div>
  );
};

export default PercentageGridCell;