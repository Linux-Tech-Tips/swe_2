import React from 'react';
import styles from './GridCell.module.css';

const GridCell = ({ day, time, isUnavailable, toggleAvailability }) => {
  const cellId = `${day}-${time}`;
  const className = isUnavailable
    ? `${styles.gridCell} ${styles.unavailable}`
    : `${styles.gridCell} ${styles.available}`;

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
      aria-label={`Toggle availability for ${day} at ${time}`}
    />
  );
};

export default GridCell;