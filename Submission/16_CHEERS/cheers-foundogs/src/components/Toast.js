import React, { Component } from "react";
import cx from "classnames";
import { connect } from "react-redux";
import { KLAYTN_SCOPE } from "constants/url";
import ui from "utils/ui";
import LinkNewTab from "components/LinkNewTab";
import IconButton from "components/IconButton";
import styled from "styled-components";

const Container = styled.div`
  position: fixed;
  top: 100px;
  right: 30px;
  width: 400px;
  padding: 22px 24px;
  z-index: 10;
  border-radius: 3px;
  color: white;
  background-color: rgba(79, 71, 62, 0.9);
  opacity: 0;
  animation: showToast 10s;
`;

const getStatus = status => {
  let backgroundColor;
  let color;
  if (status === "pending") {
    backgroundColor = "#e1e1e1";
  } else if (status === "fail") {
  } else if (status === "error") {
    backgroundColor = "red";
    color = "white";
  }

  return `
        background-color: ${backgroundColor};
        color: ${color};
    `;
};

const Status = styled.div`
  ${props => getStatus(props.status)}

  display: inline-block;
  padding: 3px 5px;
  margin-bottom: 8px;
  font-size: 10px;
  font-weight: bold;
  line-height: 1;
  border-radius: 3px;
  color: ${props => props.theme.darkBrown};
`;

const Message = styled.p`
  font-size: 14px;
  color: ${props => props.theme.white};
  margin-bottom: 10px;
`;

const TxHash = styled(LinkNewTab)`
  display: block;
  position: relative;
  width: 100%;
  font-size: 12px;
  line-height: 1;
  padding-left: 15px;
  color: ${props => props.theme.white};
  overflow: hidden;
  text-overflow: ellipsis;
  opacity: 0.6;

  &::before {
    content: "---";
    display: inline-block;
    position: absolute;
    top: 0;
    left: 0;
    width: 10px;
    height: 12px;
    background-color: ${props => props.theme.white};
    border-radius: 1px;
    line-height: 3px;
    color: ${props => props.theme.black};
  }

  &:link {
    text-decoration: underline;
  }
  /* &--link: hover {
    opacity: 1;
  } */
`;

const Close = styled(IconButton)`
  position: absolute;
  top: 15px;
  right: 15px;
  width: 20px;
  height: 20px;

  @keyframes showToast {
    0% {
      opacity: 1;
      visibility: visible;
    }

    10% {
      opacity: 1;
    }

    80% {
      opacity: 1;
    }

    100% {
      opacity: 0;
      visibility: hidden;
    }
  }
`;

class Toast extends Component {
  render() {
    const { toast } = this.props;
    console.log(toast);
    return (
      toast && (
        <Container key={new Date().getTime()}>
          <Status status={toast.status}>{toast.status}</Status>
          <Message>{toast.message}</Message>
          {toast.txHash && <TxHash>{toast.txHash}</TxHash>}
          {toast.link && (
            <LinkNewTab
              className="Toast__txHash Toast__txHash--link"
              link={`${KLAYTN_SCOPE}tx/${toast.link}`}
              title={`${toast.link}`}
            />
          )}
          <Close
            className="Toast__close"
            icon="icon-close.png"
            alt="close toast"
            onClick={ui.hideToast}
          />
        </Container>
      )
    );
  }
}

const mapStateToProps = state => ({
  toast: state.ui.toast
});

export default connect(mapStateToProps)(Toast);
