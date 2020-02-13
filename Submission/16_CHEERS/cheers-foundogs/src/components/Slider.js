import React from "react";
import Slider from "@material-ui/core/Slider";
import styled from "styled-components";
import { createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";
import { withStyles } from "@material-ui/core/styles";
import { makeStyles } from "@material-ui/core/styles";

const SliderContainer = styled.div`
  width: 93%;
  padding: 0;
  height: 80px;
  /* background-color: ${props => props.theme.lightGrey}; */
`;

const Margin = styled.div`
  height: 30px;
`;

const theme = createMuiTheme({
  palette: {
    primary: { main: "#17202E" },
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

const StyledSlider = withStyles({
  root: {
    color: "#17202E",
    height: 8,
    cursor: "auto",
    pointerEvents: "none"
  },
  thumb: {
    height: 20,
    width: 20
  },
  active: {},
  valueLabel: {
    left: "calc(-50% + 4px)",
    content: " %"
  },
  track: {
    height: 8,
    borderRadius: 4
  },
  rail: {
    height: 8,
    borderRadius: 4
  }
})(Slider);

const useStyles = makeStyles({
  mark: {
    color: "transparent"
  }
});

export default ({ targetKlay, price, status, donate, currentValue }) => {
  const classes = useStyles();
  const marks = [
    {
      value: 0,
      label: "0"
    },
    {
      value: 100,
      label: `${targetKlay} KLAY`
    }
  ];

  const valuetext = value => {
    return `${value}`;
  };

  return (
    <ThemeProvider theme={theme}>
      <SliderContainer>
        <Margin />
        {currentValue && (
          <StyledSlider
            className="PrivateValueLabel"
            defaultValue={currentValue}
            getAriaValueText={valuetext}
            step={price}
            marks={marks}
            valueLabelDisplay="on"
            textColor="secondary"
            classes={{
              mark: classes.mark
            }}
          />
        )}
      </SliderContainer>
    </ThemeProvider>
  );
};
