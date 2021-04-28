import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import { getNextPaginationPage } from 'utils';
import { Post, PostCreate, PaginationResponse, RequestResponse } from 'types/Types';
export const FEED_QUERY_KEY = 'feed';
export const POST_QUERY_KEY = 'post';

/**
 * Get a specific post
 * @param postId - Id of post
 */
export const usePostById = (postId: string) => {
  return useQuery<Post, RequestResponse>([POST_QUERY_KEY, postId], () => API.getPost(postId), { enabled: postId !== '' });
};

/**
 * Get the feed with posts, paginated
 * @param filters - Filtering
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useFeed = (filters?: any) => {
  return useInfiniteQuery<PaginationResponse<Post>, RequestResponse>(
    [FEED_QUERY_KEY, filters],
    ({ pageParam = 0 }) => API.getFeed({ ...filters, page: pageParam }),
    {
      getNextPageParam: getNextPaginationPage,
    },
  );
};

/**
 * Create a new post
 */
export const useCreatePost = (): UseMutationResult<Post, RequestResponse, PostCreate, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((newPost: PostCreate) => API.createPost(newPost), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(FEED_QUERY_KEY);
      queryClient.setQueryData([POST_QUERY_KEY, data.id], data);
    },
  });
};

/**
 * Update an post
 * @param postId - Id of post
 */
export const useUpdatePost = (postId: string): UseMutationResult<Post, RequestResponse, Partial<Post>, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((updatedPost: Partial<Post>) => API.updatePost(postId, updatedPost), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(FEED_QUERY_KEY);
      queryClient.setQueryData([POST_QUERY_KEY, postId], data);
    },
  });
};

/**
 * Delete a post
 * @param postId - Id of post
 */
export const useDeletePost = (postId: string): UseMutationResult<RequestResponse, RequestResponse, unknown, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(() => API.deletePost(postId), {
    onSuccess: () => {
      queryClient.invalidateQueries(FEED_QUERY_KEY);
    },
  });
};
