import trip from 'app/entities/trip/trip.reducer';
import item from 'app/entities/item/item.reducer';
import vendor from 'app/entities/vendor/vendor.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  trip,
  item,
  vendor,
  userProfile,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
