/* eslint-disable @typescript-eslint/no-explicit-any */
import { IFetch } from 'api/fetch';
import { setCookie } from 'api/cookie';
import { ACCESS_TOKEN, ACCESS_TOKEN_DURATION } from 'constant';
import {
  LoginRequestResponse,
  Activity,
  ActivityList,
  ActivityRequired,
  ActivityHost,
  PaginationResponse,
  Registration,
  RequestResponse,
  RefreshTokenResponse,
  User,
  UserCreate,
} from 'types/Types';

export default {
  // Auth
  createUser: (item: UserCreate) => IFetch<RequestResponse>({ method: 'POST', url: 'users/', data: item, withAuth: false, tryAgain: false }),
  authenticate: (email: string, password: string) =>
    IFetch<LoginRequestResponse>({
      method: 'POST',
      url: 'auth/login',
      data: { email, password },
      withAuth: false,
    }),
  forgotPassword: (email: string) => IFetch<RequestResponse>({ method: 'POST', url: 'auth/password/reset/', data: { email: email }, withAuth: false }),
  refreshAccessToken: () =>
    IFetch<RefreshTokenResponse>({ method: 'GET', url: 'auth/refresh-token/', refreshAccess: true, withAuth: false, tryAgain: false }).then((tokens) => {
      setCookie(ACCESS_TOKEN, tokens.token, ACCESS_TOKEN_DURATION);
      return tokens;
    }),
  changePassword: (oldPassword: string, newPassword: string) =>
    IFetch<RequestResponse>({ method: 'POST', url: 'auth/change-password/', data: { oldPassword, newPassword } }),

  // Activity
  getActivity: (id: string) => IFetch<Activity>({ method: 'GET', url: `activities/${id}/` }),
  getActivities: (filters?: any) => IFetch<PaginationResponse<ActivityList>>({ method: 'GET', url: `activities/`, data: filters || {} }),
  getMyParticipatingActivities: (filters?: any) => IFetch<PaginationResponse<ActivityList>>({ method: 'GET', url: `user/me/activities/`, data: filters || {} }),
  getMyHostActivities: (filters?: any) => IFetch<PaginationResponse<ActivityList>>({ method: 'GET', url: `user/me/host-activities/`, data: filters || {} }),
  createActivity: (item: ActivityRequired) => IFetch<Activity>({ method: 'POST', url: `activities/`, data: item }),
  updateActivity: (id: string, item: ActivityRequired) => IFetch<Activity>({ method: 'PUT', url: `activities/${id}/`, data: item }),
  deleteActivity: (id: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `activities/${id}/` }),
  getActivityHosts: (id: string) => IFetch<Array<ActivityHost>>({ method: 'GET', url: `activities/${id}/hosts/` }),
  addActivityHost: (id: string, email: string) => IFetch<Array<ActivityHost>>({ method: 'POST', url: `activities/${id}/hosts/`, data: { email } }),
  getRegistration: (activityId: string, userId: string) => IFetch<Registration>({ method: 'GET', url: `activities/${activityId}/registrations/${userId}/` }),
  getActivityRegistrations: (activityId: string) => IFetch<Array<Registration>>({ method: 'GET', url: `activities/${activityId}/registrations/` }),
  createRegistration: (activityId: string, userId: string) =>
    IFetch<Registration>({ method: 'POST', url: `activities/${activityId}/users/`, data: { userId } }),
  deleteRegistration: (activityId: string, userId: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `activities/${activityId}/users/${userId}/` }),

  // User
  getUser: () => IFetch<User>({ method: 'GET', url: `users/me/` }),
  updateUser: (userId: string, item: Partial<User>) => IFetch<User>({ method: 'PUT', url: `users/${userId}/`, data: item }),
};
