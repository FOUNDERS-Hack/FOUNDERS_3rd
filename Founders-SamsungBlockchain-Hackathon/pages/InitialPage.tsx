import PageLayout from "../layouts/PageLayout";
import Initial from "../components/InitialPage/Initial";
import NonAuthUserHeader from "../components/UserHeader/NoAuthUserHeader";
import NavTopHeader from "../components/Common/NavTopHeader";
import NavBottomFooter from "../components/Common/NavBottomFooter";

const InitialPage = () => (
  <PageLayout headerTitle="최초페이지 텍스트" isBack={false}>
    {/* <NavTopHeader title="" /> */}
    <NonAuthUserHeader />
    <Initial />
    {/* <NavBottomFooter /> */}
  </PageLayout>
);
export default InitialPage;
