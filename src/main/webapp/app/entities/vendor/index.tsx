import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vendor from './vendor';
import VendorDetail from './vendor-detail';
import VendorUpdate from './vendor-update';
import VendorDeleteDialog from './vendor-delete-dialog';

const VendorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Vendor />} />
    <Route path="new" element={<VendorUpdate />} />
    <Route path=":id">
      <Route index element={<VendorDetail />} />
      <Route path="edit" element={<VendorUpdate />} />
      <Route path="delete" element={<VendorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VendorRoutes;
