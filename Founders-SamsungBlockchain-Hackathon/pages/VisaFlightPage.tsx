import PlainPageLayout from "../layouts/PlainPageLayout"
import RegisterTwo from "../components/RegisterPage/RegisterTwo"
import Flight from "../components/Visa/Flight"
import NavTopHeader from "../components/Common/NavTopHeader"
import NavBottomFooter from "../components/Common/NavBottomFooter"

const VisaFlightPage = () => (
  <PlainPageLayout headerTitle="반려견 등록" isBack>
    <Flight />
  </PlainPageLayout>
);
export default VisaFlightPage;
