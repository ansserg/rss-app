import React from "react";
import { useSelector } from "react-redux";
import userService from "../../../services/userService";

const HomePage = function () {
    // const user = useSelector((state) => state.userNameReducer.userName);
    const user = userService.getUsername();
    console.log("user:",user);
    return (
        <div>
            {/* Добро пожаловать {user}! */}
        </div>
    )
}
export default HomePage;