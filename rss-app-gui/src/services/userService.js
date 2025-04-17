import Keycloak from 'keycloak-js';

const _kc = new Keycloak('/keycloak.json');
const withoutRules = process.env.NODE_ENV === 'development';

/**
 * Initializes Keycloak instance and calls the provided callback function if successfully authenticated.
 *
 * @param onAuthenticatedCallback
 */
const initKeycloak = onAuthenticatedCallback => {
  //   if (withoutRules) {
  //     console.log('withoutRules:',withoutRules);
  //     onAuthenticatedCallback();
  //     return;
  //   }
  _kc
    .init({
      onLoad: 'login-required',
      pkceMethod: 'S256',
      checkLoginIframe:false,
    })
    .then(authenticated => {
      if (!authenticated) {
        console.log('user is not authenticated..!');
      } else {
        console.log('keycloak init authenticated !');
      }
      console.log('keycloak init ..');
      console.log('keycloak getToken:' + _kc.token);
      onAuthenticatedCallback();
    })
    .catch(console.error);
};

const doLogin = _kc.login;

const doLogout = _kc.logout;

const getToken = () => _kc.token;

const isLoggedIn = () => !!_kc.token;

const updateToken = () => {
  // if (!withoutRules) {
  //   _kc.updateToken(5).catch(doLogin);
  // }
  _kc.updateToken(5).catch(doLogin);
};

const getUsername = () => _kc.tokenParsed?.preferred_username;

const hasRole = roles => withoutRules || roles.some(role => _kc.hasRealmRole(role));

const userService = {
  initKeycloak,
  doLogin,
  doLogout,
  isLoggedIn,
  getToken,
  updateToken,
  getUsername,
  hasRole,
};

export default userService;
