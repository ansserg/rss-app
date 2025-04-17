import { Table } from "antd";
import React from "react";

export const LacTmpl = () => {
    const columns = [
        // {
        //     title: "ФИЛИАЛ_ОБСЛУЖИВАНИЯ_БС",
        //     dataIndex: 'lac',
        //     width: 100,
        //     key: 'name',
        // },
        {
            title: "РЕГИОН_ОБСЛУЖИВАНИЯ_БС",
            dataIndex: 'region',
            width: 150,
            key: 'region',
        },
        {
            title: "LAC_TAC",
            dataIndex: 'branch',
            width: 150,
            key: 'branch',
        },
    ]
    return (
        <Table
            columns={columns}
        />
    )
}