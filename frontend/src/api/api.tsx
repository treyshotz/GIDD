/* eslint-disable @typescript-eslint/no-explicit-any */
import { IFetch } from 'api/fetch';
import {
  LoginRequestResponse,
  Activity,
  ActivityRequired,
  ActivityHost,
  PaginationResponse,
  Registration,
  RequestResponse,
  User,
  UserCreate,
} from 'types/Types';

export default {
  // Auth
  createUser: (item: UserCreate) => IFetch<RequestResponse>({ method: 'POST', url: 'user/', data: item, withAuth: false }),
  // TODO: Change endpoint
  authenticate: (email: string, password: string) =>
    IFetch<LoginRequestResponse>({
      method: 'POST',
      url: 'auth/login',
      data: { email, password },
      withAuth: false,
    }),
  forgotPassword: (email: string) => IFetch<RequestResponse>({ method: 'POST', url: 'auth/password/reset/', data: { email: email }, withAuth: false }),
  refreshAccessToken: () => IFetch<RequestResponse>({ method: 'GET', url: 'auth/refresh_token', refreshAccess: true, withAuth: false }),

  // Activity
  getActivity: (id: string) => IFetch<Activity>({ method: 'GET', url: `activities/${id}/` }),
  getActivities: (filters?: any) => IFetch<PaginationResponse<Activity>>({ method: 'GET', url: `activities/`, data: filters || {} }),
  getMyParticipatingActivities: (filters?: any) =>
    IFetch<PaginationResponse<Activity>>({ method: 'GET', url: `user/userdata/activities/`, data: filters || {} }),
  getMyHostActivities: (filters?: any) => IFetch<PaginationResponse<Activity>>({ method: 'GET', url: `user/userdata/host-activities/`, data: filters || {} }),
  createActivity: (item: ActivityRequired) => IFetch<Activity>({ method: 'POST', url: `activities/`, data: item }),
  updateActivity: (id: string, item: ActivityRequired) => IFetch<Activity>({ method: 'PUT', url: `activities/${id}/`, data: item }),
  deleteActivity: (id: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `activities/${id}/` }),
  getActivityHosts: (id: string) => IFetch<Array<ActivityHost>>({ method: 'GET', url: `activities/${id}/hosts/` }),
  addActivityHost: (id: string, email: string) => IFetch<Array<ActivityHost>>({ method: 'POST', url: `activities/${id}/hosts/`, data: { email } }),
  getRegistration: (activityId: string, userId: string) => IFetch<Registration>({ method: 'GET', url: `activities/${activityId}/users/${userId}/` }),
  getActivityRegistrations: (activityId: string) => IFetch<Array<Registration>>({ method: 'GET', url: `activities/${activityId}/users/` }),
  createRegistration: (activityId: string, userId: string) =>
    IFetch<Registration>({ method: 'POST', url: `activities/${activityId}/users/`, data: { userId } }),
  deleteRegistration: (activityId: string, userId: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `activities/${activityId}/users/${userId}/` }),

  // User
  // TODO: Change endpoint
  getUser: () => IFetch<User>({ method: 'GET', url: `user/2/` }),
  updateUser: (userName: string, item: Partial<User>) => IFetch<User>({ method: 'PUT', url: `user/${userName}/`, data: item }),
};
