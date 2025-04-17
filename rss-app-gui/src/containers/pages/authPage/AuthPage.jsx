import React from "react";
import { useNavigate } from "react-router-dom"
import { useDispatch } from "react-redux";
import { Button, Checkbox, Form, Input } from 'antd';
import { setUserName } from "../../../store/actions";
import userService from "../../../services/userService";

const AuthPage = function () {
    const navigate = useNavigate();
    const dispatchUserName = useDispatch();
    const onFinish = (values) => {
        dispatchUserName(setUserName({ userName: values.username }));
        dispatchUserName(setUserName({ auth: true }));
        // navigate("/home");
        console.log('Success:', values);
    };
    const onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };
    const clearAuth = () => {
        dispatchUserName(setUserName({ userName: undefined }));
        dispatchUserName(setUserName({ auth: false }));
    }
    clearAuth();
    // onFinish({ userName: "SAnisimov", auth: true });
    return (
        <div>
            {
                // navigate("/home")
                // userService.doLogout()
            }
        </div>
    )
}

export default AuthPage;