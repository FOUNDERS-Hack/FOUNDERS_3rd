import React from "react";
import styled from "styled-components";
import { withStyles } from "@material-ui/core/styles";
import Switch from "@material-ui/core/Switch";
import Collapse from "@material-ui/core/Collapse";
import FormControlLabel from "@material-ui/core/FormControlLabel";

const StyledSwitch = withStyles({
  root: {
    display: "none",
    marginTop: "20px"
  }
})(Switch);

const Container = styled.div`
  display: flex;
  width: ${props => props.theme.maxCardWidth};
  justify-content: center;
`;

const Description = styled.p`
  width: 90%;
  font-size: 16px;
  margin-bottom: 18px;
  font-weight: 100;
  line-height: 160%;
`;

export default ({ description }) => {
  const [checked, setChecked] = React.useState(false);

  const handleChange = () => {
    setChecked(prev => !prev);
  };

  return (
    <>
      <Collapse in={checked} collapsedHeight="138px">
        <Container>
          <Description>
            {description}
            {description}
            {description}
            {description}
            {description}
            {description}
          </Description>
        </Container>
      </Collapse>

      {checked ? (
        <FormControlLabel
          control={
            <StyledSwitch
              color="default"
              checked={checked}
              onChange={handleChange}
              label="Normal"
            />
          }
          labelPlacement="top"
          label="▲ 접기"
        />
      ) : (
        <FormControlLabel
          control={
            <StyledSwitch
              color="default"
              checked={checked}
              onChange={handleChange}
              label="Normal"
            />
          }
          labelPlacement="top"
          label="▼ 더 보기"
        />
      )}
    </>
  );
};
