import React from "react";
import userService from "../../../services/userService";
import HomePage from "../../../containers/pages/homePage";

const LogOut = function () {
    return (
        userService.doLogout()
    )
}
export default LogOut;