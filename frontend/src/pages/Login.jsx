/* Library imports */
import { useEffect } from 'react';
import { ThreeDots } from 'react-loader-spinner';

import styles from './Login.module.css';

const Login = ({ handleLogin }) => {

    /* Return the app login page */
    return (
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>

                {/* Heading and description */}
                <h1 className={styles.heading}>Tutorial Time</h1>
                <p className={styles.description}>
                    Sign in with your university Microsoft account
                </p>

                {/* Sign in button */}
                <button
                    onClick={handleLogin}
                    className={styles.microsoftButton}>
                    <div className={styles.microsoftLogo}>
                        <div className={styles.redSquare}></div>
                        <div className={styles.blueSquare}></div>
                        <div className={styles.greenSquare}></div>
                        <div className={styles.yellowSquare}></div>
                    </div>
                    <span>Sign in with Microsoft</span>
                </button>
            </div>
        </div>
    );
};

const CompleteLogin = ({ handleLoginCallback }) => {

    useEffect(() => {
        handleLoginCallback();
    }, []);

    return (
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>

                {/* Heading and description */}
                <h1 className={styles.heading}>Tutorial Time</h1>
                <p className={styles.description}>
                    Signing in...
                </p>

                {/* Loading circle */}
                <ThreeDots 
                    height={60}
                    width={60}
                    color="#499EE3"
                    backgroundColor="#64AAE3"
                    wrapperStyle={{display: 'flex', justifyContent: 'center'}}
                    visible={true}
                    ariaLabel="oval-loading"/>
            </div>
        </div>
    )
}

export { Login, CompleteLogin };
