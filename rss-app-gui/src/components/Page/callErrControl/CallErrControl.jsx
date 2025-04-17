import React, { useState } from 'react';
import { useSelector, useDispatch } from "react-redux";
import { Button, Flex, message, Modal, Space, Typography } from 'antd';
import { setCallErr, clearErrData } from "../../../store/actions";
import { postApiResource, getApiResource } from '../../../utils/network';
import { API_CALL_ERR_PROC, API_CALL_ERR_CHECK, API_CALL_ERRORS, URI_API_SERVER } from "../../../constants/api";
import { apiGetData } from '../../../utils/common';

const mergeErrData = (callErrData, res) => {
    let resValue;
    for (let i in callErrData) {
        for (let j in res) {
            if (
                (callErrData[i].imsi === res[j].imsi) &&
                (callErrData[i].callType === res[j].callType) &&
                (callErrData[i].duration === res[j].duration)
            ) {
                callErrData[i].newCallId = res[j].callId;
                callErrData[i].newStartTime = res[j].startTime;
                callErrData[i].delDate = res[j].callErrDelDate;
            }
        }
    }
    return callErrData;
}

const CallErrControl = function () {
    const userName = useSelector((state) => state.userNameReducer.userName);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const filteredData = useSelector((state) => state.callErrReducer.filteredData);
    const selectedData = useSelector((state) => state.callErrReducer.selData);
    const callErrData = useSelector((state) => state.callErrReducer.errData);
    const dispatchProcData = useDispatch();
    const dispatchData = useDispatch();
    const [checkCallsParam, setCheckCallsParam] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const { Title } = Typography;

    const CallErrRequest = async (url) => {
        // dispatchProcData(setValue({ procData: null }));
        // if (Array.isArray(callErrData) && callErrData.length > 0) {
        if (Array.isArray(selectedData) && selectedData.length > 0) {
            // const body = JSON.stringify(callErrData.map(v => ({
            const body = JSON.stringify(selectedData.map(v => ({
                callId: v.callId, startTime: v.startTime, imsi: v.imsi, duration: v.duration, callType: v.callType, callErrType: v.errType, callErrDelDate: null,
                naviUser: userName
            })));
            setIsLoading(true);
            const res = await postApiResource(url, body);
            setIsLoading(false);
            if (res) {
                dispatchProcData(setCallErr({ procData: res }));
            } else {
                message.error('Ошибка обработки! См.лог-файл сервиса');
            }
        } else {
            console.log("data is null");
        }
    }

    const fetchData = async () => {
        setIsLoading(true);
        // const res = await getApiResource(`${URI_API_SERVER}/${dbName}/${API_CALL_ERRORS}`);
        const res = await apiGetData(`${URI_API_SERVER}/${dbName}/${API_CALL_ERRORS}`);
        setIsLoading(false);
        if (res?.error) {
            Modal.error({
                title: "call_error",
                content: "Ошибка чтения call_errors"
            });
        } else {
            dispatchData(setCallErr({ errData: res, }));
        }
    }

    const handleProc = async () => {
        CallErrRequest(`${URI_API_SERVER}/${dbName}/${API_CALL_ERR_PROC}`);
    }

    const handleCheck = async () => {
        CallErrRequest(`${URI_API_SERVER}/${dbName}/${API_CALL_ERR_CHECK}`);
    }

    const handlerescan = () => {
        dispatchData(clearErrData());
        fetchData();
    }

    return (
        <div>
            {
                isLoading
                    ? <h2>Подождите, идет обновление информации..</h2>
                    : <Flex gap="small" wrap="wrap" style={{ marginBottom: 10 }}>
                        <Button key={3} type="primary" onClick={handlerescan}>Обновить</Button>
                        <Button key={1} type="primary" disabled={selectedData.length == 0} onClick={handleProc}>Перетарифицировать</Button>
                        <Button key={2} type="primary" onClick={handleCheck}>Проверить</Button>
                        <Space style={{ marginLeft: 100 }}>
                            <Title mark level={5}>
                                БД:{dbName}
                            </Title>
                            <Title level={5}>
                                Всего:{callErrData.length} Отфильтровано:{filteredData.length} Выбрано:{selectedData.length}
                            </Title>

                        </Space>
                    </Flex>
            }
        </div>
    )
}
export default CallErrControl;