import PlainPageLayout from "../layouts/PlainPageLayout"
import RegisterTwo from "../components/RegisterPage/RegisterTwo"
import UserHeader from "../components/UserHeader/UserHeader"
import NavTopHeader from "../components/Common/NavTopHeader"
import NavBottomFooter from "../components/Common/NavBottomFooter"

const RegisterSecondPage = () => (
  <PlainPageLayout headerTitle="반려견 등록" isBack>
    <RegisterTwo />
  </PlainPageLayout>
);
export default RegisterSecondPage;
