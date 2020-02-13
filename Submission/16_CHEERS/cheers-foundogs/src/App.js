import React, { Component } from "react";
import { connect } from "react-redux";
import styled, { ThemeProvider } from "styled-components";
import { BrowserRouter as Router } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import GlobalStyles from "./styles/GlobalStyles";
import Theme from "./styles/Theme";
import Routes from "./Routes";
import Modal from "components/Modal";

import * as authActions from "./redux/actions/auth";

const Wrapper = styled.div`
  margin: 0 auto;
  max-width: ${props => props.theme.maxWidth};
  width: 100%;
`;

class App extends Component {
  constructor(props) {
    super(props);
    /**
     * sessionStorage is internet browser's feature which stores data
     * until the browser tab is closed.
     */
    const walletFromSession = sessionStorage.getItem("walletInstance");
    const { integrateWallet, removeWallet } = this.props;

    if (walletFromSession) {
      try {
        /**
         * 1. If 'walletInstance' value exists,
         * add it to caver's wallet and it's information to store
         * cf) redux/actions/auth.js -> integrateWallet()
         */
        integrateWallet(JSON.parse(walletFromSession).privateKey);
      } catch (e) {
        /**
         * 2. If value in sessionStorage is invalid wallet instance,
         * remove it from caver's wallet and it's information from store
         * cf) redux/actions/auth.js -> removeWallet()
         */
        removeWallet();
      }
    }
  }
  render() {
    const { isLoggedIn } = this.props;
    return (
      <ThemeProvider theme={Theme}>
        <>
          <GlobalStyles />
          <Router>
            <>
              <Wrapper>
                <Modal />
                <Routes isLoggedIn={isLoggedIn} />
              </Wrapper>
            </>
          </Router>
          <ToastContainer position={toast.POSITION.TOP_CENTER} />
        </>
      </ThemeProvider>
    );
  }
}

const mapStateToProps = state => ({
  isLoggedIn: state.auth.isLoggedIn
});

const mapDispatchToProps = dispatch => ({
  integrateWallet: privateKey =>
    dispatch(authActions.integrateWallet(privateKey)),
  removeWallet: () => dispatch(authActions.removeWallet())
});

export default connect(mapStateToProps, mapDispatchToProps)(App);
