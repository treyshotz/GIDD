/* eslint-disable @typescript-eslint/no-explicit-any */
import { IFetch } from 'api/fetch';
import { LoginRequestResponse, Activity, ActivityRequired, PaginationResponse, RequestResponse, User, UserCreate } from 'types/Types';

export default {
  // Auth
  createUser: (item: UserCreate) => IFetch<RequestResponse>({ method: 'POST', url: 'user/', data: item, withAuth: false }),
  // TODO: Change endpoint
  authenticate: (username: string, password: string) =>
    IFetch<LoginRequestResponse>({
      method: 'POST',
      url: 'log-in/',
      data: { user_id: username, password: password },
      withAuth: false,
    }),
  forgotPassword: (email: string) => IFetch<RequestResponse>({ method: 'POST', url: 'auth/password/reset/', data: { email: email }, withAuth: false }),

  // Activity
  getActivity: (id: string) => IFetch<Activity>({ method: 'GET', url: `activities/${id}/` }),
  getActivities: (filters?: any) => IFetch<PaginationResponse<Activity>>({ method: 'GET', url: `activities/`, data: filters || {} }),
  getMyParticipatingActivities: (filters?: any) =>
    IFetch<PaginationResponse<Activity>>({ method: 'GET', url: `user/userdata/activities/`, data: filters || {} }),
  getMyHostActivities: (filters?: any) => IFetch<PaginationResponse<Activity>>({ method: 'GET', url: `user/userdata/host-activities/`, data: filters || {} }),
  createActivity: (item: ActivityRequired) => IFetch<Activity>({ method: 'POST', url: `activities/`, data: item }),
  updateActivity: (id: string, item: ActivityRequired) => IFetch<Activity>({ method: 'PUT', url: `activities/${id}/`, data: item }),
  deleteActivity: (id: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `activities/${id}/` }),

  // User
  // TODO: Change endpoint
  getUser: () => IFetch<User>({ method: 'GET', url: `user/1/` }),
  updateUser: (userName: string, item: Partial<User>) => IFetch<User>({ method: 'PUT', url: `user/${userName}/`, data: item }),
};
