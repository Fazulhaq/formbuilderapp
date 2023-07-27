import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './empty-form.reducer';

export const EmptyFormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const emptyFormEntity = useAppSelector(state => state.emptyForm.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="emptyFormDetailsHeading">
          <Translate contentKey="formbuilderappApp.emptyForm.detail.title">EmptyForm</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{emptyFormEntity.id}</dd>
          <dt>
            <span id="jSONText">
              <Translate contentKey="formbuilderappApp.emptyForm.jSONText">J SON Text</Translate>
            </span>
          </dt>
          <dd>{emptyFormEntity.jSONText}</dd>
        </dl>
        <Button tag={Link} to="/empty-form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/empty-form/${emptyFormEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmptyFormDetail;
