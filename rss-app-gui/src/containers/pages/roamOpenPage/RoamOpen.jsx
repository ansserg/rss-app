import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { message, Button, InputNumber, Form, Input, Select, DatePicker, Checkbox, Space, Row, Col, Typography, notification } from 'antd';
import { postApiResource, getApiResource, openIfoModal } from "../../../utils/network";
import { URI_API_SERVER, API_ROAM_OPEN, API_GET_ROAM_OPERATORS, API_GET_CURRENCIES, API_GET_RTPL_LIST, DB_UNDEF } from "../../../constants/api";
import SysLogsList from "../../../components/Page/sysLogsList";
import { apiJsonPostData } from '../../../utils/common';
import useFetchData from '../../../hooks/useFetchData';

const formItemLayout = {
    labelCol: {
        xs: {
            span: 14,
        },
        sm: {
            span: 7,
        },
    },
    wrapperCol: {
        xs: {
            span: 14,
        },
        sm: {
            span: 14,
        },
    },
};


const RoamOpen = function ({ roamType, roamStates }) {
    const user = useSelector((state) => state.userNameReducer.userName);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    // const [roList, setRoList] = useState(null);
    // const [curCodeList, setCurCodeList] = useState(null);
    const url_roam_open = `${URI_API_SERVER}/${dbName}/${API_ROAM_OPEN}`;
    const url_get_roam_operators = `${URI_API_SERVER}/${dbName}/${API_GET_ROAM_OPERATORS}`;
    const url_get_currencies = `${URI_API_SERVER}/${dbName}/${API_GET_CURRENCIES}`;
    const [isLoading, setIsLoading] = useState(false);
    const { Text } = Typography;
    const { data: roList, error: errorRoLst, loaded: loadedRoLst } = useFetchData(`${url_get_roam_operators}`);
    const { data: curCodeList, error: errorCurLst, loaded: loadedCurLst } = useFetchData(`${url_get_currencies}`);

    useEffect(() => {
        // getDirectLists();
    }, []);

    const getDirectLists = async () => {
        // console.log("getDirectLists..")
        setIsLoading(true);
        const roLst = await getApiResource(url_get_roam_operators);
        const curLst = await getApiResource(url_get_currencies);
        setIsLoading(false);
        // roLst ? setRoList(roLst) : message.error('ошибка чтения справочника операторов!');
        // curLst ? setCurCodeList(curLst) : message.error('ошибка чтения справочника операторов!');;
        // console.log(roLst);
        // console.log(curLst);
    }

    const onFinish = async (value) => {
        // console.log("onFinish");
        // console.log(value);
        const v = ({ ...value, userName: user, roamTp: roamType, roamStates: roamStates });
        // console.log(v);
        setIsLoading(true);
        // const res = await postApiResource(url_roam_open, JSON.stringify({ ...value, userName: user, roamTp: roamType, roamStates: roamStates }));
        const res = await apiJsonPostData(url_roam_open, JSON.stringify({ ...value, userName: user, roamTp: roamType, roamStates: roamStates }));
        setIsLoading(false);
        if (!res?.error) {
            openIfoModal("Информация из системного журнала", <SysLogsList data={res} />)
        } else {
            {
                notification.open({
                    message: `Ошибка запроса ресурса`,
                    description: `${url_roam_open} ${res?.message}`,
                    className: 'notification-warn',
                })
            }
        }
    }
    const onFinishFailed = () => {
        // console.log("onFinishFailed");
    }
    return (
        (dbName === DB_UNDEF)
            ? <>
                {
                    notification.open({
                        message: `Предупреждение`,
                        description: "Не выбрана база данных!",
                        className: 'notification-warn',
                    })
                }
            </>
            : loadedRoLst && loadedRoLst && !isLoading
                ? <>
                    <Text mark>
                        База данных:{dbName}
                    </Text>
                    <Form
                        {...formItemLayout}
                        style={{
                            maxWidth: 500,
                        }}
                        variant="filled"
                        initialValues={{
                            gprs: true,
                            kk: true,
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
                                        // .map((e => ({ ...e, key: e.roamId })))
                                        .map(((e, index) => ({ ...e, key: index })))
                                        .map(e =>
                                            <Select.Option value={e.shortName}>{e.nameR}</Select.Option>
                                        )
                                }

                            </Select>
                        </Form.Item>
                        {(roamStates === 3) ? <>
                            <Form.Item
                                label="Валюта файлов"
                                name="curFile"
                            // rules={[
                            //     {
                            //         // required: true,
                            //         // message: 'выберите tap_code',
                            //     },
                            // ]}
                            >
                                <Select>
                                    {
                                        curCodeList && curCodeList.map((e, index) => ({
                                            // ...e, key: e.curId
                                            ...e, key: index
                                        })
                                        ).sort((a, b) => {
                                            a.nameR.localeCompare(b.nameR)
                                        }).map(e =>
                                            <Select.Option value={e.curId}>{e.nameR + " (" + e.code + ")"}</Select.Option>
                                        )
                                    }

                                </Select>
                            </Form.Item>
                            <Form.Item
                                label="Номер договора"
                                name="contractNum"
                            // rules={[
                            //     {
                            //         // required: true,
                            //         // message: 'укажите номер RFC',
                            //     },
                            // ]}
                            >
                                <InputNumber
                                    style={{
                                        width: '100%',
                                    }}
                                />
                            </Form.Item>
                        </>
                            : <></>}
                        <Form.Item
                            label="Номер RFC"
                            name="task"
                            rules={[
                                {
                                    required: true,
                                    message: 'укажите номер RFC',
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
                            label="GPRS"
                            name="gprs"
                            valuePropName="checked"
                            value='true'
                        >
                            <Checkbox />

                        </Form.Item>

                        <Form.Item
                            label="Роуминг в КК"
                            name="kk"
                            valuePropName="checked"
                        >
                            <Checkbox />

                        </Form.Item>

                        <Form.Item
                            wrapperCol={{
                                offset: 2,
                                span: 10,
                            }}
                        >
                            <Button type="primary" htmlType="submit">
                                Отправить
                            </Button>
                        </Form.Item>
                    </Form>
                </>
                : <h2>Подождите...</h2>

    )

}
export default RoamOpen;