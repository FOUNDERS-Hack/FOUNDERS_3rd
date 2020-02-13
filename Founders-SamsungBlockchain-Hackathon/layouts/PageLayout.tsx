// import { NavHeader } from "../components/Common/NavHeader";
import React from "react";
import styled from "styled-components";
import NavTopHeader from "../components/Common/NavTopHeader";
import NavBottomFooter from "../components/Common/NavBottomFooter";
import UserHeader from "../components/UserHeader/UserHeader";

interface IPageLayoutProps {
  children: JSX.Element | JSX.Element[];
  headerTitle?: string;
  isBack?: boolean;
}

const StyledGrid = styled.div`
  display: grid;
  background-color: #f4f6fc;
  justify-items: center;
  /* height: 150vh; */
  /* padding-top: 20px; */
  font-family: "AppleSDGothicNeo";
  font-stretch: normal;
  font-style: normal;
  line-height: normal;
  letter-spacing: normal;

  /* add grid 14px */
  margin-top: 64px;
  margin-bottom: 64px;

  text-align: center;
  display: flex !important;
  align-items: center !important;
  flex-direction: column;
`;

const Container = styled.div`
  display: grid;
  background-color: #f4f6fc;
  justify-items: center;
  /* margin-top: 14px; */
  /* display: flex !important;
  align-items: center !important;
  flex-direction: column; */
`;

const PageLayout = (props: IPageLayoutProps) => (
  <Container>
    <NavTopHeader title={props.headerTitle} isBack={props.isBack} />
    <UserHeader />
    <StyledGrid>{props.children}</StyledGrid>
    {/* <NavBottomFooter /> */}
  </Container>
);

export default PageLayout;
