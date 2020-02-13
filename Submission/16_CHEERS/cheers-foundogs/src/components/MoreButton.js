import React from "react";
import styled from "styled-components";
import Button from "components/Button";

const Container = styled.div`
  position: relative;
  width: 20%;
  min-height: 100%;
  padding: 10px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const StyledButton = styled(Button)`
  background-color: #FAFAFA;
  color: #03A87C;
  font-size: 16px;
`;

export default () => {
  return (
    <Container>
      <StyledButton>▼ 더 보기</StyledButton>
    </Container>
  );
};
