import PlainPageLayout from "../layouts/PlainPageLayout"
import RegisterFour from "../components/RegisterPage/RegisterFour"
import UserHeader from "../components/UserHeader/UserHeader"
import NavTopHeader from "../components/Common/NavTopHeader"
import NavBottomFooter from "../components/Common/NavBottomFooter"

const RegisterFourthPage = () => (
  <PlainPageLayout headerTitle="반려견 등록" isBack>
    <RegisterFour />
  </PlainPageLayout>
);
export default RegisterFourthPage;
