import React from "react";
import cx from "classnames";
import styled from "styled-components";

const TextareaContainer = styled.div``;

const Label = styled.label`
  display: block;
  font-size: 12px;
  font-weight: bold;
  color: $brown-grey;
  margin-bottom: 8px;
`;

const STextarea = styled.textarea`
  width: 100%;
  height: 150px;
  font-size: 14px;
  border: 1px solid $light-grey;
  border-radius: 5px;
  padding: 22px 24px;

  &::placeholder {
    color: $middle-grey;
  }
  &--err {
    border-color: $alert-red;
  }
`;

const Err = styled.p`
  position: absolute;
  top: 0;
  right: 0;
  font-size: 12px;
  color: $alert-red;
`;

const Textarea = ({
  className,
  name,
  label,
  value,
  onChange,
  placeholder,
  err
}) => (
  <TextareaContainer>
    {label && <Label htmlFor={name}>{label}</Label>}
    <STextarea
      id={name}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={cx("Textarea__textarea", { "Textarea__textarea--err": err })}
      autoComplete="off"
    />
    {err && <Err>{err}</Err>}
  </TextareaContainer>
);

export default Textarea;
