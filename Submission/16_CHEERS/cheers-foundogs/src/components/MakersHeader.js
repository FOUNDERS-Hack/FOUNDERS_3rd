import React from "react";
import styled from "styled-components";
import { Link, withRouter } from "react-router-dom";
import ExitToAppOutlinedIcon from "@material-ui/icons/ExitToAppOutlined";
import AccountBalanceWalletOutlinedIcon from "@material-ui/icons/AccountBalanceWalletOutlined";
import Drawers from "components/Drawers";

const Header = styled.header`
  width: ${props => props.theme.maxCardWidth};
  border: 0;
  position: fixed;
  top: 0;
  background-color: ${props => props.theme.bgColor};
  color: white;
  border-bottom: ${props => props.theme.boxBorder};
  border-radius: 0px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0px 5px;
  z-index: 2;
  height: 60px;
`;

const HeaderWrapper = styled.div`
  width: 100%;
  max-width: ${props => props.theme.maxWidth};
  display: flex;
  justify-content: center;
  align-items: center;
`;

const LogoImage = styled.img.attrs({
  src:
    "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FYyhgg%2FbtqBW0qMhwg%2FEridNoNdjA7EbwTzLdL8cK%2Fimg.png"
})`
  width: 70%;
  margin-left: auto;
  margin-right: auto;
`;

const LeftColumn = styled.span`
  display: flex;
  align-items: center;
  width: 33%;
  text-align: center;
  &:first-child {
    margin-right: auto;
    text-align: left;
  }
  &:last-child {
    margin-left: auto;
    text-align: right;
  }
`;

const MiddleColumn = styled.span`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 33%;
  text-align: center;
`;

const RightColumn = styled.span`
  width: 33%;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
`;

const HeaderLink = styled.div`
  a {
    font-weight: 700;
  }
  a:hover {
    color: ${props => props.theme.brownGrey};
  }
`;

const Divider = styled.span`
  padding: 10px;
`;

const Ground = styled.span`
  a {
    font-weight: 700;
  }
  a:hover {
    color: ${props => props.theme.brownGrey};
  }
`;

const Makers = styled.span`
  a {
    font-size: 22px;
    font-weight: 300;
  }
  a:hover {
    color: ${props => props.theme.brownGrey};
  }
`;

export default withRouter(({ history }) => {
  return (
    <Header>
      <HeaderWrapper>
        <LeftColumn>
          <Ground>
            <Drawers />
          </Ground>
        </LeftColumn>
        <MiddleColumn>
          <Makers>
            <Link to="/makers">
              <LogoImage />
            </Link>
          </Makers>
        </MiddleColumn>
        <RightColumn>
          <HeaderLink>
            <Link to="/wallet">
              <AccountBalanceWalletOutlinedIcon />
            </Link>
          </HeaderLink>
          <Divider></Divider>
          <HeaderLink>
            <Link to="/test">
              <ExitToAppOutlinedIcon />
            </Link>
          </HeaderLink>
        </RightColumn>
      </HeaderWrapper>
    </Header>
  );
});
