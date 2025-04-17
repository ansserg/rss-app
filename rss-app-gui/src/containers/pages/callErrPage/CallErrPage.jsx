import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { Divider, Modal } from 'antd';
import CallErrorList from "../../../components/Page/callErrorList";
import CallErrControl from "../../../components/Page/callErrControl";
import { API_CALL_ERRORS, DB_UNDEF, URI_API_SERVER } from "../../../constants/api";
import { setCallErr } from "../../../store/actions";
import { getApiResource } from "../../../utils/network";
import { apiGetData } from "../../../utils/common";

const CallErrPage = function () {
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const dispatchData = useDispatch();
    const callErrData = useSelector((state) => state.callErrReducer.errData);
    const [sourceData, setSourceData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const fetchData = async () => {
        setIsLoading(true);
        // const res = await getApiResource(`${URI_API_SERVER}/${dbName}/${API_CALL_ERRORS}`);
        const res = await apiGetData(`${URI_API_SERVER}/${dbName}/${API_CALL_ERRORS}`);
        setIsLoading(false);
        if (res === false) {
            Modal.error({
                title: "call_error",
                content: "Ошибка чтения call_errors"
            });
        } else {
            setSourceData(res);
            dispatchData(setCallErr({ errData: res, filteredData: res, selData: [] }));
        }
    }
    useEffect(() => {
        !dbName.includes(DB_UNDEF)
            && fetchData();
    }, []);

    return (
        <>
            {
                // (Array.isArray(callErrData) && callErrData.length > 0) &&
                <div>
                    {isLoading
                        ? <h2>Подождите, идет загрузка информации..</h2>
                        : <>
                            <CallErrorList data={callErrData} />
                        </>

                    }
                </div>
            }
        </>
    )
}
export default CallErrPage;