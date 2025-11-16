/* Library imports */
import { UserManager, WebStorageStateStore, InMemoryWebStorage } from 'oidc-client-ts';

/* The in-memory user storage for the UserManager instance */
const userStorage = new InMemoryWebStorage();

/* The global configuration for the OIDC user manager instance */
const userManagerConfig = {
    /* Client authority definition */
    authority: "https://oauth2.unixusergroup.org/application/o/tutorial-time/",
    client_id: "MHgBwaU8hJhWfXahSXf4ciB9Uf7DOyL4ZHY9JOBB",
    redirect_uri: `${window.location.origin}/auth-callback`,
    scope: "openid profile email uni_id",

    /* Keep user storage out of local/session */
    userStore: new WebStorageStateStore({ store: userStorage }),
}

/* OpenID User Manager client setup */
const userManager = new UserManager(userManagerConfig);

export { userManager };
