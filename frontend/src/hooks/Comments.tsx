import { useInfiniteQuery, useQueryClient, useMutation, UseMutationResult } from 'react-query';
import API from 'api/api';
import { getNextPaginationPage } from 'utils';
import { PaginationResponse, RequestResponse, Comment } from 'types/Types';
export const COMMENTS_QUERY_KEY = 'comments';

/**
 * Get all comments, paginated
 * @param filters - Filtering
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useComments = (activityId: string, filters?: any) => {
  return useInfiniteQuery<PaginationResponse<Comment>, RequestResponse>(
    [COMMENTS_QUERY_KEY, filters],
    ({ pageParam = 0 }) => API.getComments(activityId, { ...filters, page: pageParam }),
    {
      getNextPageParam: getNextPaginationPage,
    },
  );
};

/**
 * Create a new comment
 */
export const useCreateComment = (activityId: string): UseMutationResult<Comment, RequestResponse, Pick<Comment, 'comment'>, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((comment) => API.createComment(activityId, comment), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(COMMENTS_QUERY_KEY);
      queryClient.setQueryData([COMMENTS_QUERY_KEY, data.id], data);
    },
  });
};

/**
 * Delete a comment
 * @param id - Id of a comment
 */
export const useDeleteComment = (activityId: string, id: string): UseMutationResult<RequestResponse, RequestResponse, unknown, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(() => API.deleteComment(activityId, id), {
    onSuccess: () => {
      queryClient.invalidateQueries(COMMENTS_QUERY_KEY);
      queryClient.removeQueries([COMMENTS_QUERY_KEY, id]);
    },
  });
};

/**
 * Edit a comment
 * @param id - Id of comment
 */
export const useEditComment = (activityId: string, id: string): UseMutationResult<Comment, RequestResponse, Pick<Comment, 'comment'>, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((comment) => API.editComment(activityId, id, comment), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(COMMENTS_QUERY_KEY);
      queryClient.setQueryData([COMMENTS_QUERY_KEY, id], data);
    },
  });
};
