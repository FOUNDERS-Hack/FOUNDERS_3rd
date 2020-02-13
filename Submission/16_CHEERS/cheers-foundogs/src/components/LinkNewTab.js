import React from "react";
import styled from "styled-components";
import cx from "classnames";

const LinkNewTab = ({ className, link, title }) => {
  return (
    <a
      className={cx("Link", className)}
      href={link}
      target="_blank"
      rel="noreferrer noopener"
    >
      {title}
    </a>
  );
};

export default LinkNewTab;
