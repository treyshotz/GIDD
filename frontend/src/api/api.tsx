/* eslint-disable @typescript-eslint/no-explicit-any */
import { IFetch } from 'api/fetch';
import { setCookie } from 'api/cookie';
import { ACCESS_TOKEN, ACCESS_TOKEN_DURATION } from 'constant';
import { logout } from 'hooks/User';
import {
  Activity,
  ActivityList,
  ActivityRequired,
  ActivityHost,
  FileUploadResponse,
  LoginRequestResponse,
  PaginationResponse,
  Registration,
  RequestResponse,
  RefreshTokenResponse,
  User,
  UserCreate,
} from 'types/Types';

const USERS = 'users';
const ME = 'me';
const AUTH = 'auth';
const ACTIVITIES = 'activities';
const REGISTRATIONS = 'registrations';
const HOSTS = 'hosts';

export default {
  // Auth
  createUser: (item: UserCreate) => IFetch<RequestResponse>({ method: 'POST', url: `${USERS}/`, data: item, withAuth: false, tryAgain: false }),
  authenticate: (email: string, password: string) =>
    IFetch<LoginRequestResponse>({
      method: 'POST',
      url: `${AUTH}/login`,
      data: { email, password },
      withAuth: false,
    }),
  forgotPassword: (email: string) => IFetch<RequestResponse>({ method: 'POST', url: `${AUTH}/forgot-password/`, data: { email }, withAuth: false }),
  resetPassword: (email: string, newPassword: string, token: string) =>
    IFetch<RequestResponse>({ method: 'POST', url: `${AUTH}/reset-password/${token}/`, data: { email, newPassword }, withAuth: false }),
  refreshAccessToken: () =>
    IFetch<RefreshTokenResponse>({ method: 'GET', url: `${AUTH}/refresh-token/`, refreshAccess: true, withAuth: false, tryAgain: true })
      .then((tokens) => {
        setCookie(ACCESS_TOKEN, tokens.token, ACCESS_TOKEN_DURATION);
        return tokens;
      })
      .catch((e) => {
        logout();
        throw e;
      }),
  changePassword: (oldPassword: string, newPassword: string) =>
    IFetch<RequestResponse>({ method: 'POST', url: `${AUTH}/change-password/`, data: { oldPassword, newPassword } }),

  // Activity
  getActivity: (id: string) => IFetch<Activity>({ method: 'GET', url: `${ACTIVITIES}/${id}/` }),
  getActivities: (filters?: any) => IFetch<PaginationResponse<ActivityList>>({ method: 'GET', url: `${ACTIVITIES}/`, data: filters || {} }),
  getMyParticipatingActivities: (userId?: string, filters?: any) =>
    IFetch<PaginationResponse<ActivityList>>({ method: 'GET', url: `${USERS}/${userId || ME}/${REGISTRATIONS}/`, data: filters || {} }),
  getMyHostActivities: (filters?: any) => IFetch<PaginationResponse<ActivityList>>({ method: 'GET', url: `${USERS}/${ME}/${HOSTS}/`, data: filters || {} }),
  createActivity: (item: ActivityRequired) => IFetch<Activity>({ method: 'POST', url: `${ACTIVITIES}/`, data: item }),
  updateActivity: (id: string, item: ActivityRequired) => IFetch<Activity>({ method: 'PUT', url: `${ACTIVITIES}/${id}/`, data: item }),
  deleteActivity: (id: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `${ACTIVITIES}/${id}/` }),

  // Activity hosts
  getActivityHosts: (id: string) => IFetch<Array<ActivityHost>>({ method: 'GET', url: `${ACTIVITIES}/${id}/${HOSTS}/` }),
  addActivityHost: (id: string, email: string) => IFetch<Array<ActivityHost>>({ method: 'POST', url: `${ACTIVITIES}/${id}/${HOSTS}/`, data: { email } }),
  removeActivityHost: (id: string, hostId: string) => IFetch<Array<ActivityHost>>({ method: 'DELETE', url: `${ACTIVITIES}/${id}/${HOSTS}/${hostId}/` }),

  // Activity registrations
  getRegistrations: (activityId: string, filters?: any) =>
    IFetch<PaginationResponse<Registration>>({ method: 'GET', url: `${ACTIVITIES}/${activityId}/${REGISTRATIONS}/`, data: filters || {} }),
  getRegistration: (activityId: string, userId: string) =>
    IFetch<Registration>({ method: 'GET', url: `${ACTIVITIES}/${activityId}/${REGISTRATIONS}/${userId}/` }),
  createRegistration: (activityId: string, userId: string) =>
    IFetch<Registration>({ method: 'POST', url: `${ACTIVITIES}/${activityId}/${REGISTRATIONS}/`, data: { id: userId } }),
  deleteRegistration: (activityId: string, userId: string) =>
    IFetch<RequestResponse>({ method: 'DELETE', url: `${ACTIVITIES}/${activityId}/${REGISTRATIONS}/${userId}/` }),

  // User
  getUser: (userId?: string) => IFetch<User>({ method: 'GET', url: `${USERS}/${userId || ME}/` }),
  updateUser: (userId: string, item: Partial<User>) => IFetch<User>({ method: 'PUT', url: `${USERS}/${userId}/`, data: item }),

  // Upload file
  uploadFile: (file: File | Blob) =>
    IFetch<FileUploadResponse>({ method: 'POST', url: 'https://api.imgbb.com/1/upload?key=909df01fa93bd63405c9a36d662523f3', file, withAuth: false }),
};
