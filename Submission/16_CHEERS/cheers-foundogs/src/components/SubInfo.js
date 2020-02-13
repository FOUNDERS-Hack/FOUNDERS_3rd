import React, { useEffect, useState } from "react";
import styled from "styled-components";
import MakersContract from "klaytn/MakersContract";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  width: ${props => props.theme.maxCardWidth};
`;

const TargetDescription = styled.p`
  width: 96%;
  font-size: 18px;
  margin-bottom: 4px;
  font-weight: 600;
  line-height: 160%;
  color: #03a87c;
`;

const Description = styled.p`
  margin-top: 16px;
  width: 98%;
  font-size: 16px;
  font-weight: 100;
  line-height: 140%;
`;

const ColoredBox = styled.div`
  /* background-color:#E5FAF6; */
  padding: 20px;
`;

export default ({ tokenId, D_day }) => {
  const [targetKlay, setTargetKlay] = useState("");

  // const dayGap = new Date(D_day) - new Date();
  // const daySeconds = 24 * 60 * 60 * 1000;
  // const dateGap = parseInt(dayGap / daySeconds) + 1;

  const _showTargetKlay = tokenId => {
    MakersContract.methods
      .showTargetKlay(tokenId)
      .call()
      .then(targetKlay => {
        if (!targetKlay) {
          return 0;
        }
        setTargetKlay(targetKlay);
      });
  };

  useEffect(() => {
    if (tokenId) {
      _showTargetKlay(tokenId);
    } else {
      console.log("no token id ");
    }
  }, [tokenId]);

  return (
    <Container>
      <ColoredBox>
        <TargetDescription>목표금액 : {targetKlay} KLAY</TargetDescription>
        <TargetDescription>펀딩기간 : {D_day}까지</TargetDescription>
        <Description>
          목표 KLAY가 100% 이상 모이면 메이커스가 성공되는 프로젝트입니다!
          <br />
          이 프로젝트는 마감일까지 목표 금액이 100% 모이지 않으면 결제가
          진행되지 않습니다.
          <br />
          에코 메이커스의 멋진 아이디어가 세상에 알려질 수 있도록 SNS에
          공유해주세요.
          <br />
          SNS 공유 이벤트 참여시 추첨을 통해 최대 100 ECO를 적립해 드립니다.
          <br />
          좋은 아이디어도 나누고 ECO도 받을 수 있는 이 기회를 놓치지 마세요!
        </Description>
      </ColoredBox>
    </Container>
  );
};
