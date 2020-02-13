import { combineReducers } from "redux";
import { routerReducer } from "react-router-redux";
import auth from "./auth";
import ui from "./ui";
import makers from "./makers";

const reducer = combineReducers({
  routing: routerReducer,
  auth,
  ui,
  makers
});

export default reducer;
