import PlainPageLayout from "../layouts/PlainPageLayout";
import Result from "../components/Card/Result";
import React from "react";

const VoteResultPage = () => (
  <PlainPageLayout headerTitle="투표현황" isBack>
    <Result />
  </PlainPageLayout>
);
export default VoteResultPage;
