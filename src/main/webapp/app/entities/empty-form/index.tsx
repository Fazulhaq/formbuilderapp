import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmptyForm from './empty-form';
import EmptyFormDetail from './empty-form-detail';
import EmptyFormUpdate from './empty-form-update';
import EmptyFormDeleteDialog from './empty-form-delete-dialog';

const EmptyFormRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmptyForm />} />
    <Route path="new" element={<EmptyFormUpdate />} />
    <Route path=":id">
      <Route index element={<EmptyFormDetail />} />
      <Route path="edit" element={<EmptyFormUpdate />} />
      <Route path="delete" element={<EmptyFormDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmptyFormRoutes;
