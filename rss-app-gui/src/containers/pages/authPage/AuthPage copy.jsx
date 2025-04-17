import React from "react";
import { useNavigate } from "react-router-dom"
import { useDispatch } from "react-redux";
import { Button, Checkbox, Form, Input } from 'antd';
import { setUserName } from "../../../store/actions";

const AuthPage = function () {
    const navigate = useNavigate();
    const dispatchUserName = useDispatch();
    const onFinish = (values) => {
        dispatchUserName(setUserName({ userName: values.username }));
        dispatchUserName(setUserName({ auth: true }));
        navigate("/home");
        // console.log('Success:', values);
    };
    const onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };
    const clearAuth=()=>{
        dispatchUserName(setUserName({ userName: undefined }));
        dispatchUserName(setUserName({ auth: false }));
    }
    clearAuth();
    return (
        <Form
            name="basic"
            labelCol={{
                span: 8,
            }}
            wrapperCol={{
                span: 16,
            }}
            style={{
                maxWidth: 600,
            }}
            initialValues={{
                remember: true,
            }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
        >
            <Form.Item
                label="Username"
                name="username"
                rules={[
                    {
                        required: true,
                        message: 'Please input your username!',
                    },
                ]}
            >
                <Input />
            </Form.Item>

            <Form.Item
                label="Password"
                name="password"
                rules={[
                    {
                        required: true,
                        message: 'Please input your password!',
                    },
                ]}
            >
                <Input.Password />
            </Form.Item>

            {/* <Form.Item
                name="remember"
                valuePropName="checked"
                wrapperCol={{
                    offset: 8,
                    span: 16,
                }}
            >
                <Checkbox>Remember me</Checkbox>
            </Form.Item> */}

            <Form.Item
                wrapperCol={{
                    offset: 8,
                    span: 16,
                }}
            >
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
        </Form>
    )
}

export default AuthPage;