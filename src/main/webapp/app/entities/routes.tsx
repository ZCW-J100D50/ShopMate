import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trip from './trip';
import Item from './item';
import Vendor from './vendor';
import UserProfile from './user-profile';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="trip/*" element={<Trip />} />
        <Route path="item/*" element={<Item />} />
        <Route path="vendor/*" element={<Vendor />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
