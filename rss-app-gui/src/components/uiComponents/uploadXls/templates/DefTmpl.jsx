import { Table } from "antd";
import React from "react";

export const DefTmpl = () => {
    const columns = [
        {
            title: "DEF",
            dataIndex: 'def',
            width: 100,
            key: 'def',
        },
        {
            title: "НАЧАЛО ДИАПАЗОНА",
            dataIndex: 'start_range',
            width: 150,
            key: 'start_range',
        },
        {
            title: "ОКОНЧАНИЕ ДИАПАЗОНА",
            dataIndex: 'end_range',
            width: 150,
            key: 'end_range',
        },
        {
            title: "ОПЕРАТОР",
            dataIndex: 'operator',
            width: 150,
            key: 'operator',
        },
        {
            title: "ПРИЗНАК ФИЛИАЛА МГФ",
            dataIndex: 'branch',
            width: 150,
            key: 'branch',
        },
        {
            title: "РЕГИОН",
            dataIndex: 'region',
            width: 150,
            key: 'region',
        },
        {
            title: "КОД РЕГИОНА",
            dataIndex: 'region_code',
            width: 150,
            key: 'region_code',
        },
        {
            title: "RN",
            dataIndex: 'rn',
            width: 150,
            key: 'rn',
        },
        {
            title: "СТАТУС",
            dataIndex: 'status',
            width: 150,
            key: 'status',
        },
    ]
    return (
        <Table
            columns={columns}
        />
    )
}