const AUTH = '/auth/';

export const AUTH_RELATIVE_ROUTES = {
  LOGIN: '',
  SIGNUP: 'sign-up/',
  FORGOT_PASSWORD: 'forgot-password/',
};

export const AUTH_ROUTES = {
  LOGIN: AUTH + AUTH_RELATIVE_ROUTES.LOGIN,
  SIGNUP: AUTH + AUTH_RELATIVE_ROUTES.SIGNUP,
  FORGOT_PASSWORD: AUTH + AUTH_RELATIVE_ROUTES.FORGOT_PASSWORD,
};

export default {
  LANDING: '/',
  ACTIVITIES: '/aktiviteter/',
  ABOUT: '/about/',
  ...AUTH_ROUTES,
};
