import { UserCog } from 'lucide-react';
/**
 * This allows consistent styling and usage across the application,
 * while still allowing any props to be passed through, such as:
 *  - `size`
 *  - `color`
 *  - `className`
 *  - `onClick`
 *
 * @param {Object} props  Props forwarded directly to the underlying `<UserCog />` icon.
 * @returns {JSX.Element} A user-settings icon component.
 */
const UserCogIcon = (props) => <UserCog {...props} />;

export default UserCogIcon;