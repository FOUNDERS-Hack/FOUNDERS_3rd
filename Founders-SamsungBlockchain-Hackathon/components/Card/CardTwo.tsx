import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn } from "semantic-ui-react";
import CardMeta from "./CardMeta";
import CardHeader from "./CardHeader";
import Link from "next/link";
import { IAnimalData, IElectionData, ICandidateData } from "../../constants/interface";
// import { parsingAPI } from '../../commonAPI/parsingAPI'
import { observer, inject } from "mobx-react";
import { STORE } from "../../constants/stores";
import { observable } from "mobx";
import { GlobalStore } from "../../stores/globalStore";
const logo1 = require("../../assets/produce2.png");
const logo2 = require("../../assets/mister2.png");

interface ICardOneProps {
  children?: JSX.Element | JSX.Element[];
  text?: string;
  className?: string;
  backText?: string;
  itemList?: ICandidateData;
  label?: string;
  img?: string;
  globalStore?: GlobalStore;
}

const GridButtonGroup = styled(GridColumn)`
  padding: 0 !important;
`;

const ColoredTextButton = styled.button`
  text-align: center !important;
  border: 0 !important;
  outline: 0 !important;
  font-size: 14px;
  font-weight: 600;
  color: #5c7fff;

  background-color: transparent !important;
  background-image: none !important;
  border-color: transparent;
  border: none;
`;

const StyledCard = styled(Card)`
  /* to remove ios safari grey box */
  -webkit-tap-highlight-color: transparent;
  border-radius: 12px !important;
  background-color: #ffffff !important;
  box-shadow: 0 10px 20px 0 rgba(46, 91, 255, 0.07) !important;
  border: solid 1px rgba(46, 91, 255, 0.08) !important;
  /* width: 380px !important; */
`;

const LeftAlignedCardContent = styled(Card.Content)`
  text-align: left;
`;

const RightTopText = styled(Card.Header)<ICardOneProps>`
  background-color: ${props => (props.label === "HOT" ? "#ff6b82" : "#6ba1ff")};
  font-size: 11px !important;
  font-weight: normal !important;
  font-stretch: normal !important;
  font-style: normal !important;
  line-height: normal !important;
  letter-spacing: normal !important;
  color: #ffffff !important;
  text-align: left !important;
  border: none !important;
  margin-bottom: 5px !important;
  border-radius: 5px;
  padding: 3px 5px !important;
  display: inline-block !important;
  /* margin-top: 5px !important; */
`;

const StyledImg = styled(Image)`
  display: block !important;
  margin-bottom: 10px !important;
`;

const StyledDiv = styled.div`
  display: flex;
  align-items: center;
`;

@inject(STORE.globalStore)
@observer
export default class CardTwo extends React.Component<ICardOneProps> {
  // @observable gender = "";

  // parseGender() {
  //   if (this.props.itemList.gender.toString() === "0") {
  //     this.gender = "여";
  //   } else if (this.props.itemList.gender.toString() === "1") {
  //     this.gender = "남";
  //   } else {
  //     this.gender = "무";
  //   }
  // }

  render() {
    // this.parseGender();
    // console.error(this.gender);
    return (
      <StyledCard>
        <LeftAlignedCardContent>
          <RightTopText label={this.props.label}>{this.props.label === "HOT" ? "HOT" : "NEW"}</RightTopText>
          <StyledDiv>
            <StyledImg size="big" src={this.props.img === "1" ? logo1 : logo2} />
          </StyledDiv>
          <CardHeader title="프로듀스 101 2차 투표" />
          <CardMeta title="시작 날짜" description="2020.02.12~2020.03.15" icon="calendar alternate" />
          <CardMeta title="투표수" description="17,983" icon="ticket" />
        </LeftAlignedCardContent>
        <Card.Content extra>
          <Link href="/VoteResultPage">
            <ColoredTextButton>투표하기</ColoredTextButton>
          </Link>
        </Card.Content>
      </StyledCard>
    );
  }
}
