import {PostRequest} from "../services/Requests.jsx"
import {GetRequest} from "../services/Requests.jsx"
import React, { useState, useContext } from 'react';
import { AuthContext, AuthProvider } from '../context/Contexts.jsx';
import { useNavigate } from "react-router-dom";
import HomeIcon from "../components/HomeIcon.jsx";
import UserIcon from '../components/UserIcon.jsx';
import {ErrorMessage} from "../services/ErrorParsing.jsx"
import styles from './UserSettings.module.css';
import Footer from '../components/Footer.jsx';


const UserSettings = ({ onLogout }) => {
  
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

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
       <p className={styles.cardDescription}> Edit your data, delete your account, or opt out of emails here. To save any changes, press the save button. <br/></p>
         <div>
           Your ID: {authContext.auth.id} <br/>
         </div>
         <div>
           Your university email address: {authContext.auth.email} <br/>
         </div>
     </div>
   </div>
  );
};

export default UserSettings
