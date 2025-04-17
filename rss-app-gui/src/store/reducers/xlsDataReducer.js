const initialState = {
    task: -1,
    data: null,
}

const xlsDataReducer = (state = initialState, action) => {
    switch (action.type) {
        case "SET_VALUE":
            return {
                ...state,
                ...action.playload,
            };
        case "INIT_VALUE":
            return {
                ...state,
                ...initialState,
            }
        default:
            return state;
    }
};

export default xlsDataReducer;