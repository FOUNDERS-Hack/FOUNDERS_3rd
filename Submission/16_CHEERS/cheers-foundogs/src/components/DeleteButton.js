import React from "react";
import styled from "styled-components";
import Button from "components/Button";
import { getWallet } from "utils/crypto";
import MakersContract from "klaytn/MakersContract";
import { Redirect } from "react-router-dom";
import { toast } from "react-toastify";

const Container = styled.div`
  position: relative;
  width: 100%;
  min-height: 100%;
  padding: 30px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const StyledButton = styled(Button)`
  background-color: ${props => props.theme.lightGrey};
`;

const _prohibitMakers = tokenId => {
  MakersContract.methods
    .showMakersState(tokenId)
    .call()
    .then(result => {
      if (result === "0") {
        console.log("이미 종료된 Makers 입니다.");
        toast.error(`이미 종료된 Makers 입니다.`);
        return 0;
      } else {
        MakersContract.methods
          .forcedClosure(tokenId)
          .send({
            from: getWallet().address,
            gas: "200000000"
          })
          .once("transactionHash", txHash => {
            console.log("txHash:", txHash);
            toast.info("처리중입니다.");
          })
          .once("receipt", receipt => {
            receipt.status
              ? toast.success(`상품이 완료상태로 변경되었습니다.`) &&
                toast.success(`TX Hash: ${receipt.transactionHash}`)
              : toast.error(`실패하였습니다`);

            return <Redirect to="/makers/" />;
          })
          .once("error", error => {
            toast.error(`실패하였습니다`);
            toast.error(`${error.toString()}`);
          });
      }
    });
};

export default ({ tokenId }) => {
  return (
    <Container>
      <StyledButton onClick={() => _prohibitMakers(tokenId)}>
        Delete
      </StyledButton>
    </Container>
  );
};
