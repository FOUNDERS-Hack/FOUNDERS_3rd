import React from "react";
import AuthForm from "../components/AuthForm";
import styled from "styled-components";
import Modal from "../components/Modal";

const Container = styled.div`
  position: relative;
  width: 100%;
  min-height: 100%;
  min-width: ${props => props.theme.minPageWidth};
  max-width: ${props => props.theme.maxPageWidth};
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const AuthPage = () => (
  <Container>
    <Modal />
    <AuthForm />
  </Container>
);

export default AuthPage;
