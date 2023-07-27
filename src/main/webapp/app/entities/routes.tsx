import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Feedbackhistory from './feedbackhistory';
import EmptyForm from './empty-form';
import FilledForm from './filled-form';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="feedbackhistory/*" element={<Feedbackhistory />} />
        <Route path="empty-form/*" element={<EmptyForm />} />
        <Route path="filled-form/*" element={<FilledForm />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
