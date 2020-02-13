import React from "react";
import styled from "styled-components";
import MakersDesc from "components/MakersDesc";
import OrderButton from "components/OrderButton";
import { makeStyles } from "@material-ui/core/styles";
import Chip from "@material-ui/core/Chip";
import FaceIcon from "@material-ui/icons/Face";
import DoneIcon from "@material-ui/icons/Done";
import { createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";
import GroupAvatars from "components/GroupAvatars";
import { HeartEmpty } from "components/common/Icons";
import Popper from "@material-ui/core/Popper";
import Fade from "@material-ui/core/Fade";
import Paper from "@material-ui/core/Paper";

const Container = styled.div`
  position: relative;
  width: 100%;
  min-height: 100%;
  padding: 30px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const ImageContainer = styled.div`
  position: relative;
  padding-bottom: 80%;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  flex-shrink: 0;
  width: 100%;
  max-width: ${props => props.theme.maxCardWidth};
  z-index: -1;
`;

const InfoContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding-top: 40px;
`;

const Image = styled.div`
  width: 100%;
  height: 500px;
  position: absolute;
  top: 0;
  background-image: url(${props => props.src});
  background-size: cover;
  background-position: center;
  opacity: ${props => (props.showing ? 1 : 0)};
  transition: opacity 0.2s linear;
  border-radius: 40px 40px 0px 0px;
`;

const ColoredLine = styled.hr`
  border: 0.5px solid ${props => props.theme.lightGrey};
  width: 100%;
`;

const Bottom = styled.div`
  padding: 30px;

  margin-top: 80px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  z-index: 1;
  margin-top: -20px;
  background: ${props => props.theme.bgColor};
  border-radius: 40px 40px 0px 0px;
`;

const TopIconsContainer = styled.div`
  display: flex;
  padding: 12px;
`;

const Breed = styled.span`
  font-size: 26px;
  font-weight: 700;
  display: block;
  color: #1f2126;
`;

const PetInfoContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
`;

const Left = styled.div`
  display: flex;
`;

const Gender = styled.div`
  margin-right: 5px;
`;

const Registered = styled.div`
  margin-right: 5px;
`;

const Vaccinated = styled.div``;

const Distance = styled.div`
  font-size: 20px;
  color: ${props => props.theme.brownGrey};
`;

const Avatars = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
`;

const Birth = styled.span`
  font-size: 18px;
  font-weight: 500;
  margin-left: 15px;
  color: ${props => props.theme.brownGrey};
`;

const Wrapper = styled.div`
  display: flex;
  margin-bottom: 40px;
  align-items: center;
`;

const Appliant = styled.div`
  font-size: 20px;
  color: ${props => props.theme.brownGrey};
`;

const theme = createMuiTheme({
  palette: {
    primary: { main: "#1d3971" },
    secondary: { main: "#fd2eb3" },
    main: { main: "#fbae17" }
  }
});

const styledPaper = styled(Paper)`
  width: 500px;
  height: 100px;
  background: white;
`;

const MakersProduct = ({ userAddress, product }) => {
  const { ...item } = product;
  const {
    tokenId,
    appliant,
    photo,
    description,
    serialNum,
    birth,
    breed,
    gender = "남"
  } = item;

  const [anchorEl, setAnchorEl] = React.useState(null);
  const [open, setOpen] = React.useState(false);
  const [placement, setPlacement] = React.useState();

  const handleClick = newPlacement => event => {
    setAnchorEl(event.currentTarget);
    setOpen(prev => placement !== newPlacement || !prev);
    setPlacement(newPlacement);
  };

  return (
    <Container>
      <ImageContainer>
        <Image key={tokenId} src={photo} showing={true}>
          <TopIconsContainer>
            <HeartEmpty />
          </TopIconsContainer>
        </Image>
      </ImageContainer>
      <Bottom>
        <PetInfoContainer>
          <Left>
            <ThemeProvider theme={theme}>
              <Gender>
                {gender === "남" ? (
                  <Chip
                    icon={<FaceIcon />}
                    label="남"
                    color="primary"
                    size="small"
                  />
                ) : (
                  <Chip
                    icon={<FaceIcon />}
                    label="여"
                    color="secondary"
                    size="small"
                  />
                )}
              </Gender>
              <Registered>
                <Chip
                  icon={<DoneIcon />}
                  label="등록"
                  clickable
                  deleteIcon={<DoneIcon />}
                  variant="outlined"
                  size="small"
                />
              </Registered>
              <Vaccinated>
                <Chip
                  icon={<DoneIcon />}
                  label="백신"
                  clickable
                  deleteIcon={<DoneIcon />}
                  variant="outlined"
                  size="small"
                />
              </Vaccinated>
            </ThemeProvider>
          </Left>
          <Distance>461m</Distance>
        </PetInfoContainer>
        <Wrapper>
          <Breed>{breed}</Breed>
          <Birth>{birth}</Birth>
        </Wrapper>
        <Avatars>
          <Appliant>현재 6명의 신청자가 있습니다</Appliant>
          <GroupAvatars />
        </Avatars>
      </Bottom>

      {/* <InfoContainer>
        {product && (
          <SliderSet
            targetKlay={targetKlay}
            price={price}
            status={status}
            D_day={D_day}
            tokenId={tokenId}
          />
        )}
      </InfoContainer> */}

      {/* <InfoContainer>
        {product && (
          <SubInfo tokenId={tokenId} D_day={D_day} targetKlay={targetKlay} />
        )}
      </InfoContainer> */}

      <ColoredLine />

      <InfoContainer>
        <MakersDesc tokenId={tokenId} description={description} />
      </InfoContainer>

      {/* {userAddress === "0xb080c3403565f1d4dad3f705796f8f994d1c2105" && (
        <DeleteButton tokenId={tokenId} />
      )} */}

      <OrderButton
        onClick={handleClick("top")}
        userAddress={userAddress}
        tokenId={tokenId}
      />
    </Container>
  );
};

export default MakersProduct;
