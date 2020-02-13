import React from 'react'
import styled from 'styled-components';
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn } from 'semantic-ui-react'
import CardOne from "../Card/CardOne"
import AddCard from "../Card/AddCard"
import UserHeader from "../UserHeader/UserHeader"
import NavTopHeader from "../Common/NavTopHeader"
import Link from 'next/link'

const verificationImage = require("../../assets/verification/invalid-name@2x.png")
interface IVerificationProps {
  children: JSX.Element | JSX.Element[];
  width?: string;
  height?: string;
}

const StyledGrid = styled(Grid)`
  text-align: center;
  vertical-align: middle;
  height: 575px;
  /* height: 100vh; */
`

const StyledGridColumn = styled(Grid.Column)`
  max-width: 450;
`

const MainText = styled.span`
  width: 165px;
  height: 48px;
  font-family: 'AppleSDGothicNeo';
  font-size: 18px;
  font-weight: 300;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.33;
  letter-spacing: normal;
  text-align: center;
  color: #2e384d;
`

const BoldText = styled(MainText)`
  font-weight: 600;
`

const GridRow = styled(Grid.Row)`
  padding-top: 45px !important;
  width: ${props => props.width} !important;
  height: ${props => props.height} !important;
`

const BaseDiv = styled.div`
height: 100%;

`

const StyledDiv = styled.div`
  padding-top: 45px;
`

const StyledImage = styled.img`
width: 292px;
  height: 465px;
  object-fit: contain;
  margin-right: 0px !important;
`

const StyledButton = styled(Button)`
  width: 312px;
  height: 50px;
  border-radius: 4px;
  background-color: #2e5bff !important;
  color: #ffffff !important;
`

const WrapperDiv = styled.div`
position: relative;
`
const ButtonDiv = styled.div`
  position: absolute;
  left: -10px;
  bottom: 0.7em;
`
// <Card.Group> 으로 카드 그룹 묶으면 가운데 정렬되지 않음
const Verification = () => (
  // <StyledGrid>
  <StyledDiv>
    <div>
      <MainText>본인인증을 위해</MainText>
      <br />
      <BoldText>신분증</BoldText><MainText>을 불러와주세요</MainText>
    </div>
    <WrapperDiv>
      <div>
        <StyledImage src={verificationImage} />
      </div>
      <ButtonDiv>
        <Link href="/MainPage">
          <StyledButton >신분증 불러오기</StyledButton>
        </Link>
      </ButtonDiv>
    </WrapperDiv>
  </StyledDiv>
  // </StyledGrid>
)

export default Verification;