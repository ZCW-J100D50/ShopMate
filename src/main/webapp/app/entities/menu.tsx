import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/trip">
        Trip
      </MenuItem>
      <MenuItem icon="asterisk" to="/item">
        Item
      </MenuItem>
      <MenuItem icon="asterisk" to="/vendor">
        Vendor
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-profile">
        User Profile
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
