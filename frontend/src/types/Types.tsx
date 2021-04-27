import { TrainingLevel } from 'types/Enums';

export type RequestResponse = {
  message: string;
};

export type LoginRequestResponse = {
  token: string;
  refreshToken: string;
};

export type RefreshTokenResponse = {
  token: string;
};

export type PaginationResponse<T> = {
  totalElements: number;
  totalPages: number;
  number: number;
  content: Array<T>;
  empty: boolean;
  last: boolean;
};

export type User = {
  id: string;
  firstName: string;
  surname: string;
  email: string;
  birthDate: string | null;
  level: TrainingLevel;
  image: string;
};

export type UserList = Pick<User, 'id' | 'firstName' | 'surname' | 'email' | 'image'>;

export type UserCreate = Pick<User, 'email' | 'firstName' | 'surname'> & {
  password: string;
  matchingPassword: string;
};

export type ActivityRequired = Partial<Activity> & Pick<Activity, 'title' | 'startDate' | 'endDate' | 'signupStart' | 'signupEnd'>;

export type Activity = {
  id: string;
  createdDate: string;
  title: string;
  description: string;
  closed: boolean;
  capacity: number;
  level: TrainingLevel;
  startDate: string;
  endDate: string;
  signupStart: string;
  signupEnd: string;
  hosts: Array<UserList>;
  images: Array<{
    url: string;
  }>;
  creator: UserList;
  geoLocation: LatLng;
  inviteOnly: boolean;
};

export type ActivityList = Pick<Activity, 'id' | 'title' | 'closed' | 'startDate' | 'endDate' | 'level' | 'description' | 'images' | 'geoLocation'>;

export type Registration = {
  user: User;
};

export type FileUploadResponse = {
  data: {
    display_url: string;
  };
};

export type LatLng = {
  lat: number;
  lng: number;
};
