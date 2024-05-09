import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrip } from 'app/shared/model/trip.model';
import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { IItem } from 'app/shared/model/item.model';
import { Category } from 'app/shared/model/enumerations/category.model';
import { getEntity, updateEntity, createEntity, reset } from './item.reducer';

export const ItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const itemEntity = useAppSelector(state => state.item.entity);
  const loading = useAppSelector(state => state.item.loading);
  const updating = useAppSelector(state => state.item.updating);
  const updateSuccess = useAppSelector(state => state.item.updateSuccess);
  const categoryValues = Object.keys(Category);

  const handleClose = () => {
    navigate('/item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrips({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }

    const entity = {
      ...itemEntity,
      ...values,
      trips: trips.find(it => it.id.toString() === values.trips?.toString()),
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
          category: 'FOOD',
          ...itemEntity,
          trips: itemEntity?.trips?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shopmateApp.item.home.createOrEditLabel" data-cy="ItemCreateUpdateHeading">
            Create or edit a Item
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="item-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="item-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Category" id="item-category" name="category" data-cy="category" type="select">
                {categoryValues.map(category => (
                  <option value={category} key={category}>
                    {category}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Price" id="item-price" name="price" data-cy="price" type="text" />
              <ValidatedField label="Quantity" id="item-quantity" name="quantity" data-cy="quantity" type="text" />
              <ValidatedField id="item-trips" name="trips" data-cy="trips" label="Trips" type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/item" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ItemUpdate;
