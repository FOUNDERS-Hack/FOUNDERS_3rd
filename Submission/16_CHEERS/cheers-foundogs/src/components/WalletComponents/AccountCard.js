import React, { Component } from "react";
import styled from "styled-components";
import CardGiftcardRoundedIcon from "@material-ui/icons/CardGiftcardRounded";
import CachedIcon from "@material-ui/icons/Cached";
import CreditCardIcon from "@material-ui/icons/CreditCard";
import EcoTokenContract from "klaytn/EcoTokenContract";

const Container = styled.div`
  width: 540px;
  height: 300px;
  display: flex;
  position: relative;
  top: 60px;
  ${props => props.theme.whiteBox};
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const KlayBalance = styled.div`
  height: 50px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 90px;
  padding-left: 75px;
  padding-right: 75px;
`;

const Span = styled.span`
  font-size: 18px;
  font-weight: 600;
`;

const EcoData = styled.div`
  height: 220px;
  width: 95%;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
`;

const Item = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const IconContainer = styled.div`
  font-size: 40px;
  cursor: pointer;
`;

const IconDescContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 15px;
`;

const IconDesc = styled.div`
  cursor: pointer;
`;

const GreenText = styled.span`
  color: ${props => props.theme.lightGreen};
  font-size: 28px;
`;

class AccountCard extends Component {
  state = {
    ecoPower: 0
  };
  _showMyToken = addressId => {
    EcoTokenContract.methods
      .balanceOf(addressId)
      .call()
      .then(result => {
        console.log("총 보유 Eco Token : ", result);
        this.setState({
          ecoPower: result
        });
      });
  };

  constructor(props) {
    super(props);
    const { address } = props;
    this._showMyToken(address);
  }

  render() {
    const { balance } = this.props;
    const balanceFloor = Math.floor(balance * 10000) / 10000;

    return (
      <Container>
        <KlayBalance>
          <Span>KLAY Balance</Span>
          <Span>
            <GreenText>{balanceFloor} </GreenText> KLAY
          </Span>
        </KlayBalance>
        <EcoData>
          <Item>
            <IconContainer>
              <CardGiftcardRoundedIcon style={{ fontSize: 40 }} />
            </IconContainer>
            <IconDescContainer>
              <IconDesc>Kit</IconDesc>
            </IconDescContainer>
          </Item>
          <Item>
            <IconContainer>
              <CreditCardIcon style={{ fontSize: 40 }} />
            </IconContainer>
            <IconDescContainer>
              <IconDesc>Card</IconDesc>
            </IconDescContainer>
          </Item>
          <Item>
            <IconContainer>
              <CachedIcon style={{ fontSize: 40 }} />
            </IconContainer>
            <IconDescContainer>
              <IconDesc>Reward</IconDesc>
            </IconDescContainer>
          </Item>
        </EcoData>
      </Container>
    );
  }
}

export default AccountCard;
