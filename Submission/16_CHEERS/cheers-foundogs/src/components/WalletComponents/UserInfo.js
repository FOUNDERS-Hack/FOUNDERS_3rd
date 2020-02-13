import React from "react";
import styled from "styled-components";
import AccountCard from "./AccountCard";
import Avatar from "components/Avatar";
import FileCopyIcon from "@material-ui/icons/FileCopy";
import NotificationsNoneIcon from "@material-ui/icons/NotificationsNone";
import { createMuiTheme, withStyles } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";
import { CopyToClipboard } from "react-copy-to-clipboard";
import Tooltip from "@material-ui/core/Tooltip";
import ClickAwayListener from "@material-ui/core/ClickAwayListener";

const Container = styled.div`
  background-color: ${props => props.theme.headerColor};
  width: ${props => props.theme.maxCardWidth};
  height: 360px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const UserName = styled.div`
  color: white;
  font-size: 18px;
  font-weight: 600;
`;
const Wrapper = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
  margin-top: 30px;
`;

const Address = styled.p`
  color: white;
  font-size: 16px;
  margin-top: 40px;
`;

const WalletAddress = styled.p`
  font-size: 13px;
  font-weight: 400;
  color: ${props => props.theme.lightGrey};
  margin-bottom: 5px;
`;

const Span = styled.span`
  margin-left: 10px;
`;

const RightCol = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const CopyContainer = styled.span`
  cursor: pointer;
`;

export default ({ address, balance }) => {
  const theme = createMuiTheme({
    palette: {
      primary: { main: "#ffffff" },
      secondary: { main: "#17202E" }
    },
    typography: {
      fontSize: 18
    }
  });

  const LightTooltip = withStyles(theme => ({
    tooltip: {
      backgroundColor: theme.palette.common.white,
      color: "rgba(0, 0, 0, 0.87)",
      boxShadow: theme.shadows[1],
      fontSize: 11
    }
  }))(Tooltip);

  const [open, setOpen] = React.useState(false);

  const handleTooltipClose = () => {
    setOpen(false);
  };

  const handleTooltipOpen = () => {
    setOpen(true);
  };

  return (
    <ThemeProvider theme={theme}>
      <Container>
        <Wrapper>
          <UserName>username님</UserName>
          <RightCol>
            <NotificationsNoneIcon
              color={"primary"}
              style={{ marginRight: 20 }}
            />
            <Avatar
              size={"md"}
              url={
                "https://s.gravatar.com/avatar/b5f8dac7526f9ef941d739a78a5ed1fb?d=mm"
              }
            />
          </RightCol>
        </Wrapper>
        <Address>
          <WalletAddress>Wallet Address</WalletAddress>
          {address}
          <Span />
          <ClickAwayListener onClickAway={handleTooltipClose}>
            <LightTooltip
              placement="right"
              PopperProps={{
                disablePortal: true
              }}
              onClose={handleTooltipClose}
              open={open}
              disableFocusListener
              disableHoverListener
              disableTouchListener
              title="Copied!"
            >
              <CopyToClipboard text={address}>
                <CopyContainer onClick={handleTooltipOpen}>
                  <FileCopyIcon style={{ fontSize: 20 }} />
                </CopyContainer>
              </CopyToClipboard>
            </LightTooltip>
          </ClickAwayListener>
        </Address>
        <AccountCard address={address} balance={balance} />
      </Container>
    </ThemeProvider>
  );
};
