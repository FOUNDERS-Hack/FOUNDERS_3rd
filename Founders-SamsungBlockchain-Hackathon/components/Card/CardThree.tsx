import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn } from "semantic-ui-react";
import CardMeta from "./CardMeta";
import CardHeader from "./CardHeader";
import Link from "next/link";
import { IAnimalData, IElectionData } from "../../constants/interface";
// import { parsingAPI } from '../../commonAPI/parsingAPI'
import { observer } from "mobx-react";
import { observable } from "mobx";
const logo1 = require("../../assets/produce2.png");

// interface ICardOneProps {
//   children?: JSX.Element | JSX.Element[];
//   text?: string;
//   className?: string;
//   backText?: string;
//   itemList: IAnimalData;
// }

interface ICardOneProps {
  children?: JSX.Element | JSX.Element[];
  text?: string;
  className?: string;
  backText?: string;
  itemList?: IElectionData;
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

const NoneColoredTextButton = styled.button`
  font-size: 14px;
  font-weight: 600;
  color: #586e89;

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

const RightTopText = styled(Card.Header)`
  /* font-family: "Montserrat" !important; */
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
  /* margin: 10px 0px 10px 0px !important; */
  background-color: #6ba1ff;
  border-radius: 5px;
  padding: 3px 5px !important;
  display: inline-block !important;
  /* margin-top: 5px !important; */
`;

const StyledImg = styled(Image)`
  display: block;
  margin-bottom: 0 !important;
  /* width: auto !important; */
  /* height: 150px !important; */
`;

const StyledDiv = styled.div`
  display: flex;
  align-items: center;
`;
@observer
export default class CardThree extends React.Component<ICardOneProps> {
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
          <RightTopText>NEW</RightTopText>
          <StyledImg floated="left" size="medium" src={logo1} />
          {/* <CardHeader title={this.props.itemList.name} description={"(" + this.gender + ", " + this.props.itemList.animalType + ")"} /> */}
          {/* <CardMeta title="생년월일" description={this.props.itemList.birth} /> */}
          {/* <CardMeta title="등록번호" description={this.props.itemList.animalID} /> */}
          {/* <CardMeta title="등록일자" description={parsingAPI.getDate()} /> */}
          <CardHeader title="프로듀스101 2차 투표" />
          <CardMeta title="시작 날짜" description="2020.02.13~2020.02.15" icon="calendar alternate" />
          <CardMeta title="투표수" description="53" icon="ticket" />
          {/* <CardMeta title="두번째 타이틀" description="어쩌구저쩌구" /> */}
        </LeftAlignedCardContent>
        <Card.Content extra>
          {/* <Grid columns={2} celled="internally"> */}
          {/* <GridButtonGroup> */}
          <Link href="/VisaFlightPage">
            <ColoredTextButton>투표하기</ColoredTextButton>
          </Link>
          {/* </GridButtonGroup> */}
          {/* <GridButtonGroup>
              <NoneColoredTextButton></NoneColoredTextButton>
            </GridButtonGroup> */}
          {/* </Grid> */}
        </Card.Content>
      </StyledCard>
    );
  }
}
