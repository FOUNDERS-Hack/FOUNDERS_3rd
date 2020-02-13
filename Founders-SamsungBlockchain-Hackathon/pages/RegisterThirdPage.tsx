import PlainPageLayout from "../layouts/PlainPageLayout"
import RegisterThree from "../components/RegisterPage/RegisterThree"
import UserHeader from "../components/UserHeader/UserHeader"
import NavTopHeader from "../components/Common/NavTopHeader"
import NavBottomFooter from "../components/Common/NavBottomFooter"

const RegisterThirdPage = () => (
  <PlainPageLayout headerTitle="반려견 등록" isBack>
    <RegisterThree />
  </PlainPageLayout>
);
export default RegisterThirdPage;
