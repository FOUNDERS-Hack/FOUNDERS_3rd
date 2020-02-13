import { observable, action, values } from "mobx";
import { IElectionData, ICandidateData } from "../constants/interface";
import { voteAPI } from "../API/VoteAPI";

export class GlobalStore {
  @observable votedData: any = {
    votedElectionName: "",
    votedCandidateName: ""
  };
  @action
  setVotedData = async (electionName: string, candidateName: string) => {
    this.votedData.votedElectionName = electionName;
    this.votedData.votedCandidateName = candidateName;
  };

  @observable electionData: IElectionData = {
    electionID: 0,
    electionName: "",
    voted: false,
    totalVoted: 0
  };

  @action
  setElectionData = async () => {
    this.electionData.electionID = 1;
    this.electionData.electionName = "미스터트롯 결승 투표";
    this.electionData.voted = await voteAPI.txGetVotedOrNot();
    this.electionData.totalVoted = await voteAPI.txGetTotalVoteCount();
  };

  @action
  setElectionVotedData = async (voted: boolean) => {
    this.electionData.voted = voted;
  };

  @observable candidateData: ICandidateData[] = [
    {
      candidateID: 0,
      candidateName: "",
      voteCount: 0
    },
    {
      candidateID: 0,
      candidateName: "",
      voteCount: 0
    }
  ];

  @action
  setCandidateData = async () => {
    const totalCandidates = await voteAPI.txGetTotalCandidates();
    for (let i = 0; i < totalCandidates; i++) {
      this.candidateData[i].candidateID = i + 1;
      this.candidateData[i].candidateName = await voteAPI.txGetCandidateName(i + 1);
      this.candidateData[i].voteCount = await voteAPI.txGetVoteCountPerCandidate(i + 1);
    }
  };
}
