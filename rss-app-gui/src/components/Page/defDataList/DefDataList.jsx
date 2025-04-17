import React, { useState } from "react";
import { useSelector } from 'react-redux';
import { Table, Button, notification } from "antd";
import { apiGetData, useGetColumnSearchProps } from '../../../utils/common';
import { URI_API_SERVER, API_DEFRU } from "../../../constants/api";
import SysLogsList from "../sysLogsList/SysLogsList";
import { openIfoModal } from "../../../utils/network";

const DefDataList = function () {

    const dataSource = useSelector((state) => state.xlsDataReducer.data);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const task = useSelector((state) => state.xlsDataReducer.task);
    const URL_API = URI_API_SERVER + "/" + dbName;
    const [isLoading, setIsLoading] = useState(false);

    const columns = [
        {
            title: 'DEF',
            dataIndex: 'code',
            width: 50,
            key: 'code',
            fixed: 'left',
            ...useGetColumnSearchProps('code'),
        },
        {
            title: 'начало диапазона',
            dataIndex: 'numb',
            width: 75,
            key: 'numb',
            fixed: 'left',
            ...useGetColumnSearchProps('numb'),
        },
        {
            title: 'окончание диапазона',
            dataIndex: 'nume',
            width: 150,
            key: 'name',
            fixed: 'left',
        },
        {
            title: 'Оператор',
            dataIndex: 'operator',
            width: 200,
            key: 'operator',
            ...useGetColumnSearchProps('operator'),
        },
        {
            title: 'Регион',
            dataIndex: 'region',
            width: 150,
            key: 'region',
            ...useGetColumnSearchProps('region'),
        },
        {
            title: 'признак филиала МГФ',
            dataIndex: 'filial',
            width: 150,
            key: 'filial',
            ...useGetColumnSearchProps('filial'),
        },
        {
            title: 'код региона',
            dataIndex: 'regionCode',
            width: 150,
            key: 'regionCode',
        },
        {
            title: 'RN',
            dataIndex: 'rn',
            width: 50,
            key: 'rn',
        },
        {
            title: 'Статус',
            dataIndex: 'status',
            width: 50,
            key: 'status',
        },
    ]

    const handleOk = async () => {
        if (task > 0) {
            const url = `${URL_API}/${API_DEFRU}/${task}`;
            setIsLoading(true);
            // const body = await getApiResource(url);
            const body = await apiGetData(url);
            setIsLoading(false);
            if (!body?.error) {
                openIfoModal("Ввод DEF РФ", <SysLogsList data={body} />)
            } else {
                notification.open({
                    message: `Ошибка`,
                    description: `Ошибка ввода изменений DEF ${body?.error?.message}!`,
                    duration:0,
                    className: 'notification-warn',
                })
            }
        }
    }

    return (
        <>
            {
                dataSource
                    ? <>
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'right', marginBottom: 30, marginRight: 30 }}>
                            <span style={{ marginRight: 30 }}>
                                БД:{dbName}
                            </span>
                            <Button type="primary" onClick={handleOk}>Ввод изменений</Button>
                        </div>
                        {
                            isLoading
                                ? <h2>Подождите, идет ввод изменений префиксов DEF РФ..</h2>
                                : <Table
                                    dataSource={dataSource}
                                    columns={columns}
                                    pagination={{
                                        default: 50,
                                        pageSizeOptions: [100, 1000,],
                                    }}
                                    scroll={{
                                        y: 550,
                                        x: 1200,
                                    }}
                                />
                        }

                    </>
                    : <>
                    </>
            }
        </>
    )
}

export default DefDataList;