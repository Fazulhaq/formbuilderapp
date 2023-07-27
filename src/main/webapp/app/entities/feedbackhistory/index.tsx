import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Feedbackhistory from './feedbackhistory';
import FeedbackhistoryDetail from './feedbackhistory-detail';
import FeedbackhistoryUpdate from './feedbackhistory-update';
import FeedbackhistoryDeleteDialog from './feedbackhistory-delete-dialog';

const FeedbackhistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Feedbackhistory />} />
    <Route path="new" element={<FeedbackhistoryUpdate />} />
    <Route path=":id">
      <Route index element={<FeedbackhistoryDetail />} />
      <Route path="edit" element={<FeedbackhistoryUpdate />} />
      <Route path="delete" element={<FeedbackhistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeedbackhistoryRoutes;
