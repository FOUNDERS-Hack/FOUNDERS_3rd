import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn, Form, Progress } from "semantic-ui-react";
import CardMeta from "./CardMeta";
import CardHeader from "./CardHeader";
import Link from "next/link";
import { IAnimalData, IElectionData } from "../../constants/interface";
// import { parsingAPI } from '../../commonAPI/parsingAPI'
import { observer, inject } from "mobx-react";
import { observable } from "mobx";
import { STORE } from "../../constants/stores";
import { GlobalStore } from "../../stores/globalStore";
import { voteAPI } from "../../API/VoteAPI";
import Router from "next/router";

const logo1 = require("../../assets/produce2.png");
const logo2 = require("../../assets/mister2.png");
const profileImg = require("../../assets/me.png");

interface IResultProps {
  voted?: boolean;
  globalStore?: GlobalStore;
}

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
`;

const StyledDiv = styled.div`
  height: 100% !important;
  padding-top: 45px;
  -webkit-tap-highlight-color: transparent;
`;
const LeftDiv = styled.div`
  width: 312px;
  float: left;
`;
const StyledFormField = styled(Form.Field)`
  margin-bottom: 40px !important;
`;

const SubmitButton = styled(Button)`
  width: 312px;
  height: 50px;
  border-radius: 4px;
  background-color: #2e5bff !important;
  color: #ffffff !important;
`;

const BoldText = styled.b`
  font-weight: 600;
`;

const TextDiv = styled.div`
  display: flex;
  padding-bottom: 20px !important;
`;

const BoldSubText = styled.span`
  font-family: "AppleSDGothicNeo";
  font-size: 14px;
  font-weight: bold;
  font-stretch: normal;
  font-style: normal;
  line-height: 0.86;
  letter-spacing: normal;
  color: #8798ad;
  margin-top: 194px !important;
`;

const NormalSubText = styled(BoldSubText)`
  font-weight: normal;
`;

const StyledImg = styled(Image)`
  margin-bottom: 10px;
`;

@inject(STORE.globalStore)
@observer
export default class Result extends React.Component<IResultProps> {
  componentDidMount = async () => {
    this.props.globalStore!.setElectionData();
    console.log(await voteAPI.txGetWhoAmI());

    // await this.props.globalStore!.setCandidateData();
    // console.log(this.props.globalStore.candidateData[0].candidateName);
  };
  render() {
    return (
      <StyledDiv>
        <LeftDiv>
          <TextDiv>
            <MainText>
              <BoldText>{this.props.globalStore.electionData.electionName}</BoldText> 진행 중!
            </MainText>
          </TextDiv>
          <div style={{ marginTop: "" }}>
            <Grid columns={1}>
              {this.props.globalStore.candidateData.map(item => (
                <Grid.Row style={{ marginBottom: "40px" }}>
                  <Grid.Column style={{ display: "flex", alignItems: "center" }}>
                    <StyledImg circular size="tiny" src="https://react.semantic-ui.com/images/avatar/large/matthew.png" style={{ marginRight: "20px" }} />
                    <MainText>{item.candidateName}</MainText>
                    {this.props.globalStore.electionData.voted === false ? (
                      <Button
                        style={{ position: "absolute", right: "10px", backgroundColor: "#2e5bff", color: "#ffffff" }}
                        onClick={async () => {
                          await voteAPI.txVote(item.candidateID).then(async () => {
                            await this.props.globalStore.setVotedData(this.props.globalStore.electionData.electionName, item.candidateName).then(() => {
                              Router.push({ pathname: "/VisaCompletePage" });
                            });
                          });
                        }}
                      >
                        투표하기
                      </Button>
                    ) : (
                      <Button disabled style={{ position: "absolute", right: "10px" }} id={item.candidateID}>
                        투표완료
                      </Button>
                    )}
                  </Grid.Column>
                  <Grid.Column>
                    <Progress percent={(item.voteCount / this.props.globalStore.electionData.totalVoted) * 100} progress color="blue">
                      {item.voteCount + "표"}
                    </Progress>
                  </Grid.Column>
                </Grid.Row>
              ))}
            </Grid>
          </div>
        </LeftDiv>
      </StyledDiv>
    );
  }
}
