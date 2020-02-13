import React from "react";
import styled from "styled-components";
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn } from "semantic-ui-react";

const HeaderTitle = styled.span`
  font-size: 17px;
  font-weight: 600;
  color: #30455f;
`;

const SubText = styled.span`
  color: #586e89;
  font-size: 10px;
  font-weight: 300;
`;

interface ICardMetaProps {
  title?: string;
  description?: string;
}

const CardHeader = (props: ICardMetaProps) => (
  <Card.Header>
    <HeaderTitle>{props.title + " "}</HeaderTitle>
    <SubText>{props.description}</SubText>
  </Card.Header>
);

export default CardHeader;
