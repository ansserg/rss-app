import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { notification } from 'antd';
import { initValue } from "../../../store/actions";
import UploadXls from "../../../components/uiComponents/uploadXls/UploadXls";
import {URI_API_SERVER, DB_UNDEF } from "../../../constants/api";

const UploadXlsData = function ({ url, urlnotfile, content, panelctl, datacomponent }) {
    const taskNumber = useSelector((state) => state.xlsDataReducer.task);
    const dataTbl = useSelector((state) => state.xlsDataReducer.data);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const URL_API = URI_API_SERVER + "/" + dbName;
    const priceData = useDispatch();
    useEffect(() => {
        priceData(initValue());
    }, [])
    console.log("UploadXlsData:url=",url);

    return (
        dbName.includes(DB_UNDEF)
            ?
            notification.open({
                message: 'Ошибка',
                description: 'Укажите базу данных!',
                className: 'notification-warn',
            })
            : <>
                {
                    dataTbl
                        ? <div style={{ paddingLeft: 10 }}>
                            {panelctl}
                            {datacomponent}
                        </div>
                        : <UploadXls url={`${URL_API}/${url}`} urlnotfile={`${URL_API}/${urlnotfile}`} content={content} />
                }
            </>
    )
}
export default UploadXlsData;