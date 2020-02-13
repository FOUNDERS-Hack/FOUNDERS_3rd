import React from "react";
import styled from "styled-components";
import { Grid } from "semantic-ui-react";
import CardOne from "../Card/CardOne";
import CardTwo from "../Card/CardTwo";

import AddCard from "../Card/AddCard";
import { IAnimalData, IElectionData, ICandidateData } from "../../constants/interface";
import { GlobalStore } from "../../stores/globalStore";
import { inject, observer } from "mobx-react";
import { STORE } from "../../constants/stores";
import { observable } from "mobx";
import CardThree from "../Card/CardThree";
// import { animalCareAPI } from '../../klaytnAPI/AnimalCareAPI';
import { voteAPI } from "../../API/VoteAPI";

const StyledGrid = styled(Grid)`
  text-align: center;
  vertical-align: middle;
  height: 575px;
  max-height: 640px !important;
  overflow-y: scroll;
  -webkit-overflow-scrolling: touch;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const StyledGridColumn = styled(Grid.Column)`
  max-width: 450px;
`;

interface IRegisterProps {
  globalStore?: GlobalStore;
}

@inject(STORE.globalStore)
@observer
export default class Main extends React.Component<IRegisterProps> {
  componentDidMount = async () => {
    this.props.globalStore!.setElectionData();
    this.props.globalStore!.setCandidateData();
  };

  render() {
    return (
      <StyledGrid>
        <StyledGridColumn>
          <CardOne label="HOT" img="2" />
          <CardTwo label="NEW" img="1" />
        </StyledGridColumn>
      </StyledGrid>
    );
  }
}
