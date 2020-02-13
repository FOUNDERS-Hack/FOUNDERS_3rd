// import HomePage from "./HomePage"

// const Index = () => (
//   <HomePage />
// );

// export default Index;
import PageLayout from "../layouts/PageLayout";
import Main from "../components/MainPage/Main";
import NonAuthPageLayout from "../layouts/NonAuthPageLayout";
import Initial from "../components/InitialPage/Initial";
import NonAuthUserHeader from "../components/UserHeader/NoAuthUserHeader";
import NavTopHeader from "../components/Common/NavTopHeader";
import NavBottomFooter from "../components/Common/NavBottomFooter";

const Index = () => (
  /* 
  ---------------
  로그인 사용 시에 주석풀고 수정해야할 부분
  ---------------
  */
  // <NonAuthPageLayout headerTitle="로그인화면" isBack={false}>
  //   {/* <NavTopHeader title="" /> */}
  //   {/* <NonAuthUserHeader /> */}
  //   <Initial />
  //   {/* <NavBottomFooter /> */}
  // </NonAuthPageLayout>
  /* 
  ---------------
  로그인 사용 시에 주석풀고 수정해야할 부분
  ---------------
  */

  <PageLayout headerTitle="투표" isBack={false}>
    {/* <NavTopHeader title="" /> */}
    <Main />
    {/* <NavBottomFooter /> */}
  </PageLayout>
);
export default Index;
