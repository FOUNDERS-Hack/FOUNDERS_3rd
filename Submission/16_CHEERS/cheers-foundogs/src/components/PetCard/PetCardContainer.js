import React from "react";
import PetCardPresenter from "./PetCardPresenter";

const PetCardContainer = props => {
  const { pet } = props;
  return (
    <PetCardPresenter
      breed={pet.breed}
      description={pet.description}
      gender={pet.gender}
      bgPhoto={pet.photo}
      tokenId={pet.tokenId}
      birth={pet.birth}
      centerIcon="fas fa-play-circle"
      topIcons={["far fa-heart", "fas fa-share"]}
    />
  );
};

PetCardContainer.propTypes = {};

export default PetCardContainer;
