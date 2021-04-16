import { TrainingLevel } from 'types/Enums';

export type RequestResponse = {
  detail: string;
};

export type LoginRequestResponse = {
  access_token: string;
  refresh_token: string;
};

export type PaginationResponse<T> = {
  count: number;
  next: number | null;
  previous: number | null;
  results: Array<T>;
};

export type User = {
  user_id: string;
  first_name: string;
  surname: string;
  email: string;
  birth_date: string | null;
  traninglevel: TrainingLevel;
};
export type UserCreate = Pick<User, 'email' | 'first_name' | 'surname'> & {
  password: string;
};

export type ActivityRequired = Partial<Activity> & Pick<Activity, 'title' | 'start_date' | 'end_date' | 'signup_start' | 'signup_end'>;

export type Activity = {
  activity_id: string;
  created_date: string;
  title: string;
  description: string;
  closed: boolean;
  capacity: number;
  location: string;
  start_date: string;
  end_date: string;
  signup_start: string;
  signup_end: string;
  hosts: Array<string>;
  image: string;
};
