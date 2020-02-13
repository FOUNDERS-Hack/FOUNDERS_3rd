import React from "react";
import cx from "classnames";
import styled from "styled-components";

const StyledIconButton = styled.button`
  display: inline-block;
  width: 24px;
  height: 24px;
  cursor: pointer;
  img {
    width: 100%;
  }
`;

const IconButton = ({ className, icon, alt, onClick }) => (
  <StyledIconButton className={cx("IconButton", className)} onClick={onClick}>
    <img src={`images/icon-close.png`} alt={alt} />
  </StyledIconButton>
);

export default IconButton;
