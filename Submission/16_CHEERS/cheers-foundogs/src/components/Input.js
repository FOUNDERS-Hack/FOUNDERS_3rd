import React from "react";
import PropTypes from "prop-types";
import styled from "styled-components";

const Container = styled.input`
  border: 0;
  border: ${props => props.theme.boxBorder};
  border-radius: ${props => props.theme.borderRadius};
  background-color: ${props => props.theme.bgColor};
  height: 50px;
  font-size: 14px;
  padding: 0px 15px;
  width: 100%;
  margin-bottom: 15px;
`;

const Label = styled.label`
  display: block;
  font-size: 12px;
  font-weight: bold;
  color: ${props => props.theme.black};
  margin-bottom: 8px;
`;

const Err = styled.p`
  position: absolute;
  top: 0;
  right: 0;
  font-size: 12px;
  color: red;
`;

const Input = ({
  placeholder,
  required = true,
  value,
  onChange,
  type = "text",
  err,
  readOnly,
  label,
  name,
  className
}) => (
  <div>
    {label && <Label htmlFor={name}>{label}</Label>}
    <Container
      id={name}
      placeholder={placeholder}
      required={required}
      value={value}
      onChange={onChange}
      type={type}
      autoComplete="off"
      readOnly={readOnly}
      className={className}
    />
    {err && <Err>{err}</Err>}
  </div>
);

Input.propTypes = {
  placeholder: PropTypes.string.isRequired,
  required: PropTypes.bool,
  value: PropTypes.string,
  onChange: PropTypes.func,
  type: PropTypes.string
};

export default Input;
