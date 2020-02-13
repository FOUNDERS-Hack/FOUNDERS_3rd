import React from "react";
import styled from "styled-components";
import { Button } from "semantic-ui-react";
import { observable } from "mobx";
import { observer } from "mobx-react";

import caver from "../../klaytn/caver";
// import {CountContract} from "../../SmartContract/ABI";
// import {CountContractCA} from "../../SmartContract/CA";

const StyledContainer = styled.div`
  display: flex;
  height: 100vh;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

@observer
export class TransactionMainPage extends React.Component {
  @observable sender_key = "0xc912a9704c5e023404d165a752e6121c6d13cf70141d119944a65eb364d926ba";
  @observable countContract: any;

  componentDidMount() {
    caver.klay.accounts.wallet.add(this.sender_key);

    // this.countContract = new caver.klay.Contract(CountContract, CountContractCA);
  }

  sendTransaction = () => {
    const walletInstance = caver.klay.accounts.wallet && caver.klay.accounts.wallet[0];

    if (!walletInstance) return;

    this.countContract.methods
      .minus()
      .send({
        from: walletInstance.address,
        gas: "200000"
      })
      .once("transactionHash", txHash => {
        console.log(`
          Sending a transaction... (Call contract's function 'plus')
          txHash: ${txHash}
          `);
      })
      .once("receipt", receipt => {
        console.log(
          `
          Received receipt! It means your transaction(calling plus function)
          is in klaytn block(#${receipt.blockNumber})
        `,
          receipt
        );
        this.setState({
          settingDirection: null,
          txHash: receipt.transactionHash
        });
      })
      .once("error", error => {
        alert(error.message);
        this.setState({ settingDirection: null });
      });
  };

  render() {
    return (
      <StyledContainer>
        <Button primary onClick={this.sendTransaction}>
          트랜잭션 ㄱㄱ
        </Button>
      </StyledContainer>
    );
  }
}
