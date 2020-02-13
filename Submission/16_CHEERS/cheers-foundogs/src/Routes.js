import React from "react";
import PropTypes from "prop-types";
import { Route, Switch, Redirect } from "react-router-dom";
import Auth from "pages/Auth";
import Makers from "pages/Makers";
import Wallet from "pages/Wallet";
import MakersDetail from "pages/MakersDetail";
import Test from "pages/Test";
import ScrollToTop from "components/ScrollToTop";

const LoggedInRoutes = () => (
  <ScrollToTop>
    <Switch>
      <Route exact path="/" component={Makers} />
      <Route path="/makers/:tokenId" component={MakersDetail} />
      <Route path="/makers" component={Makers} />
      <Route path="/wallet" component={Wallet} />
      <Route path="/test" component={Test} />
      <Redirect from="*" to="/" />
    </Switch>
  </ScrollToTop>
);

const LoggedOutRoutes = () => (
  <Switch>
    <Route exact path="/" component={Auth} />
    <Redirect from="*" to="/" />
  </Switch>
);

const AppRouter = ({ isLoggedIn }) =>
  isLoggedIn ? <LoggedInRoutes /> : <LoggedOutRoutes />;

AppRouter.propTypes = {
  isLoggedIn: PropTypes.bool.isRequired
};

export default AppRouter;
