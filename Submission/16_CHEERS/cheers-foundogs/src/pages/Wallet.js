import React from "react";
import styled from "styled-components";
import MakersHeader from "components/MakersHeader";
import WalletInfo from "components/WalletInfo";

const Container = styled.main`
  width: 100%;
  min-height: 100%;
  min-width: ${props => props.theme.maxCardWidth};
  max-width: ${props => props.theme.maxCardWidth};
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export default () => (
  <Container>
    <MakersHeader />
    <WalletInfo />
  </Container>
);
