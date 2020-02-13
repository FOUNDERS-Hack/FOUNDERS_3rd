import React, { useEffect, useState } from "react";
import WalletInfo from "./presenter";
import caver from "klaytn/caver";

const Container = props => {
  const { address, logout } = props;
  const [balance, setBalance] = useState(0);

  const getBalance = address => {
    if (!address) return;
    caver.klay.getBalance(address).then(balance => {
      setBalance(caver.utils.fromWei(balance, "ether"));
    });
  };

  useEffect(() => {
    getBalance(address);
  }, [address]);

  return <WalletInfo address={address} balance={balance} logout={logout} />;
};

export default Container;
