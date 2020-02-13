import retry from "async-retry";
import * as ethers from "ethers";
import React from "react";

import caver from "../klaytn/caver";
// import { AnimalCareContract } from "../SmartContract/AnimalCareABI";
import { VoteContract } from "./VoteABI";
import { PRIVATE_KEY, ADMIN_ADRESS, ANIMAL_CARE_CA, SMART_CONTRACT_EXECUTION, GAS_LIMIT } from "../constants/define";
import { IAnimalData } from "../constants/interface";

class VoteAPI {
  voteContract: any;
  txBlockNumber: number = 0;

  constructor() {
    caver.klay.accounts.wallet.add(PRIVATE_KEY);
    this.voteContract = new caver.klay.Contract(VoteContract, ANIMAL_CARE_CA);
  }

  // 몇명의 후보자 있는지 가져온다
  public txGetTotalCandidates = async () => {
    return await this.voteContract.methods.getTotalCandidates().call();
  };

  public txGetCandidateName = async (id: number) => {
    return await this.voteContract.methods.getCandidateName(id).call();
  };

  public txGetVoteCountPerCandidate = async (id: number) => {
    return await this.voteContract.methods.getVoteCountPerCandidate(id).call();
  };

  public txGetVotedOrNot = async () => {
    return await this.voteContract.methods.getVotedOrNot(ADMIN_ADRESS).call();
    // return await this.voteContract.methods.getVotedOrNot(ADMIN_ADRESS).call();
  };

  public txGetTotalVoteCount = async () => {
    return await this.voteContract.methods.getTotalVoteCount().call();
  };

  public txVote = async (candidateID: number) => {
    const encodedAbi = await this.voteContract.methods.vote(candidateID).encodeABI();
    await this.sendTransaction(encodedAbi);
  };

  public txGetMyVoteResult = async () => {
    await this.voteContract.methods.getMyVoteResult().call();
  };

  public txGetWhoAmI = async () => {
    await this.voteContract.methods.whoAmI().call();
  };

  private sendTransaction = async encodedAbi => {
    await retry(
      () => {
        caver.klay
          .sendTransaction({
            type: SMART_CONTRACT_EXECUTION,
            from: ADMIN_ADRESS,
            to: ANIMAL_CARE_CA,
            data: encodedAbi,
            gas: GAS_LIMIT,
            value: 0
          })
          .then(function(receipt) {
            console.log(
              `
              Received receipt! It means your transaction(calling plus function)
              is in klaytn block(#${receipt.blockNumber})
            `
            ),
              receipt;
          });
      },
      { retries: 5, factor: 1, minTimeout: 300, randomize: true }
    );
  };
}

export const voteAPI = new VoteAPI();
