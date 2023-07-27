import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FilledForm from './filled-form';
import FilledFormDetail from './filled-form-detail';
import FilledFormUpdate from './filled-form-update';
import FilledFormDeleteDialog from './filled-form-delete-dialog';

const FilledFormRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FilledForm />} />
    <Route path="new" element={<FilledFormUpdate />} />
    <Route path=":id">
      <Route index element={<FilledFormDetail />} />
      <Route path="edit" element={<FilledFormUpdate />} />
      <Route path="delete" element={<FilledFormDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FilledFormRoutes;
