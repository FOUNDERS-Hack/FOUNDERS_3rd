import React from "react";
import MakersProduct from "components/MakersProduct";
import styled from "styled-components";
import MakersHeader from "components/MakersHeader";

const Container = styled.main`
  width: 100%;
  min-height: 100%;
  min-width: ${props => props.theme.maxCardWidth};
  max-width: ${props => props.theme.maxCardWidth};
  margin: 0 auto;
`;

export default () => {
  return (
    <Container>
      <MakersHeader />
      <MakersProduct />
    </Container>
  );
};
