import dayjs from 'dayjs';
import { IVendor } from 'app/shared/model/vendor.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ITrip {
  id?: number;
  name?: string | null;
  budget?: number | null;
  date?: dayjs.Dayjs | null;
  vendor?: IVendor | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<ITrip> = {};
