import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn, SemanticICONS } from "semantic-ui-react";

const SubTitle = styled.span`
  color: #586e89;
  font-size: 10px;
  font-weight: 600;
  margin-right: 1em !important;
`;

const SubText = styled.span`
  color: #586e89;
  font-size: 12px;
  font-weight: 300;
`;

const StyledCardMeta = styled(Card.Meta)`
  display: flex;
  align-items: center;
`;
interface ICardMetaProps {
  title?: string;
  description?: string;
  icon?: SemanticICONS;
}

const CardMeta = (props: ICardMetaProps) => (
  <StyledCardMeta>
    {/* <SubTitle>{props.title}</SubTitle> */}
    <Icon name={props.icon}></Icon>
    <SubText>{props.description}</SubText>
  </StyledCardMeta>
);

export default CardMeta;
