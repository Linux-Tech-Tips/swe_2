import { User } from 'lucide-react';
/* This component provides a consistent way to use the User icon across
 * the application while passing through any props such as:
 *  - `size`
 *  - `color`
 *  - `className`
 *  - `onClick`
 *
 * @param {Object} props  Props forwarded directly to the underlying `<User />` icon.
 * @returns {JSX.Element} A user/profile icon component.
 */
const UserIcon = (props) => <User {...props} />;

export default UserIcon;