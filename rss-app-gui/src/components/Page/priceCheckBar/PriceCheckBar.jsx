import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from 'react-redux';
import { notification, Button, Flex, Modal, Typography, DatePicker } from 'antd';
import dayjs from "dayjs";
import SysLogsList from "../sysLogsList";
import RtplDataList from "../rtplDataList";
import CallData from "../callData";
import {
    API_CHANGE_RTPL, API_VIEW_RTPL, API_CDR_TEST, API_CHECK_PRICE_LIST_MAP, API_LOAD_RMOP, DEF_PARAM_START_DATE, DEF_PARAM_TAP_LIST,
    API_CALL_DATA, API_PSET_DIR_PARAM, DEF_PARAM_RTPL_NAME, API_GET_RTPL_ID, API_SET_HRS_RTPL_ID, URI_API_SERVER
} from "../../../constants/api";
import { initValue } from "../../../store/actions"
import { apiGetData, apiJsonPostData } from '../../../utils/common';

const PriceCheckBar = function () {
    const user = useSelector((state) => state.userNameReducer.userName);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const task = useSelector((state) => state.xlsDataReducer.task);
    const priceListData = useSelector((state) => state.xlsDataReducer.data);
    const priceData = useDispatch();
    const navigate = useNavigate();
    const URL_API = URI_API_SERVER + "/" + dbName;
    const [isLoading, setIsLoading] = useState(false);
    const [rmopParam, setRmopParam] = useState(null);
    const { Title } = Typography;

    const getDefParam = (attrName) => {
        return priceListData.filter((val) => {
            return val.attrdef == attrName;
        });
    }
    const startDateDef = getDefParam(DEF_PARAM_START_DATE)[0].attrvalue.substr(0, 10);
    console.log("startDateDef=", startDateDef);
    const startDate = getDefParam(DEF_PARAM_START_DATE)[0].attrvalue.replace(RegExp("[-:]", "g"), "").substr(0, 8);
    const [startDateCtl, setStartDateCtl] = useState(startDate);
    console.log('startDateCtl=', startDateCtl);
    const tapList = [].concat(...getDefParam(DEF_PARAM_TAP_LIST)
        .map(val => val.attrvalue)
        .map(val => val.split(",")))
        .map(v => v.trim());
    const rtplName = getDefParam(DEF_PARAM_RTPL_NAME)[0].attrvalue.trim();

    const openIfoModal = function (title, content) {
        Modal.info({
            title: title,
            width: "auto",
            content: content
        });
    }
    const openErrorModal = function (title, content) {
        Modal.error({
            title: title,
            content: content
        });
    }

    const handleCheckMapping = async () => {
        if (task > 0) {
            const url = `${URL_API}/${API_CHECK_PRICE_LIST_MAP}/${task}`;
            setIsLoading(true);
            const body = await apiGetData(url);
            setIsLoading(false);
            if (!body?.error) {
                openIfoModal("Проверка мппинга услуг ТП", <SysLogsList data={body} />);
            }
        } else {
            notification.open({
                message: 'Ошибка исходных данных:',
                description: 'Не задан RFC !',
                className: 'notification-warn',
            })
        }
    }

    const handleChangeRtpl = async () => {
        if (task > 0) {
            console.log("rtplName=" + rtplName);
            const url = `${URL_API}/${API_CHANGE_RTPL}/${task}`;
            setIsLoading(true);
            const body = await apiGetData(url);
            setIsLoading(false);
            if (!body?.error) {
                setIsLoading(true)
                const rtplId = await apiGetData(`${URL_API}/${API_GET_RTPL_ID}?name=${rtplName}`);
                setIsLoading(false);
                if (!rtplId?.error) {
                    console.log("rtplId=" + rtplId);
                    const rflw = JSON.stringify(rmopParam.map(v => v.rflwId));
                    console.log(rflw);
                    setIsLoading(true);
                    const res = await apiJsonPostData(`${URL_API}/${API_SET_HRS_RTPL_ID}/${rtplId}/${startDate}/${user}/${task}`, rflw);
                    setIsLoading(false);
                    if (!res?.error) {
                        openIfoModal("Ввод изменений в ТП", <SysLogsList data={[...body, ...res]} />);
                    }
                }
            }
        }
    };

    const handleViewRtpl = async () => {
        if (task > 0) {
            if (task > 0) {
                const url = `${URL_API}/${API_VIEW_RTPL}/${task}`;
                setIsLoading(true)
                // const body = await getApiResource(url);
                const body = await apiGetData(url);
                setIsLoading(false);
                if (!body?.error) {
                    openIfoModal(`Тарифный план:${body[0].rtplName}, rtpl_id:${body[0].rtplId}`, <RtplDataList data={body} />);
                }
            }
        }
    };

    const handleCreCdrs = async () => {
        if (rmopParam == true) {
            notification.open({
                message: 'Предупреждение:',
                description: 'ошибка чтения параметров операторов!',
                className: 'notification-warn',
            })
        } else {
            const body = JSON.stringify(rmopParam);
            setIsLoading(true);
            const cdrList = await apiJsonPostData(`${URL_API}/${API_CDR_TEST}/${startDateCtl}`, body);
            setIsLoading(false);
            if (!cdrList?.error) {
                notification.open({
                    message: 'Информационное сообщение',
                    description: 'Тестовые cdr сформированы успешно!',
                    className: 'notification-success',
                })
            }
        }
    };

    const handleTestCallView = async () => {
        if (rmopParam == true) {
            notification.open({
                message: 'Предупреждение:',
                description: 'Отсутствуют параметры операторов для проверки тарификации.\
                Проверка тарификации невозможна!"!',
                className: 'notification-warn',
            })
        } else {
            const rmopIdList = rmopParam.map(v => v.rmopId);
            const body = JSON.stringify(rmopIdList);
            setIsLoading(true);
            const callData = await apiJsonPostData(`${URL_API}/${API_CALL_DATA}/${startDateCtl}`, body);
            setIsLoading(false);
            if (Array.isArray(callData)) {
                if (callData.length === 0) {
                    notification.open({
                        message: 'Проверка тарификации:',
                        description: 'Данных нет',
                        className: 'notification-warn',
                    })
                } else {
                    const psetList = callData.map(v => v.psetId);
                    const rtplId = callData[0].rtplId;
                    const psBody = JSON.stringify(psetList);
                    setIsLoading(true);
                    const psetDirParam = await apiJsonPostData(`${URL_API}/${API_PSET_DIR_PARAM}/${rtplId}/${startDateCtl}`, psBody);
                    setIsLoading(false);
                    if (!psetDirParam?.error) {
                        const calls = callData.map(v => ({
                            ...v,
                            couName: psetDirParam.find(x => x.psetId == v.psetId) ? psetDirParam.find(x => x.psetId == v.psetId).couName : "",
                            rpdrName: psetDirParam.find(x => x.psetId == v.psetId) ? psetDirParam.find(x => x.psetId == v.psetId).dirName : ""
                        }))
                        openIfoModal("Результаты тарификации", <CallData data={calls} />);
                    }
                }
            } else {
                notification.open({
                    message: 'Проверка тарификации:',
                    description: callData?.message,
                    className: 'notification-warn',
                })
            }
        }
    }

    const handleLoadPrice = () => {
        priceData(initValue());
    }

    useEffect(() => {
        async function fetchData() {
            const tapBody = JSON.stringify(tapList);
            setIsLoading(true)
            const res = await apiJsonPostData(`${URL_API}/${API_LOAD_RMOP}/${startDate}`, tapBody);
            if (!res?.error) {
                setRmopParam(res);
            }
            setIsLoading(false);
        };
        fetchData();
    }, [priceListData]);

    const onChangeStartDate = (date, dateStr) => {
        setStartDateCtl(dateStr.replace(RegExp("[-:]", "g"), "").substr(0, 8));
        console.log("dateStr=", dateStr);
        console.log("startDateCtl=", startDateCtl);
    }

    return (
        isLoading
            ? <h2>Подождите, идет загрузка информации..</h2>
            : <>
                <Flex gap="small" wrap="wrap" style={{ marginBottom: 10 }}>
                    <Button key={1} type="primary" onClick={handleCheckMapping}>Маппинг услуг</Button>
                    <Button key={2} type="primary" onClick={handleChangeRtpl}>Ввод ТП</Button>
                    <Button key={3} type="primary" onClick={handleViewRtpl}>Просмотр ТП</Button>
                    <Button key={4} type="primary" onClick={handleCreCdrs}>Тестовые cdr</Button>
                    <Button key={5} type="primary" onClick={handleTestCallView}>Проверка тарификации</Button>
                    <Button key={6} type="primary" onClick={handleLoadPrice}>Новый прайс-лист</Button>
                    <DatePicker size={'small'} onChange={onChangeStartDate} defaultValue={dayjs(`${startDateDef}`)} />
                </Flex>
                <div style={{ marginLeft: 'auto', }}>
                    <Title mark level={5} >БД:{dbName}</Title>
                </div>
            </>

    );
}
export default PriceCheckBar;

