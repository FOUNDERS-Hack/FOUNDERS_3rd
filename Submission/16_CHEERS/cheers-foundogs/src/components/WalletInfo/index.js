import { connect } from "react-redux";
import Container from "./container";
import * as authActions from "redux/actions/auth";

const mapStateToProps = state => ({
  address: state.auth.address
});

const mapDispatchToProps = dispatch => ({
  logout: () => dispatch(authActions.logout())
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Container);
