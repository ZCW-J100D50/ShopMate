export interface IVendor {
  id?: number;
  name?: string | null;
  city?: string | null;
}

export const defaultValue: Readonly<IVendor> = {};
