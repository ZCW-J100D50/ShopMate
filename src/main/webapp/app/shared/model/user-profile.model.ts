import { IUser } from 'app/shared/model/user.model';

export interface IUserProfile {
  id?: number;
  nickName?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
