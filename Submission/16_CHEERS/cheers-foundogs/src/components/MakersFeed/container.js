import React from "react";
import MakersFeed from "./presenter";

const Container = props => {
  const { tabFeed, userAddress, status } = props;

  return (
    <MakersFeed feed={tabFeed} userAddress={userAddress} status={status} />
  );
};

export default Container;
