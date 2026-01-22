import {PostRequest} from "../services/Requests.jsx"
import {GetRequest} from "../services/Requests.jsx"
import React, { useState, useContext } from 'react';
import { AuthContext, AuthProvider } from '../context/Contexts.jsx';
import { useNavigate } from "react-router-dom";
import HomeIcon from "../components/HomeIcon.jsx";
import UserIcon from '../components/UserIcon.jsx';
import TrashIcon from '../components/TrashIcon.jsx';
import {ErrorMessage} from "../services/ErrorParsing.jsx"
import styles from './UserSettings.module.css';
import Footer from '../components/Footer.jsx';

const UserSettings = ({ onLogout }) => {

  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  /* function to delete user account from the database */
  async function deleteUser() {
    if(!confirm("Are you sure you want to delete your account? This cannot be undone")) {
      return;
    }

    let path = '';
    let params = {};

    if(authContext.auth.isLecturer) {
      path = "api/lecturer/deletelecturer";
      params = {staffId: authContext.auth.id};
    }
    
    else{
      path = "api/student/deletestudent";
      params = {studentId: authContext.auth.id};
    }
    
    const [statusCode, result] = await PostRequest(path, params, authContext);
    alert(statusCode);
    window.open('https://oauth2.unixusergroup.org/if/flow/user-unenrollment/');
  }

  return(
   <div className={styles.container}>
     <header className={styles.header}>
       <h1 className={styles.title}>Tutorial Time - User Settings</h1>
         <nav className={styles.navbarNav}>
         <button onClick={() => navigate("/")} className={styles.navButton}>
           <HomeIcon className={styles.actionIcon}/> Home </button>
         <button onClick={onLogout} className={styles.navButton}>
           <UserIcon className={styles.navIcon} />
           Sign Out
         </button>
         </nav>
     </header>
     <div className={styles.card}>
       <h2 className={styles.cardTitle}> Your Profile </h2>
       <p className={styles.cardDescription}> This is the user data we currently have saved for you. If you wish to change your details, please navigate to the Authentik page. <br/></p>
         <div>
           Your ID: {authContext.auth.id} <br/>
         </div>
         <div>
           Your university email address: {authContext.auth.email} <br/>
         </div>
       <p className={styles.cardDescription}> You can opt in/out of emails here. We will only ever email you in case of a data breach.</p>
       <p className={styles.cardDescription}> Press this button to delete your account. You will be redirected to delete your Authentik account. </p>
       <button className={styles.actionButton}
         onClick = {() => deleteUser()}> 
         <TrashIcon className={styles.actionIcon}/> Delete Account
       </button>
     </div>
   </div>
  );
};

export default UserSettings
