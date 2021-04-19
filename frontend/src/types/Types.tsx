import { TrainingLevel } from 'types/Enums';

export type RequestResponse = {
  detail: string;
};

export type LoginRequestResponse = {
  token: string;
  refreshToken: string;
};

export type RefreshTokenResponse = {
  token: string;
};

export type PaginationResponse<T> = {
  count: number;
  next: number | null;
  previous: number | null;
  results: Array<T>;
};

export type User = {
  userId: string;
  firstName: string;
  surname: string;
  email: string;
  birthDate: string | null;
  traninglevel: TrainingLevel;
};
export type UserCreate = Pick<User, 'email' | 'firstName' | 'surname'> & {
  password: string;
};

export type ActivityRequired = Partial<Activity> & Pick<Activity, 'title' | 'startDate' | 'endDate' | 'signupStart' | 'signupEnd'>;

export type Activity = {
  activityId: string;
  createdDate: string;
  title: string;
  description: string;
  closed: boolean;
  capacity: number;
  location: string;
  startDate: string;
  endDate: string;
  signupStart: string;
  signupEnd: string;
  hosts: Array<string>;
  image: string;
};

export type ActivityHost = Pick<User, 'firstName' | 'surname' | 'email' | 'userId'>;

export type Registration = Pick<User, 'firstName' | 'surname' | 'email' | 'userId'> & {
  registrationId: string;
  createdDate: string;
};
