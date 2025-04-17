import React, { useState } from "react";
import { Flex, Button, DatePicker, Form, Select, Checkbox, Input, Space, notification } from "antd";
import { initValue } from "../../../store/actions";
import { useDispatch, useSelector } from "react-redux";
import { URI_API_SERVER, API_GET_ROUND_TYPES, API_GET_REPRICING_RTPL, API_CHECK_REPRICING_PRICE, API_CHANGE_REPRICING_PRICES, API_ADD_NEW_REPRICING_RTPL } from "../../../constants/api";
import { apiGetData, apiJsonPostData } from '../../../utils/common';
import useFetchData from "../../../hooks/useFetchData";
import { openIfoModal } from "../../../utils/network";
import SysLogsList from "../sysLogsList/SysLogsList";
import userService from "../../../services/userService";

const RepricingCtlBar = function () {
    const [isLoading, setIsLoading] = useState(false);
    const [startDateCtl, setStartDateCtl] = useState();
    const priceData = useDispatch();
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const task = useSelector((state) => state.xlsDataReducer.task);

    const url_get_round_types = `${URI_API_SERVER}/${dbName}/${API_GET_ROUND_TYPES}`;
    const url_get_rtplList = `${URI_API_SERVER}/${dbName}/${API_GET_REPRICING_RTPL}`;
    const url_check_price = `${URI_API_SERVER}/${dbName}/${API_CHECK_REPRICING_PRICE}/${task}`;
    const url_change_prices = `${URI_API_SERVER}/${dbName}/${API_CHANGE_REPRICING_PRICES}`;
    const url_add_new_rtpl = `${URI_API_SERVER}/${dbName}/${API_ADD_NEW_REPRICING_RTPL}`;

    const { data: rndtList, error: errRndtData, loaded: loadedRndtData } = useFetchData(`${url_get_round_types}`);
    const { data: rtplList, error: errRtplData, loaded: loadedRtplData } = useFetchData(`${url_get_rtplList}`);
    const [cbChechit, setCbChange] = useState(false);
    const [form] = Form.useForm();
    const v_rtplId = Form.useWatch('rtplId', form);

    const onChangeStartDate = (date, dateStr) => {
        setStartDateCtl(dateStr.replace(RegExp("[-:]", "g"), "").substr(0, 8));
        console.log("dateStr=", dateStr);
        console.log("startDateCtl=", startDateCtl);
    }

    const onFinish = async (value) => {
        console.log("onFinish:", JSON.stringify({ ...value, userName: userService.getUsername(), task: task }));
        let url;
        if (task > 0) {
            setIsLoading(true);
            if (value.new) {
                url = url_add_new_rtpl;
            } else {
                url = url_change_prices;
            }
            const body = await apiJsonPostData(url, JSON.stringify({ ...value, startDateStr: startDateCtl, userName: userService.getUsername(), task: task }));
            setIsLoading(false);
            if (!body?.error) {
                openIfoModal("Результат ввода изменений в тарифный план для репрайсинга", <SysLogsList data={body} />)
            } else {
                notification.open({
                    message: `Ошибка`,
                    description: `Ошибка ввода изменений в тарифный план для репрайсинга ${body?.error?.message}!`,
                    duration: 0,
                    className: 'notification-warn',
                })
            }
        }
    }

    const onFinishFailed = () => {

    }

    const cbOnChange = (e) => {
        setCbChange(e.target.checked);
    }

    const handleLoadPrice = () => {
        priceData(initValue());
    }
    const handleViewRtpl = () => {
        console.log("handleViewRtpl:v_rtplId=", form.getFieldsValue());
    }

    const handleCheckPrice = async () => {
        if (task > 0) {
            setIsLoading(true);
            const body = await apiGetData(url_check_price);
            setIsLoading(false);
            if (!body?.error) {
                openIfoModal("Контроль исходных данных прайс-листа для репрайсинга", <SysLogsList data={body} />)
            } else {
                notification.open({
                    message: `Ошибка`,
                    description: `Ошибка при выполнении процедуры контроля исходных данных прайс-листа для репрайсинга ${body?.error?.message}!`,
                    duration: 0,
                    className: 'notification-warn',
                })
            }
        }
    }

    return (
        isLoading
            ? <h2>Подождите, идет загрузка информации..</h2>
            : <>
                <Form form={form}
                    // labelCol={{
                    //     span: 6,
                    // }}
                    wrapperCol={{
                        span: 30,
                    }}
                    layout="inline"
                    initialValues={{
                        size: 'default'
                    }}
                    size='default'
                    // style={{
                    //     maxWidth: 500,
                    // }}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                >
                    <Space>
                        <Form.Item
                            // label="Новый ТП"
                            name="new"
                            valuePropName="checked"
                            value='true'
                        >
                            <Flex gap="small" wrap="wrap" style={{ marginBottom: 10 }}>
                                <Button key={1} type="primary" onClick={handleCheckPrice}>Контроль исходных данных (КИД)</Button>
                                <Button key={2} type="primary" htmlType="submit">Ввод ТП</Button>
                                <Button key={3} type="primary" onClick={handleViewRtpl}>Просмотр ТП</Button>
                                <Button key={6} type="primary" onClick={handleLoadPrice}>Новый прайс-лист</Button>
                            </Flex>
                        </Form.Item>
                    </Space>
                    <Space>
                        <Form.Item
                            // label="Новый ТП"
                            name="new"
                            valuePropName="checked"
                            value='true'
                        >
                            <Checkbox onChange={cbOnChange}>Новый ТП</Checkbox>
                        </Form.Item>

                        {cbChechit ?
                            <Form.Item
                                label="ТП"
                                name="rtplName"
                                rules={[
                                    {
                                        required: true,
                                        message: 'введите наименование ТП',
                                    },
                                ]}
                            >
                                <Input
                                    style={{
                                        width: 272,
                                        margin: '0 8px',
                                    }}
                                />
                            </Form.Item>
                            : <Form.Item
                                label="ТП"
                                name="rtplId"
                                rules={[
                                    {
                                        required: true,
                                        message: 'укажите ТП',
                                    },
                                ]}
                            >
                                <Select
                                    style={{
                                        width: 300,
                                        margin: '0 8px',
                                    }}
                                >
                                    {
                                        loadedRtplData && rtplList?.sort((a, b) => a.rtplName.localeCompare(b.rtplName))
                                            .map(((e) => ({ ...e, key: e.rtplId })))
                                            .map(e =>
                                                <Select.Option value={e.rtplId}>{e.rtplId}, {e.rtplName}</Select.Option>
                                            )
                                    }

                                </Select>
                            </Form.Item>
                        }
                        <Form.Item
                            label="RNDT_V"
                            name="rndtvId"
                            rules={[
                                {
                                    required: true,
                                    message: "укажите правило округления для голосовых вызовов",
                                },
                            ]}
                        >
                            <Select
                                style={{
                                    width: 250,
                                    margin: '0 8px',
                                }}
                            >
                                {

                                    loadedRndtData && rndtList?.sort((a, b) => a.rndtid - b.rndtid)
                                        .map(((e) => ({ ...e, key: e.rndtid })))
                                        .map(e =>
                                            <Select.Option value={e.rndtid}>{e.rndtid}, {e.def}</Select.Option>
                                        )
                                }

                            </Select>
                        </Form.Item>
                        <Form.Item
                            label="RNDT_D"
                            name="rndtdId"
                            rules={[
                                {
                                    required: true,
                                    message: "укажите правило округления для передачи данных",
                                },
                            ]}
                        >
                            <Select style={{
                                width: 250,
                                margin: '0 8px',
                            }}>
                                {
                                    loadedRndtData && rndtList?.sort((a, b) => a.rndtid - b.rndtid)
                                        .map(((e) => ({ ...e, key: e.rndtid })))
                                        .map(e =>
                                            <Select.Option value={e.rndtid}>{e.rndtid}, {e.def}</Select.Option>
                                        )
                                }

                            </Select>
                        </Form.Item>
                        <Form.Item
                            label="Дата"
                            name="startDate"
                            rules={[
                                {
                                    required: true,
                                },
                            ]}
                        >
                            <DatePicker size={'small'} onChange={onChangeStartDate} />
                        </Form.Item>
                        {/* <Form.Item>
                            <Button type="primary" htmlType="submit">Ввод</Button>
                        </Form.Item> */}
                    </Space>
                </Form>
            </>
    )
}
export default RepricingCtlBar;