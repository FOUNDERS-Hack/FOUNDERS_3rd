import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn, Container } from "semantic-ui-react";
import Link from "next/link";
const profileImg = require("../../assets/me.png");

const StyledButton = styled(Button)`
  background-color: #2e5bff !important;
  opacity: ${props => props.opacity};
  border-radius: 4px;
  font-size: 14px;
  color: #ffffff !important;
  width: 120px !important;
  font-weight: 500 !important;
`;

const NoPaddingColumn = styled(Grid.Column)`
  padding-left: 0 !important;
  padding-right: 0 !important;
`;
const NoLeftPaddingColumn = styled(Grid.Column)`
  padding-left: 0 !important;
`;

const MainText = styled.span`
  font-size: 17px;
  font-weight: 500;
  color: #2e384d;
`;

const SubText = styled.span`
  font-size: 10px;
  color: #8798ad;
  float: left;
`;

const StyledRowHalfBottomPadding = styled(Grid.Row)`
  padding-bottom: 0.5em !important;
`;

const StyledRowHalfTopPadding = styled(Grid.Row)`
  padding-top: 0.5em !important;
`;

const StyledGrid = styled(Grid)`
  width: 100%;
  border-radius: 1px;
  border-bottom: solid 1px rgba(46, 91, 255, 0.08);
  background-color: #ffffff;
  padding-top: 64px !important;
`;

const UserHeader = () => (
  <StyledGrid>
    <Grid columns="equal" style={{ paddingLeft: "none !important", paddingRight: "none !important" }}>
      <Grid.Row>
        <Grid.Column width={4}>
          <Grid>
            <Grid.Row>
              <Grid.Column>
                <Image circular size="tiny" src={profileImg} />
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Grid.Column>
        <Grid.Column>
          <Grid columns="equal">
            <StyledRowHalfBottomPadding>
              <NoPaddingColumn width={3}>
                <MainText>이다연</MainText>
              </NoPaddingColumn>
              <NoPaddingColumn>
                <SubText>Diana Lee</SubText>
              </NoPaddingColumn>
            </StyledRowHalfBottomPadding>
          </Grid>
          <Grid columns="equal">
            <StyledRowHalfTopPadding>
              <Link href="/RegisterPage">
                <NoLeftPaddingColumn>
                  <StyledButton size="tiny" opacity="0.5">
                    이용안내
                  </StyledButton>
                </NoLeftPaddingColumn>
              </Link>
              <Link href="/VisaFlightPage">
                <NoLeftPaddingColumn>
                  <StyledButton size="tiny">마이페이지</StyledButton>
                </NoLeftPaddingColumn>
              </Link>
            </StyledRowHalfTopPadding>
          </Grid>
        </Grid.Column>
      </Grid.Row>
    </Grid>
  </StyledGrid>
);

export default UserHeader;
