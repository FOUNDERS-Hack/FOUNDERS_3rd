import React from "react";
import Avatar from "@material-ui/core/Avatar";
import AvatarGroup from "@material-ui/lab/AvatarGroup";
import Tooltip from "@material-ui/core/Tooltip";

export default () => {
  return (
    <AvatarGroup>
      <Avatar
        alt="Remy Sharp"
        src="https://material-ui.com/static/images/avatar/1.jpg"
      />
      <Avatar
        alt="Travis Howard"
        src="https://material-ui.com/static/images/avatar/2.jpg"
      />
      <Avatar
        alt="Cindy Baker"
        src="https://material-ui.com/static/images/avatar/3.jpg"
      />
      <Tooltip title="Foo • Bar • Baz">
        <Avatar>+3</Avatar>
      </Tooltip>
    </AvatarGroup>
  );
};
