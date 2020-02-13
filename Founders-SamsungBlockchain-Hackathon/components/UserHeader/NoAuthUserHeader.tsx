import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn, Container } from "semantic-ui-react";
import Link from "next/link";

const defaultUserImage = require("../../assets/defaultFace.png");

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
  font-size: 13px;
  font-weight: 500;
  color: #2e384d;
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

const NonAuthUserHeader = () => (
  <StyledGrid>
    <Grid columns="equal">
      <Grid.Row>
        <Grid.Column width={4}>
          <Grid>
            <Grid.Row>
              <Grid.Column>
                <Image circular size="tiny" src={defaultUserImage} />
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Grid.Column>
        <Grid.Column>
          <Grid columns="equal">
            <StyledRowHalfBottomPadding>
              <NoPaddingColumn>
                <SubText>안녕하세요 000님</SubText>
              </NoPaddingColumn>
            </StyledRowHalfBottomPadding>
          </Grid>
          <Grid columns="equal">
            <StyledRowHalfTopPadding>
              <NoLeftPaddingColumn>
                <StyledButton size="tiny" opacity="0.5">
                  이용방법
                </StyledButton>
              </NoLeftPaddingColumn>
              <Link href="/VerificationPage">
                <NoLeftPaddingColumn>
                  <StyledButton size="tiny">신분인증</StyledButton>
                </NoLeftPaddingColumn>
              </Link>
            </StyledRowHalfTopPadding>
          </Grid>
        </Grid.Column>
      </Grid.Row>
    </Grid>
  </StyledGrid>
);

export default NonAuthUserHeader;
