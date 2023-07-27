import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/feedbackhistory">
        <Translate contentKey="global.menu.entities.feedbackhistory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/empty-form">
        <Translate contentKey="global.menu.entities.emptyForm" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/filled-form">
        <Translate contentKey="global.menu.entities.filledForm" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
