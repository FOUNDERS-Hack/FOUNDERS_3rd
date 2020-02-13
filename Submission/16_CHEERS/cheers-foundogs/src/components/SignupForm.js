import React, { useState } from "react";
import caver from "../klaytn/caver";
import Input from "./Input";
import Button from "./Button";
import styled from "styled-components";
import useInput from "../hooks/useInput";
import userApi from "../api/user";

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
`;

const SignupInput = styled(Input)`
  margin-bottom: 10px;
`;

const PKButton = styled(Button)`
  background-color: ${props => props.theme.darkBrown};
`;

const SignupButton = styled(Button)`
  margin-top: 40px;
  background-color: #17202e;
`;

const LogoImage = styled.img.attrs({
  src:
    "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FYyhgg%2FbtqBW0qMhwg%2FEridNoNdjA7EbwTzLdL8cK%2Fimg.png"
})`
  width: 304px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 60px;
  margin-top: 20px;
`;

export default () => {
  const [privateKey, setPrivateKey] = useState(null);
  const username = useInput("");
  const email = useInput("");
  const password = useInput("");

  const generatePrivateKey = () => {
    const { privateKey } = caver.klay.accounts.create();
    setPrivateKey(privateKey);
  };

  const onSubmit = async e => {
    e.preventDefault();

    if (email.value !== "" && username.value !== "" && password.value !== "") {
      const param = {
        email: email.value,
        name: username.value,
        password: password.value
      };

      if (userApi.signup(param)) {
        // TODO: 회원가입 성공액션
        return true;
      } else {
        // TODO: 회원가입 실패액션
        return false;
      }
    }
  };

  const signup = () => {
    alert("가입되었습니다.");
    window.location.reload();
  };

  return (
    <Wrapper>
      <LogoImage />
      <form onSubmit={onSubmit}>
        <Input
          label="Create new account"
          placeholder={"Username"}
          {...username}
        />
        <Input placeholder={"Password"} {...password} type="password" />
        <Input placeholder={"Email"} {...email} type="email" />
        <br />
        <br />
        <SignupInput
          placeholder="Generate Private Key to Sign up"
          value={privateKey || ""}
          label="Private key"
          readOnly
        />
        <PKButton onClick={generatePrivateKey}>Generate Private Key</PKButton>
        <SignupButton onClick={signup}>Sign Up</SignupButton>
      </form>
    </Wrapper>
  );
};
