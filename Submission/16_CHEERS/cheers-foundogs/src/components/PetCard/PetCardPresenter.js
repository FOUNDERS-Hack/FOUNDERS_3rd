import React from "react";
import PropTypes from "prop-types";
import styled from "styled-components";
import { HeartFull, HeartEmpty } from "components/common/Icons";
import GroupAvatars from "components/GroupAvatars";
import { makeStyles } from "@material-ui/core/styles";
import Avatar from "@material-ui/core/Avatar";
import Chip from "@material-ui/core/Chip";
import FaceIcon from "@material-ui/icons/Face";
import DoneIcon from "@material-ui/icons/Done";
import { createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";

const Container = styled.div`
  box-shadow: 0 10px 10px 0 rgba(0, 0, 0, 0.1);
  background-color: white;
  border-radius: 8px;
  height: 200px;
  width: ${props => props.theme.maxCardWidth};
  display: flex;
  & + & {
    margin-top: 1rem;
  }
`;

const Top = styled.div`
  width: 40%;
  padding: 8px;
  display: flex;
  align-items: flex-start;
  border-radius: 8px 0px 0px 8px;
  flex-direction: column;
`;

const BgPhoto = styled.div`
  border-radius: 8px;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center center;
  background-image: linear-gradient(rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.1)),
    url(${props => props.bgPhoto});
`;

const Bottom = styled.div`
  padding: 10px;
  width: 60%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const TopIconsContainer = styled.div`
  display: flex;
  padding: 12px;
`;

const Title = styled.span`
  font-size: 26px;
  font-weight: 700;
  display: block;
  color: #1f2126;
`;

const PetInfoContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
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

const ContentContainer = styled.div`
  padding: 10px;
`;

const Avatars = styled.div`
  display: flex;
  justify-content: flex-end;
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

const useStyles = makeStyles(theme => ({
  root: {
    display: "flex",
    justifyContent: "center",
    flexWrap: "wrap",
    "& > *": {
      margin: theme.spacing(0.5)
    }
  }
}));

const theme = createMuiTheme({
  palette: {
    primary: { main: "#1d3971" },
    secondary: { main: "#fd2eb3" },
    main: { main: "#fbae17" }
  }
});

const PetCardPresenter = ({
  breed,
  description,
  gender,
  bgPhoto,
  tokenId,
  birth,
  centerIcon,
  topIcons
}) => {
  const classes = useStyles();
  return (
    <Container>
      <Top>
        <BgPhoto bgPhoto={bgPhoto}>
          <TopIconsContainer>
            <HeartEmpty />
          </TopIconsContainer>
        </BgPhoto>
      </Top>
      {breed && (
        <Bottom>
          <ContentContainer>
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
              <Title>{breed}</Title>
              <Birth>{birth}</Birth>
            </Wrapper>
            <Avatars>
              <GroupAvatars />
            </Avatars>
          </ContentContainer>
        </Bottom>
      )}
    </Container>
  );
};

PetCardPresenter.propTypes = {
  title: PropTypes.string,
  titleColor: PropTypes.string,
  intro: PropTypes.string,
  introColor: PropTypes.string,
  bgPhoto: PropTypes.string,
  bgColor: PropTypes.string
};

export default PetCardPresenter;
