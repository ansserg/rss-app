import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { notification, message, Button, InputNumber, Form, Input, Select, DatePicker, Typography } from 'antd';
import { postApiResource, getApiResource, openIfoModal } from "../../../utils/network";
import { URI_API_SERVER, API_SET_S4H, API_GET_ROAM_OPERATORS, API_GET_CURRENCIES, DB_UNDEF } from "../../../constants/api";
import SysLogsList from "../../../components/Page/sysLogsList";
import useFetchData from "../../../hooks/useFetchData";
import { apiJsonPostData } from '../../../utils/common';

const formItemLayout = {
    labelCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 6,
        },
    },
    wrapperCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 14,
        },
    },
};


const S4H = function () {
    const user = useSelector((state) => state.userNameReducer.userName);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    // const [roList, setRoList] = useState(null);
    // const [curCodeList, setCurCodeList] = useState(null);
    const url_set_s4h = `${URI_API_SERVER}/${dbName}/${API_SET_S4H}`;
    const url_get_roam_operators = `${URI_API_SERVER}/${dbName}/${API_GET_ROAM_OPERATORS}`;
    const url_get_currencies = `${URI_API_SERVER}/${dbName}/${API_GET_CURRENCIES}`;
    const { data: roList, error: errorRoLst, loaded: loadedRoLst } = useFetchData(`${url_get_roam_operators}`);
    const { data: curCodeList, error: errorCurLst, loaded: loadedCurLst } = useFetchData(`${url_get_currencies}`);
    const [isLoading, setIsLoading] = useState(false);
    const { Text } = Typography;

    useEffect(() => {
    }, []);

    const getDirectLists = async () => {
        // console.log("getDirectLists..")
        setIsLoading(true);
        const roLst = await getApiResource(url_get_roam_operators);
        const curLst = await getApiResource(url_get_currencies);
        setIsLoading(false);
        // roLst ? setRoList(roLst) : message.error('ошибка чтения справочника операторов !');
        // curLst ? setCurCodeList(curLst) : message.error('ошибка чтения справочника валют !');
        // console.log(roLst);
        // console.log(curLst);
    }

    const onFinish = async (value) => {
        // console.log("onFinish");
        // console.log(value);
        const v = ({ ...value, userName: user });
        // console.log(v);
        setIsLoading(true);
        // const res = await postApiResource(url_set_s4h, JSON.stringify({ ...value, userName: user }));
        const res = await apiJsonPostData(url_set_s4h, JSON.stringify({ ...value, userName: user }));
        setIsLoading(false);
        if (!res?.error) {
            openIfoModal("Ввод контировок", <SysLogsList data={res} />)
        }  else {
            notification.open({
                message: `Ошибка ввода контировок!`,
                description: res?.message,
                duration:0,
            })
        }
    }
    const onFinishFailed = () => {
        // console.log("onFinishFailed");
    }
    return (
        // isLoading ? <h2>Подождите...</h2>
        (dbName === DB_UNDEF)
            ? <>
                {
                    notification.open({
                        message: `Предупреждение`,
                        description: "Укажите базу данных!",
                        className: 'notification-warn',
                    })
                }
            </>
            : loadedRoLst && loadedCurLst 
                ? <>
                    <Text mark>
                        База данных:{dbName}
                    </Text>
                    <Form
                        {...formItemLayout}
                        variant="filled"
                        style={{
                            maxWidth: 1200,
                        }}
                        onFinish={onFinish}
                        onFinishFailed={onFinishFailed}
                    >
                        <Form.Item
                            label="TAP_CODE"
                            name="tapCode"
                            rules={[
                                {
                                    required: true,
                                    message: 'выберите tap_code',
                                },
                            ]}
                        >
                            <Select>
                                {
                                    roList && roList.sort((a, b) => a.shortName.localeCompare(b.shortName))
                                        .map(((e, index) => ({ ...e, key: index })))
                                        .map(e =>
                                            <Select.Option value={e.shortName}>{e.nameR}</Select.Option>
                                        )
                                }

                            </Select>
                        </Form.Item>
                        <Form.Item
                            label="Номер контракта (OperId)"
                            name="contrNum"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите номер контракта',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="КРЕДИТОР_S4"
                            name="vendor"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите номер кредитора',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="ДЕБИТОР_S4"
                            name="debitor"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите номер дебитора',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="SD (сбытовой) контракт"
                            name="sd"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите номер сбытовой контракт',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="MM (закупочный) контракт"
                            name="mm"
                            rules={[
                                {
                                    required: true,
                                    message: 'введите номер закупочный контракт',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="Валюта исходящих счетов (от МегаФон)"
                            name="curInvOut"
                            rules={[
                                {
                                    required: true,
                                    message: 'выберите валюту исходящих счетов',
                                },
                            ]}
                        >
                            <Select>
                                {
                                    curCodeList && curCodeList.map(e => ({
                                        ...e, key: e.curId
                                    })
                                    ).sort((a, b) => {
                                        a.nameR.localeCompare(b.nameR)
                                    }).map(e =>
                                        <Select.Option value={e.code}>{e.nameR + " (" + e.code + ")"}</Select.Option>
                                    )
                                }
                            </Select>
                        </Form.Item>
                        <Form.Item
                            label="Валюта входящих счетов (от партнера)"
                            name="curInvIn"
                            rules={[
                                {
                                    required: true,
                                    message: 'выберите валюту входящих счетов',
                                },
                            ]}
                        >
                            <Select>
                                {
                                    curCodeList && curCodeList.map(e => ({
                                        ...e, key: e.curId
                                    })
                                    ).sort((a, b) => {
                                        a.nameR.localeCompare(b.nameR)
                                    }).map(e =>
                                        <Select.Option value={e.code}>{e.nameR + " (" + e.code + ")"}</Select.Option>
                                    )
                                }
                            </Select>
                        </Form.Item>
                        <Form.Item
                            label="Срок оплаты счета"
                            name="termInDays"
                            rules={[
                                {
                                    required: true,
                                    message: 'укажите срок оплаты счета',
                                },
                            ]}
                        >
                            <InputNumber
                                style={{
                                    width: '100%',
                                }}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Номер RFC"
                            name="task"
                            rules={[
                                {
                                    required: true,
                                    message: 'укажите срок оплаты счета',
                                },
                            ]}
                        >
                            <InputNumber
                                style={{
                                    width: '100%',
                                }}
                            />
                        </Form.Item>
                        <Form.Item
                            label="дата"
                            name="startDate"
                            rules={[
                                {
                                    required: true,
                                    message: 'укажите начальную дату',
                                },
                            ]}
                        >
                            <DatePicker />
                        </Form.Item>
                        <Form.Item
                            wrapperCol={{
                                offset: 6,
                                span: 16,
                            }}
                        >
                            <Button type="primary" htmlType="submit">
                                Submit
                            </Button>
                        </Form.Item>
                    </Form>
                </>
                : <h2>Подождите...</h2>
    )
}
export default S4H;