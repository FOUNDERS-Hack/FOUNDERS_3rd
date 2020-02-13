import PlainPageLayout from "../layouts/PlainPageLayout"
import Register from "../components/RegisterPage/Register"
import UserHeader from "../components/UserHeader/UserHeader"
import NavTopHeader from "../components/Common/NavTopHeader"
import NavBottomFooter from "../components/Common/NavBottomFooter"

const RegisterPage = () => (
  <PlainPageLayout headerTitle="반려견 등록" isBack>
    <Register />
  </PlainPageLayout>
);
export default RegisterPage;
