import React, { useState, useEffect, useRef } from 'react';
import styles from './EditStudentsModal.module.css';
import { X, Plus, Trash2 } from 'lucide-react';

const EditStudentsModal = ({ isOpen, onClose, moduleName, enrolledStudents, onAddStudent, onRemoveStudent }) => {
  const [newStudentId, setNewStudentId] = useState('');
  const modalRef = useRef(null);

  useEffect(() => {
    if (isOpen) {
      // Focus the input when the modal opens for better accessibility
      const inputElement = modalRef.current?.querySelector('input');
      if (inputElement) {
        inputElement.focus();
      }
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleAddClick = () => {
    if (newStudentId.trim() && !enrolledStudents.includes(newStudentId.trim())) {
      onAddStudent(newStudentId.trim());
      setNewStudentId('');
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleAddClick();
    } else if (e.key === 'Escape') {
      onClose();
    }
  };

  // Close modal if clicking outside of it
  const handleOverlayClick = (e) => {
    if (modalRef.current && !modalRef.current.contains(e.target)) {
      onClose();
    }
  };

  return (
    <div className={styles.modalOverlay} onClick={handleOverlayClick}>
      <div className={styles.modalContent} ref={modalRef} role="dialog" aria-modal="true" aria-labelledby="edit-students-title">
        <div className={styles.modalHeader}>
          <h2 id="edit-students-title" className={styles.modalTitle}>Edit Students for {moduleName}</h2>
          <button onClick={onClose} className={styles.closeButton} aria-label="Close modal">
            <X size={20} />
          </button>
        </div>

        <div className={styles.inputSection}>
          <input
            type="text"
            placeholder="Enter Student ID"
            value={newStudentId}
            onChange={(e) => setNewStudentId(e.target.value)}
            onKeyDown={handleKeyDown}
            className={styles.studentIdInput}
            aria-label="Student ID"
          />
          <button onClick={handleAddClick} className={styles.addButton}>
            <Plus size={18} /> Add Student
          </button>
        </div>

        <div className={styles.studentListSection}>
          <h3 className={styles.listTitle}>Enrolled Students ({enrolledStudents.length})</h3>
          {enrolledStudents.length === 0 ? (
            <p className={styles.emptyListMessage}>No students enrolled yet.</p>
          ) : (
            <ul className={styles.studentList}>
              {enrolledStudents.map((studentId) => (
                <li key={studentId} className={styles.studentListItem}>
                  <span>{studentId}</span>
                  <button onClick={() => onRemoveStudent(studentId)} className={styles.removeButton} aria-label={`Remove student ${studentId}`}>
                    <Trash2 size={16} />
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className={styles.modalFooter}>
          <button onClick={onClose} className={styles.doneButton}>Done</button>
        </div>
      </div>
    </div>
  );
};

export default EditStudentsModal;