import React, { useState, useEffect, useRef, useContext } from 'react';
import styles from './EditStudentsModal.module.css';
import { X, Plus, Trash2 } from 'lucide-react';
import {PostRequest} from '../services/Requests.jsx';
import { AuthContext } from '../context/Contexts.jsx';
import {ErrorMessage} from "../services/ErrorParsing.jsx"
/**
 * Modal component for adding/removing students (or TAs) from a module.
 *
 * @param {Object}   props
 * @param {boolean}  props.isOpen          Whether the modal is visible.
 * @param {Function} props.onClose         Callback to close the modal.
 * @param {string}   props.moduleCode      The module code being edited.
 * @param {number[]} props.enrolledStudents A list of currently enrolled student IDs.
 * @param {Function} [props.onUpdateSuccess] Optional callback invoked after a successful update.
 * @param {string}   props.type            Entity type label (e.g. "student", "TA").
 */
const EditStudentsModal = ({ isOpen, onClose, moduleCode, enrolledStudents, onUpdateSuccess, type}) => {
  const [newStudentId, setNewStudentId] = useState('');
  const [status, setStatus] = useState({ loading: false, error: null, success: null });
  const modalRef = useRef(null);
  const authContext = useContext(AuthContext);
  
  // Added this to 3nsure enrolledStudents is an array for mapping, which handles empty string case
  // Pls lmk is this is unnecessary -Lilian
  const displayStudents = Array.isArray(enrolledStudents) ? enrolledStudents : [];

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
        setStatus({ loading: false, error: "Please enter a valid Student ID.", success: null });
        return false;
    }
    
    try {
        const path = `api/module/${action}${type}`;
        const requestBody = { 
          moduleCode: moduleCode,  
          id: parsedId
        };

        const [statusCode, result] = await PostRequest(path, requestBody, authContext);

        if (statusCode === 200) {
            // Adjust success message verb for "remove"
            const verb = action === "remove" ? "remov" : action;
            setStatus({ 
                loading: false, 
                error: null, 
                success: `${type} ID ${id} was ${verb}ed successfully.` 
            });
            
            // Calls parent function refreshes list
            onUpdateSuccess && onUpdateSuccess(); 
            
            return true;

        } else {
            const errorMsg = ErrorMessage(result) || `Server returned status code ${statusCode}.`;
            setStatus({ loading: false, error: errorMsg, success: null });
            return false;
        }

    } catch (error) {
        setStatus({ loading: false, error: 'Network error or connection failed.', success: null });
        console.log(error);
        return false;
    }
  };
  /**
   * Handles clicking the “Add” button for the new ID.
   */
  const handleAddClick = async () => {
    const success = await callEnrollmentAPI(newStudentId, 'add');
    if (success) {
        setNewStudentId(''); // Clear input
    }
  };
  /**
   * Handles clicking the remove button on a specific student in the list.
   *
   * @param {number|string} studentId ID of the student to remove.
   */
  const handleRemoveClick = async (studentId) => {
    // ID is passed directly from list item
    await callEnrollmentAPI(studentId, 'remove');
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
  
  const modalTitleText = type === 'ta' ? 'Teaching Assistants' : 'Students';

  return (
    <div className={styles.modalOverlay} onClick={handleOverlayClick}>
      <div className={styles.modalContent} ref={modalRef} role="dialog" aria-modal="true" aria-labelledby="edit-students-title">
        <div className={styles.modalHeader}>
          <h2 id="edit-students-title" className={styles.modalTitle}>
            Edit {modalTitleText} for {moduleCode}
          </h2>
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
            placeholder={`Enter ${type} ID`}
            value={newStudentId}
            onChange={(e) => setNewStudentId(e.target.value)}
            onKeyDown={handleKeyDown}
            className={styles.studentIdInput}
            aria-label={`${type} ID`}
          />
          <button onClick={handleAddClick} className={styles.addButton}>
            <Plus size={18} /> Add {type}
          </button>
        </div>

        <div className={styles.studentListSection}>
          <h3 className={styles.listTitle}> Registered {type}s ({displayStudents.length})</h3>
          {displayStudents.length === 0 ? (
            <p className={styles.emptyListMessage}>No {type}s registered yet.</p>
          ) : (
            <ul className={styles.studentList}>
              {displayStudents.map((studentId) => (
                <li key={studentId} className={styles.studentListItem}>
                  <span>{studentId}</span>
                  <button onClick={() => handleRemoveClick(studentId)} className={styles.removeButton} aria-label={`Remove ${type} ${studentId}`}>
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
