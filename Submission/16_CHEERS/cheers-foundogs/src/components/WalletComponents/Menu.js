import React from "react";
import styled from "styled-components";
import ListAltIcon from "@material-ui/icons/ListAlt";
import MoneyIcon from "@material-ui/icons/Money";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import { Link } from "react-router-dom";
import PetsIcon from "@material-ui/icons/Pets";

const Container = styled.div`
  background-color: ${props => props.theme.bgColor};
  width: ${props => props.theme.maxCardWidth};
  height: 380px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const Grid = styled.div`
  width: 90%;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  padding-top: 150px;
`;

const Col = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const IconDescContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 15px;
`;

const IconContainer = styled.div`
  width: 80px;
  height: 80px;
  ${props => props.theme.whiteBox}
  font-size: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
`;

const IconDesc = styled.div`
  cursor: pointer;
`;

export default () => {
  return (
    <Container>
      <Grid>
        <Col>
          <IconContainer>
            <ListAltIcon style={{ fontSize: 40 }} />
          </IconContainer>
          <Link to={`/makers`}>
            <IconDescContainer>
              <IconDesc>지원목록</IconDesc>
            </IconDescContainer>
          </Link>
        </Col>
        <Col>
          <IconContainer>
            <PetsIcon style={{ fontSize: 40 }} />
          </IconContainer>
          <IconDescContainer>
            <IconDesc>펫용품</IconDesc>
          </IconDescContainer>
        </Col>
        <Col>
          <IconContainer>
            <MoneyIcon style={{ fontSize: 40 }} />
          </IconContainer>
          <IconDescContainer>
            <IconDesc>쿠폰</IconDesc>
          </IconDescContainer>
        </Col>
        <Col>
          <IconContainer>
            <FavoriteBorderIcon style={{ fontSize: 40 }} />
          </IconContainer>
          <IconDescContainer>
            <IconDesc>좋아요</IconDesc>
          </IconDescContainer>
        </Col>
      </Grid>
    </Container>
  );
};
