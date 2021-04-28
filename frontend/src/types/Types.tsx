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
};

export type ActivityRequired = Partial<Activity> & Pick<Activity, 'title' | 'startDate' | 'endDate' | 'signupStart' | 'signupEnd'>;

export type Activity = {
  capacity: number;
  closed: boolean;
  createdDate: string;
  creator: UserList;
  description: string;
  endDate: string;
  equipment: Array<{
    name: string;
    amount: number;
  }>;
  geoLocation: LatLng;
  hosts: Array<UserList>;
  id: string;
  inviteOnly: boolean;
  level: TrainingLevel;
  images: Array<{
    url: string;
  }>;
  startDate: string;
  signupStart: string;
  signupEnd: string;
  title: string;
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
