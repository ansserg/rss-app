import React from 'react';
import { Table } from 'antd';
import { useGetColumnSearchProps } from '../../../utils/common';

const RtplDataList = function ({ data }) {
    const ds = data.map((v, i) => ({ ...v, key: i, startDate: new Date(v.startDate).toLocaleString("ru-RU"), endDate: new Date(v.endDate).toLocaleString("ru-RU") }));
    const columns = [
        {
            title: 'услуга',
            dataIndex: 'tariffName',
            key: 'tariffName',
            ...useGetColumnSearchProps("tariffName"),
        },
        {
            title: 'тариф,$',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'округление',
            dataIndex: 'rndtName',
            key: 'rndtName',
        },
        {
            title: 'start_date',
            dataIndex: 'startDate',
            ...useGetColumnSearchProps("startDate"),
            key: 'startDate',
        },
        {
            title: 'end_date',
            dataIndex: 'endDate',
            key: 'endDate',
        },
        {
            title: 'lcal_id/rpdr_id',
            dataIndex: 'lcalId',
            key: 'lcalId',
            ...useGetColumnSearchProps("lcalId"),
        },
    ]

    return (
        <div className="App">
            <Table dataSource={ds} columns={columns} />;
        </div>
    )
}
export default RtplDataList;

