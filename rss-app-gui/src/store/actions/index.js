export const setValue = (value) => {
    return ({
        type: "SET_VALUE",
        playload: value,
    })
};

export const setCallErr = (value) => {
    return ({
        type: "SET_CALL_ERR",
        playload: value,
    })
};

export const clearErrData = () => {
    return ({
        type: "CLEAR_ERR_DATA",
    })
}

export const clearFilteredErrData = () => {
    return ({
        type: "CLEAR_FILTERED_ERR_DATA",
    })
}

export const clearSelectedErrData = () => {
    return ({
        type: "CLEAR_SELECTED_ERR_DATA",
    })
}

export const setRap = (value) => {
    return ({
        type: "SET_RAP",
        playload: value,
    })
};

export const clearRap = () => {
    return ({
        type: "CLEAR_RAP",
    })
}
export const initValue = () => {
    return ({
        type: "INIT_VALUE",
    })
}

export const setAppDataBaseName = (value) => {
    return ({
        type: "SET_DB_NAME",
        playload: value,
    })
}
export const setUserName = (value) => {
    return ({
        type: "SET_USER_NAME",
        playload: value,
    })

}
