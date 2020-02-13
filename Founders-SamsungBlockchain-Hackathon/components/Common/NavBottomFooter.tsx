import React, { Component } from "react";
import styled from "styled-components";
import { Grid, Menu, Icon, Image, Button, Divider, Segment, GridColumn, Container } from "semantic-ui-react";
import Link from "next/link";

const homeImg = require("../../assets/home.svg");
const settingImg = require("../../assets/setting.svg");
const communityImg = require("../../assets/community.svg");
const placeImg = require("../../assets/place.svg");

// const StyledImage = styled(Image)`
//   width: 24px !important;
//   height: 24px !important;
// `

const MainGrid = styled(Grid)`
  /* width: 100%;
  background-color: #ffffff; */
  display: flex;
`;

interface INavBottomFooter {
  title?: string;
  isClicked?: boolean;
}

const StyledMenu = styled(Menu)`
  display: flex;
  justify-content: center;
  width: 100%;
  -webkit-backdrop-filter: blur(10px);
  backdrop-filter: blur(10px);
  border: none !important;
  border-radius: 0 !important;
  border-top: solid 1px #dde6f2 !important;
  /* padding-left: 0 !important; */
  /* padding-right: 0 !important; */
`;

const StyledImage = styled(Image)`
  background-color: #606060;
`;

const StyledText = styled.div<INavBottomFooter>`
  height: 16px;
  font-family: "SFProDisplay";
  font-size: 10px;
  font-weight: normal;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.6;
  letter-spacing: normal;
  text-align: center;
  color: ${props => (props.isClicked ? "#1c38d7" : "#686868")};
`;
const StyledMenuItem = styled(Menu.Item)`
  &::before {
    /* border: 0px !important; */
    width: 0px !important;
    /* border: 10px solid red !important; */
    /* background-color: red !important; */
  }
`;

export default class NavBottomFooter extends React.Component {
  state = {};

  handleItemClick = name => this.setState({ activeItem: name });

  render() {
    const activeItem = this.state;

    return (
      <MainGrid>
        <StyledMenu icon="labeled">
          <Link href="/index">
            <StyledMenuItem name="home" active={activeItem === "home"} onClick={this.handleItemClick}>
              <StyledImage src={homeImg} />
              <StyledText isClicked>홈</StyledText>
            </StyledMenuItem>
          </Link>

          {/* <StyledMenuItem
            name='place'
            active={activeItem === 'place'}
            onClick={this.handleItemClick}
          >
            <StyledImage src={placeImg} />
            <StyledText>펫플레이스</StyledText>
          </StyledMenuItem> */}

          {/* <StyledMenuItem
            name='community'
            active={activeItem === 'community'}
            onClick={this.handleItemClick}
          >
            <StyledImage src={communityImg} />
            <StyledText>커뮤니티</StyledText>
          </StyledMenuItem> */}

          <Link href="/VisaCompletePage">
            <StyledMenuItem name="setting" active={activeItem === "setting"} onClick={this.handleItemClick}>
              <StyledImage src={settingImg} />
              <StyledText>마이페이지</StyledText>
            </StyledMenuItem>
          </Link>
        </StyledMenu>
      </MainGrid>
    );
  }
}
