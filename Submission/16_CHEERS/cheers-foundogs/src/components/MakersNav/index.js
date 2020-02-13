import { connect } from "react-redux";
import Container from "./container";
import * as makersActions from "redux/actions/makers";

const mapStateToProps = state => ({
  feed: state.makers.feed,
  targetKlay: state.makers.targetKlay,
  userAddress: state.auth.address
});

const mapDispatchToProps = dispatch => ({
  getFeed: () => dispatch(makersActions.getFeed())
});

export default connect(mapStateToProps, mapDispatchToProps)(Container);
