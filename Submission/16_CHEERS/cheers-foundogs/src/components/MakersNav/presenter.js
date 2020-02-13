import React, { useState } from "react";
import PropTypes from "prop-types";
import { makeStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Paper from "@material-ui/core/Paper";
import styled from "styled-components";
import { createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";
import { blueGrey } from "@material-ui/core/colors";
import MakersFeed from "components/MakersFeed";

const Root = styled(Paper)`
  flex-grow: 0;
  min-height: 100%;
  background-color: ${props => props.theme.bgColor};
  box-shadow: none;
`;

const StyledTabs = styled(Tabs)`
  width: ${props => props.theme.maxCardWidth};
  position: fixed;
  z-index: 1;
  background-color: ${props => props.theme.bgColor};
`;

const StyledTab = styled(Tab)`
  width: 30%;
`;

const StyledBox = styled(Box)`
  background-color: ${props => props.theme.bgColor};
`;

const StyledTypo = styled(Typography)``;

const useStyles = makeStyles(theme => ({
  root: {
    zIndex: 1
  },
  elevation1: {
    boxShadow: "none"
  }
}));

function TabPanel(props) {
  const { children, value, index, theme, ...other } = props;

  return (
    <ThemeProvider theme={theme}>
      <StyledTypo
        classes="root"
        component="div"
        role="tabpanel"
        hidden={value !== index}
        id={`simple-tabpanel-${index}`}
        aria-labelledby={`simple-tab-${index}`}
        {...other}
      >
        {/* <StyledBox p={20}>{children}</StyledBox> */}
        <StyledBox p={3}>{children}</StyledBox>
      </StyledTypo>
    </ThemeProvider>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    "aria-controls": `simple-tabpanel-${index}`
  };
}

export default props => {
  const classes = useStyles();
  const [value, setValue] = useState(0);

  const { feed } = props;

  const homeFeed = [];
  const FinishedFeed = [];

  feed &&
    feed.map(item => {
      if (item.status === "1") {
        homeFeed.push(item);
      } else if (item.status === "0") {
        FinishedFeed.push(item);
      }
      return null;
    });

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const theme = createMuiTheme({
    palette: {
      primary: { main: blueGrey[900] },
      secondary: { main: "#17202E" }
    },
    typography: {
      fontFamily: [
        "-apple-system",
        "Noto Sans KR",
        "BlinkMacSystemFont",
        '"Segoe UI"',
        "Roboto",
        '"Helvetica Neue"',
        "Arial",
        "sans-serif",
        '"Apple Color Emoji"',
        '"Segoe UI Emoji"',
        '"Segoe UI Symbol"'
      ].join(","),
      fontSize: 16,
      fontWeight: 700
    }
  });

  return (
    <ThemeProvider theme={theme}>
      <Root
        classes={{
          elevation1: classes.elevation1
        }}
      >
        <AppBar
          position="relative"
          classes={{
            root: classes.root
          }}
        >
          <StyledTabs
            value={value}
            onChange={handleChange}
            indicatorColor="secondary"
            textColor="secondary"
            centered
          >
            <StyledTab label="홈" {...a11yProps(0)} />
            <StyledTab label="상점" {...a11yProps(1)} />
            <StyledTab label="MY" {...a11yProps(2)} />
          </StyledTabs>
        </AppBar>
        <TabPanel value={value} index={0} theme={theme}>
          <MakersFeed tabFeed={feed} />
        </TabPanel>
        <TabPanel value={value} index={1} theme={theme}>
          <MakersFeed tabFeed={feed} />
        </TabPanel>
        <TabPanel value={value} index={2} theme={theme}>
          <MakersFeed tabFeed={feed} />
        </TabPanel>
      </Root>
    </ThemeProvider>
  );
};
