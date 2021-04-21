import { useMutation, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import URLS from 'URLS';
import { User, UserCreate, LoginRequestResponse, RequestResponse, RefreshTokenResponse } from 'types/Types';
import { getCookie, setCookie, removeCookie } from 'api/cookie';
import { ACCESS_TOKEN, REFRESH_TOKEN, ACCESS_TOKEN_DURATION, REFRESH_TOKEN_DURATION } from 'constant';

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
      setCookie(ACCESS_TOKEN, data.token, ACCESS_TOKEN_DURATION);
      setCookie(REFRESH_TOKEN, data.refreshToken, REFRESH_TOKEN_DURATION);
      queryClient.removeQueries(USER_QUERY_KEY);
      queryClient.prefetchQuery(USER_QUERY_KEY, () => API.getUser());
    },
  });
};

export const useRefreshToken = (): UseMutationResult<RefreshTokenResponse, RequestResponse, unknown, unknown> => {
  return useMutation(() => API.refreshAccessToken());
};

export const useForgotPassword = (): UseMutationResult<RequestResponse, RequestResponse, string, unknown> => {
  return useMutation((email) => API.forgotPassword(email));
};

export const useLogout = () => {
  const queryClient = useQueryClient();
  return () => {
    queryClient.removeQueries(USER_QUERY_KEY);
    logout();
  };
};

export const logout = () => {
  removeCookie(ACCESS_TOKEN);
  removeCookie(REFRESH_TOKEN);
  location.href = URLS.LANDING;
};

export const useIsAuthenticated = () => {
  return typeof getCookie(REFRESH_TOKEN) !== 'undefined';
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

export const useChangePassword = (): UseMutationResult<RequestResponse, RequestResponse, { oldPassword: string; newPassword: string }, unknown> => {
  return useMutation(({ oldPassword, newPassword }) => API.changePassword(oldPassword, newPassword));
};
