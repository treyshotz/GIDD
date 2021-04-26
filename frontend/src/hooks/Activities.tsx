import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import { Activity, ActivityList, ActivityRequired, UserList, PaginationResponse, Registration, RequestResponse } from 'types/Types';

export const ACTIVITIES_QUERY_KEY = 'activities';
export const ACTIVITIES_QUERY_KEY_REGISTRATION = 'activity_registrations';
export const HOSTS_QUERY_KEY = 'activity_hosts';
export const INVITED_USERS_QUERY_KEY = 'activity_invited_users';

/**
 * Get a specific activity
 * @param id - Id of activity
 */
export const useActivityById = (id: string) => {
  return useQuery<Activity, RequestResponse>([ACTIVITIES_QUERY_KEY, id], () => API.getActivity(id), { enabled: id !== '' });
};

/**
 * Get all activities, paginated
 * @param filters - Filtering
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useActivities = (filters?: any) => {
  return useInfiniteQuery<PaginationResponse<ActivityList>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, filters],
    ({ pageParam = 0 }) => API.getActivities({ ...filters, page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

/**
 * Get activities where the signed in user is participating, paginated
 * @param filters - Filtering
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useMyParticipatingActivities = (userId?: string, filters?: any) => {
  return useInfiniteQuery<PaginationResponse<ActivityList>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, 'me_participating', filters],
    ({ pageParam = 0 }) => API.getMyParticipatingActivities(userId, { ...filters, page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

/**
 * Get activities where the signed in user is host or creator, paginated
 * @param filters - Filtering
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useMyHostActivities = (filters?: any) => {
  return useInfiniteQuery<PaginationResponse<ActivityList>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, 'me_host', filters],
    ({ pageParam = 0 }) => API.getMyHostActivities({ ...filters, page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

/**
 * Create a new activity
 */
export const useCreateActivity = (): UseMutationResult<Activity, RequestResponse, ActivityRequired, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((newActivity: ActivityRequired) => API.createActivity(newActivity), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(ACTIVITIES_QUERY_KEY);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, data.id], data);
    },
  });
};

/**
 * Update an activity
 * @param id - Id of activity
 */
export const useUpdateActivity = (id: string): UseMutationResult<Activity, RequestResponse, ActivityRequired, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((updatedActivity: ActivityRequired) => API.updateActivity(id, updatedActivity), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(ACTIVITIES_QUERY_KEY);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, id], data);
    },
  });
};

/**
 * Delete an activity
 * @param id - Id of activity
 */
export const useDeleteActivity = (id: string): UseMutationResult<RequestResponse, RequestResponse, unknown, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(() => API.deleteActivity(id), {
    onSuccess: () => {
      queryClient.invalidateQueries(ACTIVITIES_QUERY_KEY);
    },
  });
};

//////////////////////////////////
///////// Activity hosts /////////
//////////////////////////////////

/**
 * Get all host on an activity
 * @param activityId - Id of activity
 */
export const useActivityHostsById = (activityId: string) => {
  return useQuery<Array<UserList>, RequestResponse>([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], () => API.getActivityHosts(activityId));
};

/**
 * Add host to an activity
 * @param activityId - Id of activity
 */
export const useAddActivityHost = (activityId: string): UseMutationResult<Array<UserList>, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((email) => API.addActivityHost(activityId, email), {
    onSuccess: (data) => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY]);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], data);
    },
  });
};

/**
 * Remove host from an activity
 * @param activityId - Id of activity
 */
export const useRemoveActivityHost = (activityId: string): UseMutationResult<Array<UserList>, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((hostId) => API.removeActivityHost(activityId, hostId), {
    onSuccess: (data) => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY]);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], data);
    },
  });
};

//////////////////////////////////
///// Activity registrations /////
//////////////////////////////////

/**
 * Get all registrations in an activity, paginated
 * @param activityId - Id of activity
 */
export const useActivityRegistrations = (activityId: string) => {
  return useInfiniteQuery<PaginationResponse<Registration>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, activityId, ACTIVITIES_QUERY_KEY_REGISTRATION],
    ({ pageParam = 0 }) => API.getRegistrations(activityId, { page: pageParam }),
  );
};

/**
 * Get a user's registration at an activity if it exists
 * @param activityId - Id of activity
 * @param userId - Id of user
 */
export const useActivityRegistration = (activityId: string, userId: string) => {
  return useQuery<Registration, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, activityId, ACTIVITIES_QUERY_KEY_REGISTRATION, userId],
    () => API.getRegistration(activityId, userId),
    {
      enabled: userId !== '',
      retry: false,
    },
  );
};

/**
 * Create a registration in an activity
 * @param activityId - Id of activity
 */
export const useCreateActivityRegistration = (activityId: string): UseMutationResult<Registration, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((userId) => API.createRegistration(activityId, userId), {
    onSuccess: (data) => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, ACTIVITIES_QUERY_KEY_REGISTRATION, data.user.id], data);
    },
  });
};

/**
 * Delete a registration in an activity
 * @param activityId - Id of activity
 */
export const useDeleteActivityRegistration = (activityId: string): UseMutationResult<RequestResponse, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((userId: string) => API.deleteRegistration(activityId, userId), {
    onSuccess: () => {
      queryClient.removeQueries([ACTIVITIES_QUERY_KEY, activityId]);
    },
  });
};

//////////////////////////////////
//////// Activity invites ////////
//////////////////////////////////

/**
 * Get activities where the signed in user is participating, paginated
 * @param activityId - Id of activity
 * @param filters - Filtering
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useActivityInvitedUsers = (activityId: string, filters?: any) => {
  return useInfiniteQuery<PaginationResponse<UserList>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, activityId, INVITED_USERS_QUERY_KEY, filters],
    ({ pageParam = 0 }) => API.getActivityInvitedUsers(activityId, { ...filters, page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

/**
 * Invite a user to join a private activity
 * @param activityId - Id of activity
 */
export const useAddActivityInvitedUser = (activityId: string): UseMutationResult<PaginationResponse<UserList>, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((email) => API.addActivityInvitedUser(activityId, email), {
    onSuccess: () => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, INVITED_USERS_QUERY_KEY]);
    },
  });
};

/**
 * Remove an invited user from an activity
 * @param activityId - Id of activity
 */
export const useRemoveActivityInvitedUser = (activityId: string): UseMutationResult<PaginationResponse<UserList>, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((userId) => API.removeActivityInvitedUser(activityId, userId), {
    onSuccess: () => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY]);
    },
  });
};
