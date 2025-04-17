const initialState = {
    procData: [],
    errData: [],
    filteredData: [],
    selData: [],
}

const callErrReducer = (state = initialState, action) => {
    switch (action.type) {
        case "SET_CALL_ERR":
            return {
                ...state,
                ...action.playload,
            };
        case "CLEAR_ERR_DATA":
            return {
                ...state,
                ...initialState,
            }
        case "CLEAR_FILTERED_ERR_DATA":
            return {
                ...state,
                filteredData: [],
            }
        case "CLEAR_SELECTED_ERR_DATA":
            return {
                ...state,
                selData: [],
            }
        default:
            return state;
    }
};

export default callErrReducer;