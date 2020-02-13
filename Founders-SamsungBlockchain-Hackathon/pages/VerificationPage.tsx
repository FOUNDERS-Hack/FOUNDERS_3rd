import PlainPageLayout from "../layouts/PlainPageLayout"
import Verification from "../components/VerificationPage/Verification"
import UserHeader from "../components/UserHeader/UserHeader"
import NavTopHeader from "../components/Common/NavTopHeader"
import NavBottomFooter from "../components/Common/NavBottomFooter"

const VerificationPage = () => (
  <PlainPageLayout headerTitle="신분인증" isBack>
    <Verification />
  </PlainPageLayout>
);
export default VerificationPage;
