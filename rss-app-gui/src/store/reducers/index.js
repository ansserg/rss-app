import { combineReducers } from "redux";
import xlsDataReducer from "./xlsDataReducer";
import callErrReducer from "./callErrReducer";
import userNameReducer from "./userNameReducer";
import appReducer from "./appReducer";
import rapReducer from "./rapReducer";

export default combineReducers({
    xlsDataReducer,
    callErrReducer,
    userNameReducer,
    appReducer,
    rapReducer,
});
