const initialState = {
    rapData: [],
    filteredData:[],
    selData:[],
}

    const rapReducer = (state = initialState, action) => {
        switch (action.type) {
            case "SET_RAP":
                return {
                    ...state,
                    ...action.playload,
                };
                case "CLEAR_RAP":
                    return {
                        ...state,
                        ...initialState,
                    }
            default:
                return state;
        }
    };

export default rapReducer;