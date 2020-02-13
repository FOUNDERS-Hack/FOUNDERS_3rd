import React from 'react'
import styled from 'styled-components';
import { Grid, Card, Icon, Image, Button, Divider, Segment, GridColumn } from 'semantic-ui-react'
import CardOne from "../Card/CardOne"
import AddCard from "../Card/AddCard"
import UserHeader from "../UserHeader/UserHeader"
import NavTopHeader from "../Common/NavTopHeader"
import Link from 'next/link'

const StyledGrid = styled(Grid)`
  text-align: center;
  vertical-align: middle;
  height: 575px;
  max-height: 640px !important;
  overflow-y: scroll;
  -webkit-overflow-scrolling: touch;
  &::-webkit-scrollbar {
    display: none;
  }
`

const StyledGridColumn = styled(Grid.Column)`
  max-width: 450px;
`
const Initial = () => (
  <StyledGrid>
    <StyledGridColumn>
      <AddCard isAuth={false} />
    </StyledGridColumn>
  </StyledGrid>
)

export default Initial;