import styles from './Login.module.css';

const Login = ({ onLoginSuccess }) => {

    /* Login handler callback, using the passed onLoginSuccess prop */
    const handleLogin = () => {
        // TODO Here, implement actual OAuth2 behavior
        /* Simulate successful authentication */
        let demoId = window.prompt("Enter your ID");
        let demoName = window.prompt("Enter your name");
        let demoEmail = window.prompt("Enter your email");
        let demoIsLecturer = window.confirm("Are you a lecturer?");
        setTimeout(() => {
            onLoginSuccess({
                id: demoId, 
                name: demoName, 
                email: demoEmail, 
                isLecturer: demoIsLecturer
            });
        }, 500);
    };

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

export default Login;
