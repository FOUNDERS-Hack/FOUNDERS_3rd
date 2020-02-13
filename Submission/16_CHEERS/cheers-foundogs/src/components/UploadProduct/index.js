import { connect } from "react-redux";
import Container from "./container";
import * as makersActions from "../../redux/actions/makers";

const mapDispatchToProps = dispatch => ({
  uploadItem: (filePath, title, description, targetKlay, D_day, price) =>
    dispatch(
      makersActions.uploadItem(
        filePath,
        title,
        description,
        targetKlay,
        D_day,
        price
      )
    )
});

export default connect(null, mapDispatchToProps)(Container);
