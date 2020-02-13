import React, { useState } from "react";
import styled from "styled-components";
import MakersContract from "klaytn/MakersContract";
import cav from "klaytn/caver";
import { getWallet } from "utils/crypto";
import EcoTokenContract from "klaytn/EcoTokenContract";
import dealService from "../api/deal";
import { toast } from "react-toastify";

const Container = styled.div`
  position: relative;
  width: 100%;
  height: 100px;
  min-height: 100%;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

// const LikeButton = styled.span`
//   cursor: pointer;
//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
//   background-color: ${props => props.theme.headerColor};
// `;

// const Order = styled.div`
//   display: grid;
//   grid-template-columns: 4fr 9fr;
//   height: 65px;
//   width: 100%;
//   position: fixed;
//   bottom: 0;
//   max-width: ${props => props.theme.maxCardWidth};
// `;

const OrderText = styled.div`
  font-size: 20px;
  font-weight: 500;
  color: ${props => props.theme.white};
`;

const OrderButton = styled.button`
  cursor: pointer;
  display: flex;
  position: fixed;
  bottom: 10px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: ${props => props.theme.mainColor};
  width: 520px;
  height: 64px;
  border-radius: 30px;
  font-size: 12px;
  text-align: center;
  font-weight: 200;
  padding: 20px 0px;
  border: 0;
  box-shadow: 0 10px 10px 0 rgba(0, 0, 0, 0.2);
  @import url("https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap");
  font-family: -apple-system, "Noto Sans KR", sans-serif, BlinkMacSystemFont,
    "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans";
`;

async function getCount(address) {
  const cnt = await cav.klay.getTransactionCount(address);
  return cnt;
}

export default ({ userAddress, tokenId, onClick }) => {
  // const _investMakers = tokenId => {
  //   console.log("invest", tokenId);

  //   MakersContract.methods
  //     .showMakersState(tokenId)
  //     .call()
  //     .then(result => {
  //       if (result === "0" || result === "2") {
  //         console.log("신청 종료된 상품입니다.");
  //         toast.error("신청 종료된 상품입니다.");
  //         return 0;
  //       } else {
  //         MakersContract.methods
  //           .prohibitOverlap(tokenId)
  //           .call()
  //           .then(result2 => {
  //             if (result2 === false) {
  //               console.log("이미 신청한 상품입니다.");
  //               toast.error("이미 신청한 상품입니다.");
  //               return 0;
  //             } else {
  //               MakersContract.methods
  //                 .showMakersPrice(tokenId)
  //                 .call()
  //                 .then(price => {
  //                   if (!price) {
  //                     return 0;
  //                   }

  //                   getCount(getWallet().address).then(cnt => {
  //                     MakersContract.methods
  //                       .investMakers(tokenId)
  //                       .send({
  //                         from: getWallet().address,
  //                         gas: "200000000",
  //                         value: cav.utils.toPeb(price.toString(), "KLAY"),
  //                         nonce: cnt + 1
  //                       })
  //                       .once("transactionHash", txHash => {
  //                         console.log("txHash:", txHash);

  //                         // TODO : param1 : txHash
  //                         // TODO : 여기!
  //                         dealService.registerDeal(txHash);

  //                         toast.info("처리중입니다.");
  //                       })
  //                       .once("receipt", receipt => {
  //                         receipt.status
  //                           ? toast.success("신청 성공하였습니다") &&
  //                             toast.success(
  //                               `TX Hash: '${receipt.transactionHash}'`
  //                             )
  //                           : toast.error("신청 실패하였습니다");
  //                       })
  //                       .once("error", error => {
  //                         console.log("_Invest Error");
  //                         console.log(error);
  //                         toast.error(`실패하였습니다 ${error.toString()}`);
  //                       });
  //                   });
  //                 });

  //               console.log("------------------------------------");
  //               console.log("reward Eco power");
  //               console.log("------------------------------------");

  //               EcoTokenContract.methods
  //                 .transfer(getWallet().address, 3)
  //                 .send({
  //                   from: getWallet().address,
  //                   gas: "200000000"
  //                 })
  //                 .once("receipt", receipt => {
  //                   console.log("Eco power 영수증");
  //                   toast.success(`에코파워가 지급되었습니다.`);
  //                   toast.success(`${receipt.transactionHash}`);
  //                 })
  //                 .once("error", error => {
  //                   console.log(error);
  //                   toast.success(`에코파워 지급에 실패하였습니다.`);
  //                 });
  //             }
  //           });
  //       }
  //     });
  // };

  // const [isLiked, setIsLiked] = useState(false);

  // const handleLike = () => {
  //   setIsLiked(!isLiked);
  //   console.log(isLiked);
  // };

  return (
    <Container>
      {/* <LikeButton onClick={handleLike}>
          {isLiked ? <HeartFull /> : <HeartEmpty />}
          <LikeText>좋아요</LikeText>
        </LikeButton> */}
      <OrderButton onClick={onClick}>
        <OrderText>분양 신청하기</OrderText>
      </OrderButton>
    </Container>
  );
};
