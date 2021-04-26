import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import { Activity, ActivityList, ActivityRequired, ActivityHost, PaginationResponse, Registration, RequestResponse } from 'types/Types';

export const ACTIVITIES_QUERY_KEY = 'activities';
export const ACTIVITIES_QUERY_KEY_REGISTRATION = 'activity_registrations';
export const HOSTS_QUERY_KEY = 'activity_hosts';

export const useActivityById = (id: string) => {
  return useQuery<Activity, RequestResponse>([ACTIVITIES_QUERY_KEY, id], () => API.getActivity(id), { enabled: id !== '' });
};

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

export const useMyHostActivities = () => {
  return useInfiniteQuery<PaginationResponse<ActivityList>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, 'me_host'],
    ({ pageParam = 0 }) => API.getMyHostActivities({ page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

export const useCreateActivity = (): UseMutationResult<Activity, RequestResponse, ActivityRequired, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((newActivity: ActivityRequired) => API.createActivity(newActivity), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(ACTIVITIES_QUERY_KEY);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, data.id], data);
    },
  });
};

export const useUpdateActivity = (id: string): UseMutationResult<Activity, RequestResponse, ActivityRequired, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((updatedActivity: ActivityRequired) => API.updateActivity(id, updatedActivity), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(ACTIVITIES_QUERY_KEY);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, id], data);
    },
  });
};

export const useDeleteActivity = (id: string): UseMutationResult<RequestResponse, RequestResponse, unknown, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(() => API.deleteActivity(id), {
    onSuccess: () => {
      queryClient.invalidateQueries(ACTIVITIES_QUERY_KEY);
    },
  });
};

export const useActivityHostsById = (activityId: string) => {
  return useQuery<Array<ActivityHost>, RequestResponse>([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], () => API.getActivityHosts(activityId));
};

export const useAddActivityHost = (activityId: string): UseMutationResult<Array<ActivityHost>, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((email) => API.addActivityHost(activityId, email), {
    onSuccess: (data) => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY]);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], data);
    },
  });
};

export const useRemoveActivityHost = (activityId: string): UseMutationResult<Array<ActivityHost>, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((hostId) => API.removeActivityHost(activityId, hostId), {
    onSuccess: (data) => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY]);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], data);
    },
  });
};
export const useMyHostActivisties = () => {
  return useInfiniteQuery<PaginationResponse<ActivityList>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, 'me_host'],
    ({ pageParam = 0 }) => API.getMyHostActivities({ page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

export const useActivityRegistrations = (activityId: string) => {
  return useInfiniteQuery<PaginationResponse<Registration>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY, activityId, ACTIVITIES_QUERY_KEY_REGISTRATION],
    ({ pageParam = 0 }) => API.getRegistrations(activityId, { page: pageParam }),
  );
};

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

export const useCreateActivityRegistration = (activityId: string): UseMutationResult<Registration, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((userId) => API.createRegistration(activityId, userId), {
    onSuccess: (data) => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, ACTIVITIES_QUERY_KEY_REGISTRATION, data.user.id], data);
    },
  });
};

export const useDeleteActivityRegistration = (activityId: string): UseMutationResult<RequestResponse, RequestResponse, string, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((userId: string) => API.deleteRegistration(activityId, userId), {
    onSuccess: () => {
      queryClient.removeQueries([ACTIVITIES_QUERY_KEY, activityId]);
    },
  });
};
