import { ITrip } from 'app/shared/model/trip.model';
import { Category } from 'app/shared/model/enumerations/category.model';

export interface IItem {
  id?: number;
  name?: string | null;
  category?: keyof typeof Category | null;
  price?: number | null;
  quantity?: number | null;
  trips?: ITrip | null;
}

export const defaultValue: Readonly<IItem> = {};
