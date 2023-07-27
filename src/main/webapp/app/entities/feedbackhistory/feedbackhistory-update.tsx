import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFilledForm } from 'app/shared/model/filled-form.model';
import { getEntities as getFilledForms } from 'app/entities/filled-form/filled-form.reducer';
import { IFeedbackhistory } from 'app/shared/model/feedbackhistory.model';
import { getEntity, updateEntity, createEntity, reset } from './feedbackhistory.reducer';

export const FeedbackhistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const filledForms = useAppSelector(state => state.filledForm.entities);
  const feedbackhistoryEntity = useAppSelector(state => state.feedbackhistory.entity);
  const loading = useAppSelector(state => state.feedbackhistory.loading);
  const updating = useAppSelector(state => state.feedbackhistory.updating);
  const updateSuccess = useAppSelector(state => state.feedbackhistory.updateSuccess);

  const handleClose = () => {
    navigate('/feedbackhistory' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFilledForms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...feedbackhistoryEntity,
      ...values,
      filledForm: filledForms.find(it => it.id.toString() === values.filledForm.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...feedbackhistoryEntity,
          filledForm: feedbackhistoryEntity?.filledForm?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="formbuilderappApp.feedbackhistory.home.createOrEditLabel" data-cy="FeedbackhistoryCreateUpdateHeading">
            <Translate contentKey="formbuilderappApp.feedbackhistory.home.createOrEditLabel">Create or edit a Feedbackhistory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="feedbackhistory-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('formbuilderappApp.feedbackhistory.feedbackText')}
                id="feedbackhistory-feedbackText"
                name="feedbackText"
                data-cy="feedbackText"
                type="text"
              />
              <ValidatedField
                label={translate('formbuilderappApp.feedbackhistory.feedbackDate')}
                id="feedbackhistory-feedbackDate"
                name="feedbackDate"
                data-cy="feedbackDate"
                type="date"
              />
              <ValidatedField
                id="feedbackhistory-filledForm"
                name="filledForm"
                data-cy="filledForm"
                label={translate('formbuilderappApp.feedbackhistory.filledForm')}
                type="select"
              >
                <option value="" key="0" />
                {filledForms
                  ? filledForms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedbackhistory" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FeedbackhistoryUpdate;
