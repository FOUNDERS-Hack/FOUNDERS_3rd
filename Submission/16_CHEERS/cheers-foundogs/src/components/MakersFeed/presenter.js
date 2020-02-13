import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import PetCard from "components/PetCard";
import OptionBar from "components/OptionBar";
import { pets } from "data/pets";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  ${props => props.theme.maxCardWidth};
`;

const FeedProduct = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;

  position: relative;
  width: 100%;
  max-width: ${props => props.theme.maxCardWidth};
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
`;

const FeedContainer = styled.div`
  position: relative;
  width: 100%;
  min-height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const MakersFeed = props => {
  const { feed } = props;

  return (
    <Container>
      <OptionBar />
      <FeedContainer>
        {feed &&
          feed.map(pet => {
            return (
              <FeedProduct key={pet.tokenId}>
                <Link to={`/makers/${pet.tokenId}`}>
                  <PetCard pet={pet} />
                </Link>
              </FeedProduct>
            );
          })}
        {pets &&
          pets.map(pet => {
            return (
              <FeedProduct key={pet.tokenId}>
                <Link to={`/makers/${pet.tokenId}`}>
                  <PetCard pet={pet} />
                </Link>
              </FeedProduct>
            );
          })}
      </FeedContainer>
    </Container>
  );
};

MakersFeed.defaultProps = {
  photo:
    "https://1.bp.blogspot.com/-y1m-3XnfuPg/XcEykIQLkNI/AAAAAAAAAB0/R3q6WDU_9zwbLtwEoeyqehUH_PPz3SdogCLcBGAsYHQ/s1600/Repurposed%2Bvinyl%2Bwallet.png"
};

export default MakersFeed;
