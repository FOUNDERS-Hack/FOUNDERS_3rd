import PlainPageLayout from "../layouts/PlainPageLayout";
import RegisterTwo from "../components/RegisterPage/RegisterTwo";
import Complete from "../components/Visa/Complete";
import NavTopHeader from "../components/Common/NavTopHeader";
import NavBottomFooter from "../components/Common/NavBottomFooter";
import styled from "styled-components";
import VoteCompleted from "../components/Card/VoteCompleted";

const StyledDiv = styled.div`
  height: 100% !important;
  padding-top: 45px;
  -webkit-tap-highlight-color: transparent;
`;

const VisaCompletePage = () => (
  <PlainPageLayout headerTitle="투표완료">
    <StyledDiv>
      <VoteCompleted></VoteCompleted>
    </StyledDiv>
  </PlainPageLayout>
);
export default VisaCompletePage;
