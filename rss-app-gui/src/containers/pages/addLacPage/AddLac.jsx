import React from "react";
import { useSelector, useDispatch } from "react-redux";
import UploadXls from "../../../components/uiComponents/uploadXls/UploadXls";
import { API_UPLOAD_LAC, DB_UNDEF, URI_API_SERVER } from "../../../constants/api";
import { Divider, Typography, notification } from "antd";
import LacList from "../../../components/Page/LacList/LacList";
import { LacTmpl } from "../../../components/uiComponents/uploadXls/templates/LacTmpl";

const AddLac = function () {
    const taskNumber = useSelector((state) => state.xlsDataReducer.task);
    const dataTbl = useSelector((state) => state.xlsDataReducer.data);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const URL_API = URI_API_SERVER + "/" + dbName;
    const priceData = useDispatch();
    const { Text } = Typography;
    return (
        <>
            {dbName.includes(DB_UNDEF)
                ? notification.open({
                    message: 'Ошибка',
                    description: 'Укажите базу данных!',
                    className: 'notification-warn',
                })
                : <>
                    <Text mark level={5} style={{ marginLeft: 5, marginTop: 5 }}>
                        База данных:{dbName}
                    </Text>
                    <UploadXls url={`${URL_API}/${API_UPLOAD_LAC}`} content={<LacTmpl />} />
                    <Divider />
                    <LacList dbName={dbName} />
                </>
            }
        </>
    )
}

export default AddLac;