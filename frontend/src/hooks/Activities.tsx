import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import { Activity, ActivityRequired, PaginationResponse, RequestResponse } from 'types/Types';

export const EXPORT_QUERY_KEY = 'activities';

export const useActivityById = (id: string) => {
  return useQuery<Activity, RequestResponse>([EXPORT_QUERY_KEY, id], () => API.getActivity(id), { enabled: id !== '' });
};

export const useActivities = () => {
  return useInfiniteQuery<PaginationResponse<Activity>, RequestResponse>([EXPORT_QUERY_KEY], ({ pageParam = 1 }) => API.getActivities({ page: pageParam }), {
    getNextPageParam: (lastPage) => lastPage.next,
  });
};

export const useMyParticipatingActivities = () => {
  return useInfiniteQuery<PaginationResponse<Activity>, RequestResponse>(
    [EXPORT_QUERY_KEY],
    ({ pageParam = 1 }) => API.getMyParticipatingActivities({ page: pageParam }),
    {
      getNextPageParam: (lastPage) => lastPage.next,
    },
  );
};

export const useMyHostActivities = () => {
  return useInfiniteQuery<PaginationResponse<Activity>, RequestResponse>(
    [EXPORT_QUERY_KEY],
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
      queryClient.invalidateQueries(EXPORT_QUERY_KEY);
      queryClient.setQueryData([EXPORT_QUERY_KEY, data.activityId], data);
    },
  });
};

export const useUpdateActivity = (id: string): UseMutationResult<Activity, RequestResponse, ActivityRequired, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((updatedActivity: ActivityRequired) => API.updateActivity(id, updatedActivity), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(EXPORT_QUERY_KEY);
      queryClient.setQueryData([EXPORT_QUERY_KEY, id], data);
    },
  });
};

export const useDeleteActivity = (id: string): UseMutationResult<RequestResponse, RequestResponse, unknown, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(() => API.deleteActivity(id), {
    onSuccess: () => {
      queryClient.invalidateQueries(EXPORT_QUERY_KEY);
    },
  });
};
