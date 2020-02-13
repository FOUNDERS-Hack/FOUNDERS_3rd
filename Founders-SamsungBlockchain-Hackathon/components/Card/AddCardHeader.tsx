import React from 'react'
import styled from 'styled-components';
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn } from 'semantic-ui-react'

const HeaderTitle = styled.span`
  font-size: 15px;
  font-weight: 600;
  color: #586e89;
`

const StyledIcon = styled(Icon)`
  width: 16px;
  height: 16px;
  color: #2e5bff;
  margin-right: 8px !important;
`

interface IAddCardHeaderProps {
  title?: string;
  description?: string;
}

const AddCardHeader = (props: IAddCardHeaderProps) => (
  <Card.Header>
    <StyledIcon size="small" name="plus"></StyledIcon>
    <HeaderTitle>{props.title}</HeaderTitle>
  </Card.Header>
)

export default AddCardHeader;