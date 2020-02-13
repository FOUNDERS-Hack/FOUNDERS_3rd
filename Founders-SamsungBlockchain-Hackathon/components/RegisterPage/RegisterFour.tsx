import React, { Component, useState } from 'react'
import styled from 'styled-components';
import { Input, Radio, Form, Dropdown, Button, GridColumn, TextArea } from 'semantic-ui-react'
import CardOne from "../Card/CardOne"
import AddCard from "../Card/AddCard"
import UserHeader from "../UserHeader/UserHeader"
import Car from "../Common/NavTopHeader"
import Link from 'next/link'
import { GlobalStore } from '../../stores/globalStore';
import { inject, observer } from 'mobx-react';
import { STORE } from '../../constants/stores';

const cardImage = require("../../assets/visa.png");

interface IRegisterProps {
  children: JSX.Element | JSX.Element[];
  width?: string;
  height?: string;
  isClicked?: boolean;
  primary?: boolean;
}


const MainText = styled.div`
  font-family: 'AppleSDGothicNeo';
  font-size: 22px;
  font-weight: 300;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.33;
  letter-spacing: normal;
  color: #2e384d;
  text-align: left !important;
  /* float: left !important; */
`

const StyledDiv = styled.div`
  height: 100% !important;
  padding-top: 45px;
  -webkit-tap-highlight-color: transparent;

`

const LeftDiv = styled.div`
/* display: flex; */
width: 312px;
float: left;
`
const StyledFormField = styled(Form.Field)`
  margin-bottom: 12px !important;
  float: left;
  /* height: 50px !important; */
`

const SubmitButton = styled(Button)`
  width: 312px;
  height: 50px;
  border-radius: 4px;
  background-color: #2e5bff !important;
  color: #ffffff !important;
`

const BoldText = styled.b`
  font-weight: 600;
`

const TextDiv = styled.div` 
  display: flex;
  padding-bottom: 20px !important;
`

// const CenterDiv

const SubText = styled(MainText)`
  color: #8798ad;
  font-size: 12px;
  width: 100%;
  float: center;
`

const BoldSubText = styled.span`
  font-family: 'AppleSDGothicNeo';
  font-size: 14px;
  font-weight: bold;
  font-stretch: normal;
  font-style: normal;
  line-height: 0.86;
  letter-spacing: normal;
  color: #8798ad;
  margin-top: 194px !important;
`

const NormalSubText = styled(BoldSubText)`
  font-weight: normal;
`

const StyledImage = styled.img`
  /* width: 292px; */
  width: 100%;
  height: 100%;
  /* height: 465px; */
  object-fit: contain;
  /* margin-right: 0px !important; */
`

interface IRegisterFourProps {
  globalStore?: GlobalStore;
}

@inject(STORE.globalStore)
@observer
export default class RegisterFour extends React.Component<IRegisterFourProps> {
  render() {
    return(
      <StyledDiv>
      <LeftDiv>
        <TextDiv>
          <MainText>증명서에 들어갈<br />
            <BoldText>{this.props.globalStore.tempAniName}</BoldText>의 사진을 추가해주세요.</MainText>
        </TextDiv>
        <TextDiv>
          <SubText>등록시 카드에 사진이 적용됩니다.</SubText>
        </TextDiv>
        <div>
          <StyledImage src={cardImage} />
        </div>
        <div style={{ marginTop: '114px' }}>
          <NormalSubText><BoldSubText>다음에 추가하기</BoldSubText></NormalSubText>
        </div>
        <Link href="/MainPage">
          <SubmitButton type="submit" style={{ marginTop: '24px' }}>사진 추가하기</SubmitButton>
        </Link>
      </LeftDiv >
    </StyledDiv >
    );
  }
}