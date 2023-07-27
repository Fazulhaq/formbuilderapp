import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feedbackhistory.reducer';

export const FeedbackhistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feedbackhistoryEntity = useAppSelector(state => state.feedbackhistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feedbackhistoryDetailsHeading">
          <Translate contentKey="formbuilderappApp.feedbackhistory.detail.title">Feedbackhistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feedbackhistoryEntity.id}</dd>
          <dt>
            <span id="feedbackText">
              <Translate contentKey="formbuilderappApp.feedbackhistory.feedbackText">Feedback Text</Translate>
            </span>
          </dt>
          <dd>{feedbackhistoryEntity.feedbackText}</dd>
          <dt>
            <span id="feedbackDate">
              <Translate contentKey="formbuilderappApp.feedbackhistory.feedbackDate">Feedback Date</Translate>
            </span>
          </dt>
          <dd>
            {feedbackhistoryEntity.feedbackDate ? (
              <TextFormat value={feedbackhistoryEntity.feedbackDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="formbuilderappApp.feedbackhistory.filledForm">Filled Form</Translate>
          </dt>
          <dd>{feedbackhistoryEntity.filledForm ? feedbackhistoryEntity.filledForm.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/feedbackhistory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feedbackhistory/${feedbackhistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeedbackhistoryDetail;
