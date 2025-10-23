import styles from './Auth.module.css';

export default function Login({ onLoginSuccess }) {
  const handleMicrosoftLogin = () => {
    // Simulate successful authentication
    setTimeout(() => {
        onLoginSuccess();
    }, 500);
  };

  return (
    <div className={styles.loginContainer}> 
      <div className={styles.loginCard}>   
        
        <h1 className={styles.heading}>FACTS</h1>
        <p className={styles.description}>
          Sign in with your university Microsoft account
        </p>
        
        <button
          onClick={handleMicrosoftLogin}
          className={styles.microsoftButton}
        >
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
}