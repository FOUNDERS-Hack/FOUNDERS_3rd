import React from 'react'
import styled from 'styled-components';
import { Grid, Card, Form, Input, Dropdown, Button, GridColumn } from 'semantic-ui-react'
import CardOne from "../Card/CardOne"
import AddCard from "../Card/AddCard"
import UserHeader from "../UserHeader/UserHeader"
import NavTopHeader from "../Common/NavTopHeader"
import Link from 'next/link'
import { observer, inject } from 'mobx-react';
import { observable, action } from 'mobx';
import { STORE } from '../../constants/stores';
import { GlobalStore } from '../../stores/globalStore';

const animalType = [
  { key: 'rt', value: '리트리버', text: '리트리버' },
  { key: 'sc', value: '시츄', text: '시츄' },
  { key: 'sb', value: '시바견', text: '시바견' },
  { key: 'bg', value: '비글', text: '비글' },
  { key: 'pm', value: '포메리안', text: '포메리안' },
]

const hairColorType = [
  { key: 'gold', value: '금색', text: '금색' },
  { key: 'black', value: '검정색', text: '검정색' },
  { key: 'white', value: '흰색', text: '흰색' },
  { key: 'grey', value: '회색', text: '회색' },
  { key: 'brown', value: '적갈색', text: '적갈색' },
]

interface IRegisterProps {
  children: JSX.Element | JSX.Element[];
  width?: string;
  height?: string;
  isClicked?: boolean;
  primary?: boolean;
}

// const StyledGrid = styled(Grid)`
//   text-align: left;
//   vertical-align: middle;
//   height: 575px;
//   /* height: 100vh; */
// `

// const StyledGridColumn = styled(Grid.Column)`
//   max-width: 450;
// `

const MainText = styled.div`
  font-size: 18px;
  font-weight: 300 !important;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.33;
  letter-spacing: normal;
  color: #2e384d;
  padding-bottom: 12px !important;
  text-align: left !important;
`

const StyledDiv = styled.div`
  padding-top: 45px;
  -webkit-tap-highlight-color: transparent;

`

const StyledImage = styled.img`
  width: 292px;
  height: 465px;
  object-fit: contain;
  margin-right: 0px !important;
`

// const StyledButton = styled(Button)`
//   width: 312px;
//   height: 50px;
//   border-radius: 4px;
//   background-color: #2e5bff !important;
//   color: #ffffff !important;
// `

const WrapperDiv = styled.div`
position: relative;
`
// const ButtonDiv = styled.div`
//   position: absolute;
//   left: -10px;
//   bottom: 0.7em;
// `
const LeftDiv = styled.div`
/* display: flex; */
width: 312px;
float: left;
`
const StyledFormField = styled(Form.Field)`
  margin-bottom: 40px !important;
`

const StyledInput = styled(Input)`
  /* not good to add */
  /* border: solid 1px #ced0da !important; */
`
const InputWrapper = styled.div`
  padding-bottom: 40px;
`

const StyledButton = styled(Button)`
  width: 148px;
  height: 36px;
  border-radius: 5px;
  border: solid 1px #2e5bff;
  background: #ffffff !important;
`;

const SubmitButton = styled(Button)`
  width: 312px;
  height: 50px;
  border-radius: 4px;
  background-color: #2e5bff !important;
  color: #ffffff !important;
`

interface IRegisterTwoProps {
  globalStore?: GlobalStore;
}

// <Card.Group> 으로 카드 그룹 묶으면 가운데 정렬되지 않음
@inject(STORE.globalStore)
@observer
export default class Register extends React.Component<IRegisterTwoProps> {
  render() {
    return (
      <StyledDiv>
        <LeftDiv>
          <Form>
            <StyledFormField>
              <MainText>반려견 이름이 무엇인가요?</MainText>
              <input value={this.props.globalStore.name} onChange={value => this.props.globalStore.updateName(value.target.value)} placeholder='최대 15자' />
            </StyledFormField>
            <StyledFormField>
              <MainText>품종을 선택해주세요</MainText>
              <Dropdown
                placeholder='선택'
                fluid
                search
                selection
                options={animalType}
                onChange={this.props.globalStore.animalTypeChange}
              />
            </StyledFormField>
            <StyledFormField>
              <MainText>모색을 선택해주세요</MainText>
              <Dropdown
                placeholder='선택'
                fluid
                search
                selection
                options={hairColorType}
                onChange={this.props.globalStore.colorTypeChange}
              />
            </StyledFormField>
            <StyledFormField>
              <MainText>성별을 선택해주세요</MainText>
              {/* TODO: color does not change */}
              <StyledButton active={this.props.globalStore.gender === "1"} basic onClick={() => this.props.globalStore.genderClick("1")}>수컷</StyledButton>
              <StyledButton active={this.props.globalStore.gender === "2"} basic onClick={() => this.props.globalStore.genderClick("2")}>암컷</StyledButton>
            </StyledFormField>
            <StyledFormField>
              <MainText>중성화 수술을 했나요?</MainText>
              {/* TODO: color does not change */}
              <StyledButton active={this.props.globalStore.neutralization === "1"} basic onClick={() => this.props.globalStore.neutralizationClick("1")}>예</StyledButton>
              <StyledButton active={this.props.globalStore.neutralization === "2"} basic onClick={() => this.props.globalStore.neutralizationClick("2")}>아니오</StyledButton>
            </StyledFormField>
            <Link href="/RegisterSecondPage">
              <SubmitButton>다음</SubmitButton>
            </Link>
          </Form>
        </LeftDiv>
      </StyledDiv>
    );

  }

}