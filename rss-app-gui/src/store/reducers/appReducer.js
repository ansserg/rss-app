import { Navigate } from "react-router-dom";
import { DB_UNDEF } from "../../constants/api";

const initialState={
    dataBaseName:DB_UNDEF,
}

const appReducer=(state=initialState,action)=>{
    switch(action.type) {
        case "SET_DB_NAME":
            return {
                ...state,
                ...action.playload,
            }
            default:
                return state;
    }
}

export default appReducer;
