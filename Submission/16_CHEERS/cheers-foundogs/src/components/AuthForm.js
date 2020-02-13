import React, { useState } from "react";
import styled from "styled-components";
import LoginForm from "./LoginForm";
import SignupForm from "./SignupForm";

const Container = styled.div`
  width: 100%;
  max-width: ${props => props.theme.maxCardWidth};
  padding: 36px 60px 83px;
  ${props => props.theme.whiteBox}
`;

const Message = styled.p`
  color: ${props => props.theme.brownGrey};
  font-size: 12px;
  margin-top: 50px;
`;

const LinkSpan = styled.span`
  font-weight: bold;
  cursor: pointer;
  &:hover {
    text-decoration: underline;
  }
`;

export default () => {
  const [loginForm, setLoginForm] = useState(true);
  const toggleForm = () => {
    if (loginForm === true) {
      setLoginForm(false);
    } else {
      setLoginForm(true);
    }
  };

  return (
    <Container>
      {loginForm ? <LoginForm /> : <SignupForm />}
      <Message>
        {loginForm ? "Don't have an account? " : "Have an account? "}
        <LinkSpan to="/" onClick={toggleForm}>
          {loginForm ? "Sign up" : "Login"}
        </LinkSpan>
      </Message>
    </Container>
  );
};
