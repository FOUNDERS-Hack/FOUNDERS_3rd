import React from 'react'
import styled from 'styled-components';
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn, Container } from 'semantic-ui-react'
import Router from 'next/router';

const menuImg = require('../../assets/menu.svg')
const notiImg = require('../../assets/noti.svg')

const StyledImage = styled(Image)`
  width: 24px !important;
  height: 24px !important;
`

const StyledIcon = styled(Icon)`
  width: 24px !important;
  height: 24px !important;
  color: #2e384d !important;
`

const MainGrid = styled(Grid)`
  width: 100%;
  height: 64px;
  background-color: #ffffff;
  position: fixed;
  z-index: 4;

  align-items: center !important;
  flex-direction: column;

  /* 어긋나는 오류땜에 넣었더니 됌 */
  margin: 0 0 0 0 !important;
`

const NavText = styled.div`
  height: 20px;
  font-size: 17px;
  font-weight: 500;
  color: #2e384d;
  text-align: center;
`

interface INavTopHeader {
  title?: string;
  isBack?: boolean;
}

const NavTopHeader = (props: INavTopHeader) => (
  <MainGrid columns='equal'>
    <Grid.Row>
      <Grid.Column>
        {props.isBack ? <StyledIcon name="arrow left" onClick={() => Router.back()} /> : <StyledImage src={menuImg} />}
        {/* <StyledImage src={menuImg} /> */}
      </Grid.Column>
      <Grid.Column>
        <NavText>{props.title}</NavText>
      </Grid.Column>
      <Grid.Column>
        {props.isBack ? <></> : <StyledImage src={notiImg} floated="right" />}
      </Grid.Column>
    </Grid.Row>
  </MainGrid>
)

export default NavTopHeader;