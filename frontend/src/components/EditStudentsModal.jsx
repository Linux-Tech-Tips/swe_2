import React, { useState, useEffect, useRef } from 'react';
import styles from './EditStudentsModal.module.css';
import { X, Plus, Trash2 } from 'lucide-react';
import PatchRequest from '../services/PatchRequest.jsx';

const EditStudentsModal = ({ isOpen, onClose, moduleCode, moduleTitle, enrolledStudents, onUpdateSuccess}) => {
  const [newStudentId, setNewStudentId] = useState('');
  const [status, setStatus] = useState({ loading: false, error: null, success: null });
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

  const callEnrollmentAPI = async (studentId, action) => {
    setStatus({ loading: true, error: null, success: null });

    const id = studentId.trim();
    const parsedId = parseInt(id, 10);
    
    if (!id || isNaN(parsedId)) {
        setStatus({ loading: false, error: "Please enter a valid numeric Student ID.", success: null });
        return false;
    }
    
    try {
        const path = `api/modules/${moduleCode}/enrollment`;
        const requestBody = { 
            studentId: parsedId,
            action: action
        };

        const [statusCode, result] = await PatchRequest(path, requestBody);

        if (statusCode === 200) {
            setStatus({ 
                loading: false, 
                error: null, 
                success: `Student ID ${id} was ${action.toLowerCase()}ed successfully.` 
            });
            
            // Calls parent function refrehs list
            onUpdateSuccess && onUpdateSuccess(); 
            
            return true;

        } else {
            const errorMsg = result || `Server returned status code ${statusCode}.`;
            setStatus({ loading: false, error: errorMsg, success: null });
            return false;
        }

    } catch (error) {
        setStatus({ loading: false, error: 'Network error or connection failed.', success: null });
        return false;
    }
  };

  const handleAddClick = async () => {
    const success = await callEnrollmentAPI(newStudentId, 'ADD');
    if (success) {
        setNewStudentId(''); // Clear input
    }
  };

  const handleRemoveClick = async (studentId) => {
    // ID is passed directly from list item
    await callEnrollmentAPI(studentId, 'REMOVE');
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleAddClick();
    } else if (e.key === 'Escape') {
      onClose();
    }
  };

  const handleOverlayClick = (e) => {
    if (modalRef.current && !modalRef.current.contains(e.target)) {
      onClose();
    }
  };

  return (
    <div className={styles.modalOverlay} onClick={handleOverlayClick}>
      <div className={styles.modalContent} ref={modalRef} role="dialog" aria-modal="true" aria-labelledby="edit-students-title">
        <div className={styles.modalHeader}>
          <h2 id="edit-students-title" className={styles.modalTitle}>Edit Students for {moduleTitle}</h2>
          <button onClick={onClose} className={styles.closeButton} aria-label="Close modal">
            <X size={20} />
          </button>
        </div>

        {status.loading && <p style={{ color: 'blue', textAlign: 'center' }}>Processing request...</p>}
        {status.error && <p style={{ color: 'red', textAlign: 'center', fontWeight: 'bold' }}>Error: {status.error}</p>}
        {status.success && <p style={{ color: 'green', textAlign: 'center' }}>{status.success}</p>}

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
                  <button onClick={() => handleRemoveClick(studentId)} className={styles.removeButton} aria-label={`Remove student ${studentId}`}>
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