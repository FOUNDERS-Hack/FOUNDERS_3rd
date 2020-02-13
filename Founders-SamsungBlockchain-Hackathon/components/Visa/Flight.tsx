import React from 'react'
import styled from 'styled-components';
import { Form, Button } from 'semantic-ui-react'
import Link from 'next/link'

const MainText = styled.div`
  font-size: 18px;
  font-weight: 300 !important;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.33;
  letter-spacing: normal;
  color: #2e384d;
  padding-bottom: 12px !important;
  text-align: left !important;
`

const StyledDiv = styled.div`
  height: 100% !important;
  padding-top: 45px;
  -webkit-tap-highlight-color: transparent;

`
const LeftDiv = styled.div`
width: 312px;
float: left;
`
const StyledFormField = styled(Form.Field)`
  margin-bottom: 40px !important;
`

const SubmitButton = styled(Button)`
  width: 312px;
  height: 50px;
  border-radius: 4px;
  background-color: #2e5bff !important;
  color: #ffffff !important;
`

const BoldText = styled.b`
  font-weight: 600;
`

const TextDiv = styled.div` 
  display: flex;
  padding-bottom: 20px !important;
`

const BoldSubText = styled.span`
  font-family: 'AppleSDGothicNeo';
  font-size: 14px;
  font-weight: bold;
  font-stretch: normal;
  font-style: normal;
  line-height: 0.86;
  letter-spacing: normal;
  color: #8798ad;
  margin-top: 194px !important;
`

const NormalSubText = styled(BoldSubText)`
  font-weight: normal;
`

const Flight = () => (
  <StyledDiv>
    <LeftDiv>
      <TextDiv>
        <MainText><BoldText>돌아오는 항공편</BoldText>의 <br />번호를 입력해주세요.</MainText>
      </TextDiv>
      <Form>
        <StyledFormField>
          <input type="text" placeholder='항공편명 입력' />
        </StyledFormField>
      </Form>
      <div style={{ marginTop: '297px' }}>
        <NormalSubText><BoldSubText>혹시 편도를 이용하시나요?</BoldSubText></NormalSubText>
      </div>
      <Link href="/VisaCompletePage">
        <SubmitButton type="submit" style={{ marginTop: '24px' }}>다음</SubmitButton>
      </Link>
    </LeftDiv>
  </StyledDiv>
);

export default Flight;