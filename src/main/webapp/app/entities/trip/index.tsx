import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trip from './trip';
import TripDetail from './trip-detail';
import TripUpdate from './trip-update';
import TripDeleteDialog from './trip-delete-dialog';

const TripRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Trip />} />
    <Route path="new" element={<TripUpdate />} />
    <Route path=":id">
      <Route index element={<TripDetail />} />
      <Route path="edit" element={<TripUpdate />} />
      <Route path="delete" element={<TripDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TripRoutes;
