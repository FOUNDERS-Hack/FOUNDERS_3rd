import React from "react";
// import { KLAY_FAUCET } from "constants/url";
// import Input from "components/Input";
import styled from "styled-components";

import Menu from "../WalletComponents/Menu";
import UserInfo from "../WalletComponents/UserInfo";
import TransactionsList from "../WalletComponents/TransactionsList";
import { Divider } from "@material-ui/core";

const Container = styled.div``;

// const GreenFont = styled.p`
//   font-size: 12px;
//   color: ${props => props.theme.lightGreen};
// `;

export default ({ address, balance }) => {
  return (
    <Container>
      <UserInfo address={address} balance={balance} />

      <Menu />
      <Divider />
      <TransactionsList address={address} />
    </Container>
  );
};
