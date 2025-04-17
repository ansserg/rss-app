const initialState = {
    auth:false,
    userName:null,
}

const userNameReducer = (state = initialState, action) => {
    switch (action.type) {
        case "SET_USER_NAME":
            // console.log("SET_USER_NAME..",action.playload)
            return {
                ...state,
                ...action.playload,
            };
        default:
            return state;
    }
};

export default userNameReducer;