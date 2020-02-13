import React from 'react'
import styled, { css } from 'styled-components';
import { Grid, Card, Form, Dropdown, Button, GridColumn, TextArea } from 'semantic-ui-react'
import CardOne from "../Card/CardOne"
import AddCard from "../Card/AddCard"
import UserHeader from "../UserHeader/UserHeader"
import NavTopHeader from "../Common/NavTopHeader"
import Link from 'next/link'
import { SyncLoader } from 'react-spinners';
import { observer, inject } from 'mobx-react';
import { observable, action } from 'mobx';
import { useRouter } from 'next/router'
import Router from 'next/router'
import { animalCareAPI } from '../../klaytnAPI/AnimalCareAPI';
import { GlobalStore } from '../../stores/globalStore';
import { STORE } from '../../constants/stores';

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

const LoadingDiv = styled.div`
  display: flex;
  height: 504px;
  justify-content: center;
  align-items: center;
`

const LeftDiv = styled.div`
/* display: flex; */
width: 312px;
float: left;
`
const StyledFormField = styled(Form.Field)`
  margin-bottom: 40px !important;
`

const SubmitButton = styled(Button)`
  width: 312px;
  height: 50px;
  border-radius: 4px;
  background-color: #2e5bff !important;
  color: #ffffff !important;
`

const SelectButton = styled(Button)`
  /* height: 25px !important; */
  border-radius: 4px !important;
  margin-bottom: 10px !important;
  padding: 9px !important;
  margin-right: 8px !important;
`
const DivFloat = styled.div`
float: left;
text-align: left;
/* margin-bottom: 40px !important; */
`
const override = css`
    display: block;
    margin: 0 auto;
    border-color: red;
`;

interface IRegisterTwoProps {
  globalStore?: GlobalStore;
}

// <Card.Group> 으로 카드 그룹 묶으면 가운데 정렬되지 않음
@inject(STORE.globalStore)
@observer
export default class RegisterTwo extends React.Component<IRegisterTwoProps> {
  @observable loading: boolean = false;
  @observable loadingDone: boolean = false;

  @observable birth: string = "";
  @observable adoptionDate: string = "";

  @observable remarks: boolean[] = [false, false, false, false, false, false, false, false];

  @action
  remarksClick = (_type: number) => {
    this.remarks[_type] = !this.remarks[_type];
  }

  @action
  updateBirth = (value: string) => {
    this.birth = value;
  };

  @action
  updateAdoptionDate = (value: string) => {
    this.adoptionDate = value;
  };
  

  handler = () => {
    Router.push({
      pathname: '/RegisterThirdPage'
    });
  }
  
  @action
  onClick = async() => {
    this.loading = !this.loading;
    let gender: string = ""
    // 중성
    if(this.props.globalStore.neutralization === "1") {
      gender = "2";
    } else {
      if(this.props.globalStore.gender === "1") {
        gender = "1";
      } else {
        gender = "0"
      }
    }

    const animalName = this.props.globalStore.name;
    const animalType = this.props.globalStore.animalType;
    const animalColor = this.props.globalStore.colorType;
    this.props.globalStore.setTempAniName(animalName);

    await animalCareAPI.txSetFamily(this.props.globalStore.userDID.toString());
    setTimeout(
      async function() {
        await animalCareAPI.txSetRegistAnimal(
          this.props.globalStore.animalID.toString(), animalName, animalType, animalColor, 
          gender, this.birth, this.adoptionDate, "");

        this.loadingDone = true;
      }
      .bind(this),
      4000
  );

    this.props.globalStore.clearFirstRegistPage();
  }

  render() {
    if(this.loadingDone) {this.handler();}
    return (
      <StyledDiv>
        <LeftDiv>
          {
            this.loading ?
            <LoadingDiv>
              <SyncLoader
                sizeUnit={"px"}
                size={20}
                color={'#2E5BFF'}
                loading={true}
              />
            </LoadingDiv> : 
          <Form>
          <StyledFormField>
            <MainText>입양일을 입력해주세요</MainText>
            <input value={this.birth} onChange={value => this.updateBirth(value.target.value)} type="text" placeholder='20171022' />
          </StyledFormField>
          <StyledFormField>
            <MainText>생년월일을 입력해주세요</MainText>
            <input value={this.adoptionDate} onChange={value => this.updateAdoptionDate(value.target.value)} type="text" placeholder='20171022' />
          </StyledFormField>
          <StyledFormField>
            <MainText>병력을 적어주세요</MainText>
            <DivFloat>
              <SelectButton active={this.remarks[0]} onClick={() => this.remarksClick(0)} basic size="mini">슬개골</SelectButton>
              <SelectButton active={this.remarks[1]} onClick={() => this.remarksClick(1)} basic size="mini">결석</SelectButton>
              <SelectButton active={this.remarks[2]} onClick={() => this.remarksClick(2)} basic size="mini">습진</SelectButton>
              <SelectButton active={this.remarks[3]} onClick={() => this.remarksClick(3)} basic size="mini">눈물자국</SelectButton>
              <SelectButton active={this.remarks[4]} onClick={() => this.remarksClick(4)} basic size="mini">각막손상</SelectButton>
              <SelectButton active={this.remarks[5]} onClick={() => this.remarksClick(5)} basic size="mini">기관지협착증</SelectButton>
              <SelectButton active={this.remarks[6]} onClick={() => this.remarksClick(6)} basic size="mini">발작</SelectButton>
              <SelectButton active={this.remarks[7]} onClick={() => this.remarksClick(7)} basic size="mini">피부질환</SelectButton>
            </DivFloat>
            <TextArea placeholder='기타 (30자 이내)' />
          </StyledFormField>
            <SubmitButton onClick={() => this.onClick()} type="submit">다음</SubmitButton>
        </Form>
          }
        </LeftDiv>
      </StyledDiv>
    );

  }

}