import { useMutation, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import URLS from 'URLS';
import { User, UserCreate, LoginRequestResponse, RequestResponse } from 'types/Types';
import { getCookie, setCookie, removeCookie } from 'api/cookie';
import { ACCESS_TOKEN, REFRESH_TOKEN } from 'constant';

export const USER_QUERY_KEY = 'user';
export const USERS_QUERY_KEY = 'users';

export const useUser = () => {
  const isAuthenticated = useIsAuthenticated();
  return useQuery<User | undefined, RequestResponse>(USER_QUERY_KEY, () => (isAuthenticated ? API.getUser() : undefined));
};

export const useRefreshUser = () => {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries(USER_QUERY_KEY);
  };
};

export const useLogin = (): UseMutationResult<LoginRequestResponse, RequestResponse, { email: string; password: string }, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(({ email, password }) => API.authenticate(email, password), {
    onSuccess: (data) => {
      setCookie(ACCESS_TOKEN, data.token, 1000 * 60 * 15);
      setCookie(REFRESH_TOKEN, data.refreshToken);
      queryClient.removeQueries(USER_QUERY_KEY);
      queryClient.prefetchQuery(USER_QUERY_KEY, () => API.getUser());
    },
  });
};

export const useForgotPassword = (): UseMutationResult<RequestResponse, RequestResponse, string, unknown> => {
  return useMutation((email) => API.forgotPassword(email));
};

export const useLogout = () => {
  const queryClient = useQueryClient();
  return () => {
    removeCookie(ACCESS_TOKEN);
    removeCookie(REFRESH_TOKEN);
    queryClient.removeQueries(USER_QUERY_KEY);
    location.href = URLS.LANDING;
  };
};

export const useIsAuthenticated = () => {
  return typeof getCookie(ACCESS_TOKEN) !== 'undefined';
};

export const useCreateUser = (): UseMutationResult<RequestResponse, RequestResponse, UserCreate, unknown> => {
  return useMutation((user) => API.createUser(user));
};

export const useUpdateUser = (): UseMutationResult<User, RequestResponse, { userId: string; user: Partial<User> }, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(({ userId, user }) => API.updateUser(userId, user), {
    onSuccess: (data) => {
      queryClient.invalidateQueries(USERS_QUERY_KEY);
      const user = queryClient.getQueryData<User | undefined>(USER_QUERY_KEY);
      if (data.id === user?.id) {
        queryClient.setQueryData(USER_QUERY_KEY, data);
      }
    },
  });
};
