import React, { useState } from "react";
import { connect } from "react-redux";
import { isValidPrivateKey } from "../utils/crypto";
import Input from "./Input";
import Button from "./Button";
import styled from "styled-components";

import * as authActions from "../redux/actions/auth";
import useInput from "../hooks/useInput";
import userApi from "../api/user";

const Container = styled.div`
  display: flex;
  flex-direction: column;
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

const LoginInput = styled(Input)`
  margin-bottom: 10px;
`;

const PwdInput = styled(Input)`
  margin-bottom: 30px;
`;

const LoginButton = styled(Button)`
  margin-top: 10px;
  color: white;
  background-color: #17202e;
`;

const LoginForm = ({ login }) => {
  const [warningMessage, setWarningMessage] = useState("");

  const privateKey = useInput("");
  const emailInput = useInput("");
  const passwordInput = useInput("");

  const handleLogin = () => {
    const privateKeyValue = privateKey.value;
    const email = emailInput.value;
    const password = passwordInput.value;

    // TODO: log in --DEPRECATED
    userApi.login({ email, password });

    // 클레이튼 로그인
    if (email && password) {
      isValidPrivateKey(privateKeyValue)
        ? login(privateKeyValue)
        : setWarningMessage("* Invalid Private Key");
    } else {
      alert("아이디와 패스워드를 입력해주세요");
    }
  };

  return (
    <Container>
      <LogoImage />
      <LoginInput
        type="text"
        name="useremail"
        label="Log in"
        placeholder="id"
        onChange={emailInput.onChange}
        err={warningMessage}
      />
      <PwdInput
        type="password"
        name="password"
        placeholder="password"
        onChange={passwordInput.onChange}
        err={warningMessage}
      />
      <LoginInput
        type="password"
        name="privateKey"
        label="Private Key"
        placeholder="0x2c4078447..."
        onChange={privateKey.onChange}
        err={warningMessage}
      />
      <LoginButton onClick={handleLogin}>Log in</LoginButton>
    </Container>
  );
};

const mapDispatchToProps = dispatch => ({
  login: privateKey => dispatch(authActions.login(privateKey))
});

export default connect(null, mapDispatchToProps)(LoginForm);
