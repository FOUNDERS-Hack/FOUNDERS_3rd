import React from "react";
// import cx from "classnames";
import styled from "styled-components";

const InputFileContainer = styled.div`
  position: relative;
  font-size: 14px;
  border: 1px solid ${props => props.theme.lightGrey};
  border-radius: 5px;
  margin-top: 60px;
  margin-bottom: 40px;
  height: 50px;
  &--err {
    border-color: ${props => props.theme.alertRed};
  }
`;
const Label = styled.p`
  position: absolute;
  top: -26px;
  font-size: 12px;
  font-weight: bold;
  line-height: 1;
  color: ${props => props.theme.black};
`;

const InputButton = styled.label`
  float: right;
  display: block;
  width: 120px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  font-weight: bold;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  color: white;
  background-color: ${props => props.theme.brownGrey};
  cursor: pointer;
`;

const InputFileInput = styled.input`
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
`;
const FileName = styled.div`
  float: left;
  padding: 16px 18px;
  text-align: left;
  color: ${props => props.theme.brownGrey};
  font-size: 12px;

  &--empty {
    color: ${props => props.theme.brownGrey};
  }
`;
const Err = styled.p`
  position: absolute;
  top: -22px;
  right: 0;
  font-size: 12px;
  color: $alert-red;
`;

const Input = ({
  className,
  name,
  value,
  label,
  fileName,
  onChange,
  required,
  accept,
  err
}) => (
  <InputFileContainer>
    <Label>{label}</Label>
    <InputFileInput
      id="upload"
      type="file"
      name={name}
      value={value}
      onChange={onChange}
      required={required}
      accept={accept}
    />
    <FileName>{fileName || "No photo"}</FileName>
    <InputButton htmlFor="upload">Search</InputButton>
    {err && <Err>{err}</Err>}
  </InputFileContainer>
);

export default Input;
