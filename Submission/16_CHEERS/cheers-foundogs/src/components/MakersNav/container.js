import React, { useEffect, useState } from "react";
import MakersNav from "./presenter";

const Container = props => {
  const { feed, userAddress, getFeed, status } = props;

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!feed) {
      getFeed();
    } else {
      setIsLoading(false);
    }
  }, [feed, getFeed]);

  return (
    <MakersNav
      feed={feed}
      userAddress={userAddress}
      isLoading={isLoading}
      status={status}
    />
  );
};

export default Container;
