import React, { useEffect, useState } from "react";
import MakersProduct from "./presenter";
import { withRouter } from "react-router-dom";

export default withRouter(
  ({
    match: {
      params: { tokenId }
    },
    ...props
  }) => {
    const { feed, userAddress, getFeed } = props;
    const [isLoading, setIsLoading] = useState(true);
    const [isLikedState, setIsLiked] = useState(false);
    const [likeCountState, setLikeCount] = useState(0);
    const [currentItem, setCurrentItem] = useState(0);

    const slideNext = () => {
      const totalFiles = feed.length;
      if (currentItem === totalFiles - 1) {
        setCurrentItem(0);
      } else {
        setCurrentItem(currentItem + 1);
      }
    };

    const slidePrev = () => {
      const totalFiles = feed.length;
      if (currentItem === 0) {
        setCurrentItem(totalFiles - 1);
      } else {
        setCurrentItem(currentItem - 1);
      }
    };

    const toggleLike = () => {
      if (isLikedState) {
        setIsLiked(false);
        setLikeCount(likeCountState - 1);
      } else {
        setIsLiked(true);
        setLikeCount(likeCountState + 1);
      }
    };

    useEffect(() => {
      if (!feed) {
        getFeed();
      } else {
        setIsLoading(false);
      }
    }, [feed, getFeed]);

    const { ...feedData } = feed;

    const objlength = Object.keys(feedData).length;

    const product = feedData[objlength - tokenId];

    return (
      <MakersProduct
        product={product}
        userAddress={userAddress}
        isLoading={isLoading}
        tokenId={tokenId}
        likeCount={likeCountState}
        isLiked={isLikedState}
        setIsLiked={setIsLiked}
        setLikeCount={setLikeCount}
        currentItem={currentItem}
        slidePrev={slidePrev}
        slideNext={slideNext}
        toggleLike={toggleLike}
      />
    );
  }
);
