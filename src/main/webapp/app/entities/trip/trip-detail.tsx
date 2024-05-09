import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip.reducer';

export const TripDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tripEntity = useAppSelector(state => state.trip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripDetailsHeading">Trip</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tripEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{tripEntity.name}</dd>
          <dt>
            <span id="budget">Budget</span>
          </dt>
          <dd>{tripEntity.budget}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{tripEntity.date ? <TextFormat value={tripEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Vendor</dt>
          <dd>{tripEntity.vendor ? tripEntity.vendor.id : ''}</dd>
          <dt>User Profile</dt>
          <dd>{tripEntity.userProfile ? tripEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip/${tripEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripDetail;
