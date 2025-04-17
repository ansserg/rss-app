import React, { useState } from 'react';
import { useSelector } from "react-redux";
import { Button, Row, InputNumber, Form, Input, Select, DatePicker, theme, Col, Divider, Upload, Typography, notification } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { DB_UNDEF, URI_API_SERVER } from "../../../constants/api";
import AuthPage from '../authPage/AuthPage';
import SysLogsList from '../../../components/Page/sysLogsList/SysLogsList';
import { API_ADD_EXCH } from '../../../constants/api';
import { postApiResource, openIfoModal } from '../../../utils/network';
import { apiJsonPostData } from '../../../utils/common';

const formItemLayout = {
    labelCol: {
        maxWidth: 'none',
        padding: 0,
        xs: {
            span: 10,
        },
        sm: {
            span: 10,
        },
    },
    wrapperCol: {
        xs: {
            span: 10,
        },
        sm: {
            span: 14,
        },
    },
};

const ExchangePage = function ({ tp }) {
    const user = useSelector((state) => state.userNameReducer.userName);
    const [isLoading, setIsLoading] = useState(false);
    const { token } = theme.useToken();
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const URL_API = URI_API_SERVER + "/" + dbName;
    const [fileList, setFileList] = useState([]);
    const [startDate, setStartDate] = useState();
    const [task, setTask] = useState();
    const url_exch = `${URL_API}/${API_ADD_EXCH}/${tp}`;
    const { Text } = Typography;

    const props = {
        accept: ".xls,.xlsx",
        onRemove: (file) => {
            const index = fileList.indexOf(file);
            const newFileList = fileList.slice();
            newFileList.splice(index, 1);
            setFileList(newFileList);
        },
        beforeUpload: (file) => {
            // setFileList([...fileList, file]);
            console.log('beforeUpload', file);
            return false;
        },
        fileList
    };


    const onFinish = async (value) => {
        console.log("onFinish", "user:", user, "dbName:", dbName);
        console.log(value);
        const currArr = Object.entries(value).map((v => {
            return ({ curName: v[0], curValue: v[1] });
        }));
        const bodyVal = ({ exchData: currArr, userName: user, startDate: startDate, task: task });
        setIsLoading(true);
        // const res = await postApiResource(url_exch, JSON.stringify(bodyVal));
        const res = await apiJsonPostData(url_exch, JSON.stringify(bodyVal));
        setIsLoading(false);
        if (!res?.error) {
            openIfoModal("Ввод курса валют", <SysLogsList data={res} />)
        } else {
            notification.open({
                message: `Ошибка выполнения запроса`,
                description: res.message,
                duration: 0,
            })
        }
    }
    const onFinishFailed = () => {
        console.log("onFinishFailed");
    }

    const onChangeStartDate = (value) => {
        setStartDate(value);
    }

    const onChangeTask = (e) => {
        setTask(e.target.value);
    }

    return (
        (dbName === DB_UNDEF)
            ? <>
                {
                    notification.open({
                        message: `Предупреждение`,
                        description: "Выберите базу данных!",
                        duration: 0,
                    })
                }
            </>
            : isLoading
                ? <h2>Подождите...</h2>
                : <>
                    <Text mark>
                        База данных:{dbName}
                    </Text>
                    <Form
                        layout='inline'
                        style={{
                            padding: 10,
                            // wrapperCol:10,
                            maxWidth: 'none'
                        }}
                    >
                        <Form.Item label="Дата"
                            name="startDate"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите Дату',
                                },
                            ]}
                        >
                            <DatePicker onChange={onChangeStartDate} />
                        </Form.Item>
                        <Form.Item
                            label="RFC"
                            name="task"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите номер RFC',
                                },
                            ]}
                        >
                            <Input onChange={onChangeTask} />
                        </Form.Item>
                        {(tp == 0)
                            ? <Upload {...props} disabled>
                                <Button icon={<UploadOutlined />}>Ввод из файла</Button>
                            </Upload>
                            : <></>
                        }
                    </Form>
                    <Divider />
                    <Form
                        {...formItemLayout}
                        variant="filled"
                        style={{
                            maxWidth: 'none',
                            background: token.colorFillAlter,
                            borderRadius: token.borderRadiusLG,
                            padding: 5,
                        }}
                        size='default'
                        onFinish={onFinish}
                        onFinishFailed={onFinishFailed}
                    >
                        <Row gutter={24}>
                            <Col span={6} key={1}>
                                <Form.Item
                                    label="USD"
                                    name="USD"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите курс USD',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                            </Col>
                            <Col span={6} key={2}>
                                <Form.Item
                                    label="EUR"
                                    name="EUR"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите курс EUR',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                            </Col>
                            {(tp == 0)
                                ? <>
                                    <Col span={6} key={3}>
                                        <Form.Item
                                            label="RUB"
                                            name="RUB"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'введите курс RUB',
                                                },
                                            ]}
                                        >
                                            <Input />
                                        </Form.Item>
                                    </Col>
                                    <Col span={6} key={4}>
                                        <Form.Item
                                            label="RUR (курс IMF)"
                                            name="RUR"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'введите курс RUB',
                                                },
                                            ]}
                                        >
                                            <Input />
                                        </Form.Item>
                                    </Col>
                                </>
                                : <></>
                            }
                            <Col span={6} key={6}>
                                <Form.Item
                                    label={(tp == 0) ? "AUSTRALIA_DOLLAR" : "AUD"}
                                    name="AUD"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите курс AUSTRALIA_DOLLAR',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                            </Col>
                            <Col span={6} key={5}>
                                <Form.Item
                                    label={(tp == 0) ? "POUNDS_STERLING" : "GBP"}
                                    name="GBP"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите курс POUNDS_STERLING',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                            </Col>
                            {(tp == 0)
                                ? <>
                                    <Col span={6} key={7}>
                                        <Form.Item
                                            label="DENMARK_KRON"
                                            name="DKK"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'введите курс AUSTRALIA_DOLLAR',
                                                },
                                            ]}
                                        >
                                            <Input />
                                        </Form.Item>
                                    </Col>
                                    <Col span={6} key={8}>
                                        <Form.Item
                                            label="NEWZEALAND_DOLLAR"
                                            name="NZD"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'введите курс NEWZEALAND_DOLLAR',
                                                },
                                            ]}
                                        >
                                            <Input />
                                        </Form.Item>
                                    </Col>
                                    <Col span={6} key={9}>
                                        <Form.Item
                                            label="NORWAY_KRON"
                                            name="NOK"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'введите курс NORWAY_KRON',
                                                },
                                            ]}
                                        >
                                            <Input />
                                        </Form.Item>
                                    </Col>
                                </>
                                : <></>
                            }
                            <Col span={6} key={10}>
                                <Form.Item
                                    label={(tp == 0) ? "SWISS_FRANC" : "CHF"}
                                    name="CHF"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите курс SWISS_FRANC',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                            </Col>
                            {(tp == 0)
                                ? <>
                                    <Col span={6} key={11}>
                                        <Form.Item
                                            label="SWEDEN_KRON"
                                            name="SEK"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'введите курс SWEDEN_KRON',
                                                },
                                            ]}
                                        >
                                            <Input />
                                        </Form.Item>
                                    </Col>
                                </>
                                : <></>
                            }
                            <Col span={6} key={12}>
                                <Form.Item
                                    label={(tp == 0) ? "CHINESE_YUAN" : "CNY"}
                                    name="CNY"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите курс CHINESE_YUAN',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                            </Col>
                        </Row>
                        <Divider />
                        <div
                            style={{
                                textAlign: 'right',
                            }}
                        >
                            <Button type="primary" htmlType="submit">
                                Отправить
                            </Button>
                        </div>
                    </Form>
                </>

    );
};
export default ExchangePage;
