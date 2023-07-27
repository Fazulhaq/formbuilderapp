import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './filled-form.reducer';

export const FilledFormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const filledFormEntity = useAppSelector(state => state.filledForm.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="filledFormDetailsHeading">
          <Translate contentKey="formbuilderappApp.filledForm.detail.title">FilledForm</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{filledFormEntity.id}</dd>
          <dt>
            <span id="jSONText">
              <Translate contentKey="formbuilderappApp.filledForm.jSONText">J SON Text</Translate>
            </span>
          </dt>
          <dd>{filledFormEntity.jSONText}</dd>
        </dl>
        <Button tag={Link} to="/filled-form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/filled-form/${filledFormEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FilledFormDetail;
