import React from "react";
import Input from "components/Input";
import Button from "components/Button";
import styled from "styled-components";
import TextareaAutosize from "react-autosize-textarea";
import "date-fns";
import Grid from "@material-ui/core/Grid";
import DateFnsUtils from "@date-io/date-fns";
import {
  MuiPickersUtilsProvider,
  KeyboardDatePicker
} from "@material-ui/pickers";
import { makeStyles } from "@material-ui/core/styles";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";

const StyledKeyboardDatePicker = styled(KeyboardDatePicker)`
  font-weight: 500;
  width: 100%;
`;

const Container = styled.div``;

const Form = styled.form``;

const StyledButton = styled(Button)`
  background-color: ${props => props.theme.mainColor};
  margin-top: 30px;
`;

const Textarea = styled(TextareaAutosize)`
  border: 0;
  border: ${props => props.theme.boxBorder};
  border-radius: ${props => props.theme.borderRadius};
  background-color: ${props => props.theme.bgColor};
  min-height: 150px;
  max-height: 200px;
  font-size: 15px;
  padding: 15px;
  width: 100%;
  margin-bottom: 30px;
  resize: none;
  &:focus {
    outline: none;
  }
  display: block;
  color: ${props => props.theme.black};
  @import url("https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap");
  font-family: -apple-system, "Noto Sans KR", sans-serif, BlinkMacSystemFont,
    "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans";
`;

const DateWrapper = styled.div`
  margin-bottom: 30px;
`;

const Wrapper = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  margin-left: -8px;
`;

const Gender = styled.div`
  width: 100%;
  margin-bottom: 10px;
`;

const Breed = styled.div`
  margin-bottom: 10px;
  width: 90%;
`;

const useStyles = makeStyles(theme => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
    width: 250
  },
  selectEmpty: {
    marginTop: theme.spacing(2)
  }
}));

const UploadProduct = ({
  handleSubmit,
  breed,
  gender,
  serialNum,
  photo,
  description,
  handleDateChange,
  selectedDate,
  handleChange,
  handleChangeBreed
}) => {
  const classes = useStyles();

  return (
    <Container>
      <Form>
        <Wrapper>
          <Breed>
            <FormControl className={classes.formControl}>
              <InputLabel
                shrink
                id="demo-simple-select-placeholder-label-label"
              >
                품종
              </InputLabel>
              <Select
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={breed}
                onChange={handleChangeBreed}
              >
                <MenuItem value={"불독"}>불독</MenuItem>
                <MenuItem value={"치와와"}>치와와</MenuItem>
                <MenuItem value={"푸들"}>푸들</MenuItem>
                <MenuItem value={"닥스훈트"}>닥스훈트</MenuItem>
                <MenuItem value={"믹스견"}>믹스견</MenuItem>
                <MenuItem value={"포메라니안"}>포메라니안</MenuItem>
                <MenuItem value={"시바견"}>시바견</MenuItem>
                <MenuItem value={"말티즈"}>말티즈</MenuItem>
                <MenuItem value={"비숑"}>비숑</MenuItem>
                <MenuItem value={"웰시코기"}>웰시코기</MenuItem>
                <MenuItem value={"시츄"}>시츄</MenuItem>
                <MenuItem value={"닥스훈트"}>닥스훈트</MenuItem>
              </Select>
            </FormControl>
          </Breed>

          {/* <PriceWrapper>
          <KlayInput label="상품 가격" placeholder={"0"} {...price} />
          <Klay>KLAY</Klay>
        </PriceWrapper> */}
          <Gender>
            <FormControl className={classes.formControl}>
              <InputLabel
                shrink
                id="demo-simple-select-placeholder-label-label"
              >
                성별
              </InputLabel>
              <Select
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={gender}
                onChange={handleChange}
              >
                <MenuItem value={"남"}>남</MenuItem>
                <MenuItem value={"여"}>여</MenuItem>
              </Select>
            </FormControl>
          </Gender>
        </Wrapper>
        <DateWrapper>
          <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Grid container justify="left">
              <StyledKeyboardDatePicker
                className="D_day"
                disableToolbar
                format="yyyy년 MM월 dd일"
                margin="normal"
                id="date-picker-dialog"
                label="강아지 생년월일"
                value={selectedDate}
                onChange={handleDateChange}
                KeyboardButtonProps={{
                  "aria-label": "change date"
                }}
              />
            </Grid>
          </MuiPickersUtilsProvider>
        </DateWrapper>
        <Input
          label="동물등록번호"
          placeholder={"동물등록번호"}
          {...serialNum}
        />
        <Input label="image" placeholder={"image"} {...photo} />
        <Textarea placeholder={"상세 설명"} {...description} />

        {/* <InputFile
          className="UploadPhoto__file"
          name="file"
          label="Search file"
          fileName={isCompressing ? "Compressing image..." : fileName}
          onChange={handleFileChange}
          accept=".png, .jpg, .jpeg"
          required
        /> */}
        <StyledButton onClick={handleSubmit}>Upload</StyledButton>
      </Form>
    </Container>
  );
};
export default UploadProduct;
