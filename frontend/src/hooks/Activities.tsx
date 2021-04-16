import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import { Activity, ActivityRequired, ActivityHost, PaginationResponse, RequestResponse } from 'types/Types';

export const ACTIVITIES_QUERY_KEY = 'activities';
export const HOSTS_QUERY_KEY = 'activities';

export const useActivityById = (id: string) => {
  return useQuery<Activity, RequestResponse>([ACTIVITIES_QUERY_KEY, id], () => API.getActivity(id), { enabled: id !== '' });
};

export const useActivities = () => {
  return useInfiniteQuery<PaginationResponse<Activity>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY],
    ({ pageParam = 1 }) => API.getActivities({ page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

export const useMyParticipatingActivities = () => {
  return useInfiniteQuery<PaginationResponse<Activity>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY],
    ({ pageParam = 1 }) => API.getMyParticipatingActivities({ page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

export const useMyHostActivities = () => {
  return useInfiniteQuery<PaginationResponse<Activity>, RequestResponse>(
    [ACTIVITIES_QUERY_KEY],
    ({ pageParam = 1 }) => API.getMyHostActivities({ page: pageParam }),
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
      queryClient.setQueryData([ACTIVITIES_QUERY_KEY, data.activityId], data);
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
    onSuccess: () => {
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId]);
      queryClient.invalidateQueries([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY]);
      // TODO: uncomment when backend sends a list of new hosts on success
      // queryClient.setQueryData([ACTIVITIES_QUERY_KEY, activityId, HOSTS_QUERY_KEY], data);
    },
  });
};
