import PageLayout from "../layouts/PageLayout";
import Main from "../components/MainPage/Main";
import UserHeader from "../components/UserHeader/UserHeader";
import NavTopHeader from "../components/Common/NavTopHeader";
import NavBottomFooter from "../components/Common/NavBottomFooter";

const MainPage = () => (
  <PageLayout headerTitle="메인페이지 텍스트" isBack={false}>
    {/* <NavTopHeader title="" /> */}
    <Main />
    {/* <NavBottomFooter /> */}
  </PageLayout>
);
export default MainPage;
