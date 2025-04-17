import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from 'react-redux';
import { Table, Flex, Button, Space, Typography } from 'antd';
import RapList from "../../../components/Page/rapList";
import TextList from "../../../components/Page/textList";
import { getApiResource, postApiResource } from "../../../utils/network";
import { API_GET_RAP, API_GET_MSG_RAP_LIST, API_RAP_REPROC, URI_API_SERVER, DB_NAME_RSSREP, DB_UNDEF } from "../../../constants/api";
import { clearRap } from "../../../store/actions";
import { apiJsonPostData } from "../../../utils/common";

const RapPage = function () {
    const [data, setData] = useState(null);
    const [errApi, setErrApi] = useState(false);
    const [msgRapList, setMsgRapList] = useState();
    const user = useSelector((state) => state.userNameReducer.userName);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const dispatchSelData = useDispatch();
    const filteredData = useSelector((state) => state.rapReducer.filteredData);
    const selectedData = useSelector((state) => state.rapReducer.selData);
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState("");
    const { Title } = Typography;

    const getResource = async (url) => {
        setIsLoading(true);
        setMessage("Подождите, идет загрузка...");
        const res = await getApiResource(url);
        console.log(res);
        if (res) {
            setData(res);
            // dispatchSelData(clearRap());
            setErrApi(false);
            // const msg = await postApiResource(`${URI_API_SERVER}/${dbName}/${API_GET_MSG_RAP_LIST}`, JSON.stringify(res));
            const msg = await apiJsonPostData(`${URI_API_SERVER}/${dbName}/${API_GET_MSG_RAP_LIST}`, JSON.stringify(res));
            setMsgRapList(msg);
        } else {
            setErrApi(true);
        }
        setIsLoading(false);

    }
    useEffect(() => {
        !dbName.includes(DB_UNDEF)
            && getResource(`${URI_API_SERVER}/${DB_NAME_RSSREP}/${API_GET_RAP}`);
        // dispatchSelData(clearRap());
    }, []);

    const handleRapProc = async () => {
        setIsLoading(true);
        setMessage("Подождите, идет обработка..")
        // const calls = await postApiResource(`${URI_API_SERVER}/${dbName}/${API_RAP_REPROC}/${user}`, JSON.stringify(selectedData));
        const calls = await apiJsonPostData(`${URI_API_SERVER}/${dbName}/${API_RAP_REPROC}/${user}`, JSON.stringify(selectedData));
        // dispatchSelData(clearRap());
        setIsLoading(false);
    }

    const handleRefresh = () => {
        getResource(`${URI_API_SERVER}/${DB_NAME_RSSREP}/${API_GET_RAP}`);
    }
    return (
        <>
            {
                isLoading
                    ? <h2>{message}</h2>
                    : errApi
                        ? <h2>Error</h2>
                        : <div>
                            <Flex gap="small" wrap="wrap" style={{ marginBottom: 10 }}>
                                <Button key={1} type="primary" onClick={handleRefresh}>Обновить</Button>
                                <Button key={2} type="primary" disabled={selectedData.length == 0} onClick={handleRapProc}>Перетарифицировать</Button>
                                <Space align='center' size='middle'>
                                    <Title mark level={5}>
                                        База данных:{dbName}
                                    </Title>
                                    <Title level={5}>
                                        Отфильтровано:{filteredData.length} Выбрано:{selectedData.length}
                                    </Title>

                                </Space>
                            </Flex>
                            {msgRapList && <TextList msg={msgRapList} />}
                            {data && <RapList data={data} />}
                        </div>

            }

        </>
    )
}
export default RapPage;