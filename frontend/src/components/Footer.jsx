import pdf from  "../assets/privacy_policy.pdf"
import styles from './Footer.module.css'

const Footer = () => {
  return (
    <div className={styles.footer}>
      Contact us via email at unixusergroup@gmail.com  <br/>
      Click <a href={pdf} download="PrivacyPolicy.pdf">here</a> to read our privacy policy
    </div>
  );
};

export default Footer;
