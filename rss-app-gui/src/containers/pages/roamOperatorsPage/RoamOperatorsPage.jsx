import React, { useState, useEffect } from "react";
import { useSelector } from 'react-redux';
import { notification, Button, DatePicker, Form, Input, InputNumber, Select, Typography, } from "antd";
import { openIfoModal } from "../../../utils/network";
import { API_ADD_TEST_RMOP, URI_API_SERVER, API_GET_COUNTRIES, API_GET_ROAM_OPERATORS, API_GET_RTPL_LIST, API_OCSDB_ADD_RMOP, DB_UNDEF, DB_NAME_RSS } from "../../../constants/api";
import SysLogsList from "../../../components/Page/sysLogsList";
import useFetchData from "../../../hooks/useFetchData";
import { apiJsonPostData } from "../../../utils/common";
import userService from "../../../services/userService";




const RoamOperatorsPage = function () {
    // const userName = useSelector((state) => state.userNameReducer.userName);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const url_get_currencies = `${URI_API_SERVER}/${dbName}/${API_GET_COUNTRIES}`;
    const url_get_roam_operators = `${URI_API_SERVER}/${dbName}/${API_GET_ROAM_OPERATORS}`;
    const url_add_test_operators = `${URI_API_SERVER}/${dbName}/${API_ADD_TEST_RMOP}`;
    const url_get_getRtplList = `${URI_API_SERVER}/${dbName}/${API_GET_RTPL_LIST}`;
    const url_add_bis_rmop = `${URI_API_SERVER}/${dbName}/${API_OCSDB_ADD_RMOP}`;
    const { data: countries, error: errorCountriesData, loaded: loadedCountriesData } = useFetchData(`${url_get_currencies}`);
    const { data: roList, error: errRoamOperData, loaded: loadedRoamOperData } = useFetchData(`${url_get_roam_operators}`);
    const { data: rtplList, error: errorRtplData, loaded: loadedRtplData } = useFetchData(`${url_get_getRtplList}`);
    const [isLoading, setIsLoading] = useState(false);
    const { Text } = Typography;

    useEffect(() => {
    }, []);

    const onFinish = async (value) => {
        setIsLoading(true);
        const res = await apiJsonPostData(url_add_test_operators, JSON.stringify({ ...value, userName: userService.getUsername() }));
        const res1 = await apiJsonPostData(url_add_bis_rmop, JSON.stringify({ ...value, userName: userService.getUsername() }));
        setIsLoading(false);
        if (!res?.error && !res1?.error) {
            openIfoModal("Новый оператор", <SysLogsList data={[...res, ...res1]} />);
        } else if (!res?.errror) {
            res1 = {
                message: "BIS:'Ошибка ввода изменений",
                apmt: 1,
                process: `BIS.WRK_OPERATORS:RFC-${value[0].task}`,
            }
            openIfoModal("Новый оператор", <SysLogsList data={[...res, ...res1]} />);
        } else if (res1) {
            res = {
                message: "RSSMB:'Ошибка ввода изменений",
                apmt: 1,
                process: `RSSMB.WRK_OPERATORS:RFC-${value[0].task}`,
            }
            openIfoModal("Новый оператор", <SysLogsList data={[...res, ...res1]} />);
        } else {
            res = {
                message: "Ошибка ввода изменений",
                apmt: 1,
                process: `WRK_OPERATORS:RFC-${value[0].task}`,
            }
            openIfoModal("Новый оператор", <SysLogsList data={res} />);
        }

    }
    const onFinishFailed = () => {

    }

    return (
        (dbName === DB_UNDEF) ?
            <>
                {
                    notification.open({
                        message: `Предупреждение`,
                        description: "Укажите базу данных!",
                        className: 'notification-warn',
                    })
                }
            </>
            : isLoading
                ? <h2>Подождите..</h2>
                : loadedCountriesData && loadedRoamOperData && loadedRtplData
                    ? errorCountriesData || errRoamOperData || errorRtplData
                        ? <>
                            {/* {
                            notification.open({
                                message: `ошибка чтения справочников`,
                                description: errorCountriesData?.message ?? errRoamOperData?.message ?? errorRtplData?.message,
                                className: 'notification-warn',
                            })
                        } */}
                        </>
                        // : <div className={dbName===DB_NAME_RSS&&styles.rssbacgr} >
                        : <div >
                            <Text mark>
                                База данных:{dbName}
                            </Text>
                            <Form
                                labelCol={{
                                    span: 6,
                                }}
                                wrapperCol={{
                                    span: 16,
                                }}
                                layout="horizontal"
                                initialValues={{
                                    size: 'default'
                                }}
                                size='default'
                                style={{
                                    maxWidth: 500,
                                }}                                   
                                onFinish={onFinish}
                                onFinishFailed={onFinishFailed}
                            >
                                <Form.Item label="TAP_CODE"
                                    name="tapCode"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите TAP-код нового оперататора',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                                <Form.Item label="Наименование"
                                    name="name"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите наименование нового оператора',
                                        },
                                    ]}
                                >
                                    <Input />
                                </Form.Item>
                                <Form.Item
                                    label="TMPL_TAP_CODE"
                                    name="tmplTapCode"
                                    rules={[
                                        {
                                            message: 'выберите TAP-код шаблонного оператора',
                                        },
                                    ]}
                                >
                                    <Select>
                                        {
                                            loadedRoamOperData && roList.sort((a, b) => a.shortName.localeCompare(b.shortName))
                                                .map(((e, index) => ({ ...e, key: index })))
                                                .map(e =>
                                                    <Select.Option value={e.shortName}>{e.nameR}</Select.Option>
                                                )
                                        }

                                    </Select>
                                </Form.Item>
                                <Form.Item label="Страна"
                                    name="country"
                                    rules={[
                                        {
                                            message: 'выберите страну нового оператора',
                                        },
                                    ]}
                                >
                                    <Select>
                                        {countries?.sort((a, b) => a.nameR.localeCompare(b.nameR)).map(e =>
                                            <Select.Option value={e.nameR}>{e.couCode + ": " + e.nameE + " (" + e.nameR + ")"}</Select.Option>
                                        )}
                                    </Select>
                                </Form.Item>
                                <Form.Item label="Тарифный план"
                                    name="hrsRtplId"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'укажите тарифный план',
                                        },
                                    ]}
                                >
                                    <Select>
                                        {loadedRtplData && rtplList.sort((a, b) => a.rtplName.localeCompare(b.rtplName)).map(e =>
                                            <Select.Option value={e.rtplId}>{e.rtplName + " (" + e.rtplId + ")"}</Select.Option>
                                        )}
                                    </Select>
                                </Form.Item>
                                <Form.Item label="IMSI"
                                    name="imsi"
                                >
                                    <Input />
                                </Form.Item>
                                <Form.Item label="Дата"
                                    name="startDate"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите Дату',
                                        },
                                    ]}
                                >
                                    <DatePicker />
                                </Form.Item>
                                <Form.Item label="RFC"
                                    name="task"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'введите номер RFC',
                                        },
                                    ]}
                                >
                                    <InputNumber />
                                </Form.Item>
                                <Form.Item
                                    wrapperCol={{
                                        offset: 6,
                                        span: 16,
                                    }}
                                >
                                    <Button type="primary" htmlType="submit">
                                        Отправить
                                    </Button>
                                </Form.Item>
                            </Form>
                        </div>
                    : <h1>
                        Загрузка справочников..
                    </h1>

    )
}
export default RoamOperatorsPage;