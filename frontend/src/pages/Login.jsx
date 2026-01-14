/* Library imports */
import { useContext } from 'react';
import { AuthContext } from '../context/Contexts.jsx';
import { useEffect } from 'react';
import { ThreeDots } from 'react-loader-spinner';

import styles from './Login.module.css';
import Footer from '../components/Footer.jsx';

const Login = ({ handleLogin }) => {
    /* Access the error state directly from context */
    const { loginError } = useContext(AuthContext);

    /* Return the app login page */
    return (
      <div>
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>

                {/* Heading and description */}
                <h1 className={styles.heading}>Tutorial Time</h1>
                <p className={styles.description}>
                    {/*Sign in with your university Microsoft account <- FIXME once MS gives us access*/}
                    Sign in with your account
                </p>

                {/* Error Display - Only shows if loginError exists */}
                {loginError && (
                    <div style={{
                        backgroundColor: '#ffebee', 
                        color: '#c62828', 
                        padding: '10px', 
                        borderRadius: '4px', 
                        marginBottom: '15px',
                        fontSize: '0.9rem',
                        textAlign: 'center'
                    }}>
                        {loginError}
                    </div>
                )}

                {/* Sign in button */}
                <button
                    onClick={handleLogin}
                    className={styles.microsoftButton}>
                    {/* FIXME once MS gives us access
                    <div className={styles.microsoftLogo}>
                        <div className={styles.redSquare}></div>
                        <div className={styles.blueSquare}></div>
                        <div className={styles.greenSquare}></div>
                        <div className={styles.yellowSquare}></div>
                    </div>
                    <span>Sign in with Microsoft</span>
                    */}
                    <span>Sign in</span>
                </button>
            </div>
        </div>
        <Footer/>
      </div>
    );
};

const CompleteLogin = ({ handleLoginCallback }) => {
    /* Access the error state to see if callback fa9led */
    const { loginError } = useContext(AuthContext);

    useEffect(() => {
        // Only trigger callback if there isn't already an error
        if(!loginError) {
            handleLoginCallback();
        }
    }, []);

    return (
      <div>
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>

                {loginError ? (
                     <>
                        <h1 className={styles.heading} style={{color: '#c62828'}}>Login Failed</h1>
                        <p className={styles.description}>
                            {loginError}
                        </p>
                        <button 
                            className={styles.microsoftButton}
                            onClick={() => window.location.href = '/'} 
                            style={{marginTop: '20px', justifyContent: 'center'}}
                        >
                            Return to Login
                        </button>
                     </>
                ) : (
                    <>

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
                        </>
                    )}
            </div>
          </div>
          <Footer/>
        </div>
    )
}

export { Login, CompleteLogin };
