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
  ABOUT: '/about/',
  ACTIVITIES: '/aktiviteter/',
  ADMIN_ACTIVITIES: '/admin/aktiviteter/',
  PROFILE: '/profil/',
  ...AUTH_ROUTES,
};
