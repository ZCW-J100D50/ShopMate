import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vendor.reducer';

export const VendorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vendorEntity = useAppSelector(state => state.vendor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vendorDetailsHeading">Vendor</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vendorEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{vendorEntity.name}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{vendorEntity.city}</dd>
        </dl>
        <Button tag={Link} to="/vendor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vendor/${vendorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VendorDetail;
