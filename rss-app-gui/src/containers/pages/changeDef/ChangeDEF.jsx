import React from "react";
import { useSelector, useDispatch } from "react-redux";
import UploadXls from "../../../components/uiComponents/uploadXls/UploadXls";
import { API_UPLOAD_DEF, URI_API_SERVER ,API_GET_DEF} from "../../../constants/api";
import { Divider, Typography } from "antd";
import DefDataList from "../../../components/Page/defDataList/DefDataList";
import { DefTmpl } from "../../../components/uiComponents/uploadXls/templates/DefTmpl";

const ChangeDEF = function () {
    const taskNumber = useSelector((state) => state.xlsDataReducer.task);
    const dataTbl = useSelector((state) => state.xlsDataReducer.data);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const URL_API = URI_API_SERVER + "/" + dbName;
    const priceData = useDispatch();
    const { Text } = Typography;
    return (
        <>
            <Text mark level={5}>
                База данных:{dbName}
            </Text>
            <UploadXls url={`${URL_API}/${API_UPLOAD_DEF}`} urlnotfile={`${URL_API}/${API_GET_DEF}`} content={<DefTmpl/>} />
            <Divider />
            <DefDataList />
        </>
    )
}

export default ChangeDEF;