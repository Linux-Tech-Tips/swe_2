import pdf from  "../assets/privacy_policy.pdf"
import styles from './Footer.module.css'

const Footer = () => {
  return (
    <div className={styles.footer}>
      <div className={styles.text}>
        Contact us via email at unixusergroup@gmail.com  <br/>
        Click <a href={pdf} download="PrivacyPolicy.pdf">here</a> to read our privacy policy
      </div>
      <div className={styles.text}>
        By using Tutorial Time, you are consenting to the use of your personal data as outlined in our privacy policy <br/>
        You can revoke your consent at any time by contacting us
      </div>
    </div>
  );
};

export default Footer;
