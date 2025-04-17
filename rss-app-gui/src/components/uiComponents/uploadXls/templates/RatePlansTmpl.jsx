import { Button, Divider, Table } from "antd";
import React from "react";

export const RatePlansTmpl = () => {
    const columns_rtpl = [
        {
            title: "Attrdef",
            dataIndex: 'attrdef',
            width: 100,
            key: 'attrdef',
        },
        {
            title: "Attrvalue",
            dataIndex: 'attrval',
            width: 150,
            key: 'attrval',
        },
        {
            title: "Attrvalue2",
            dataIndex: 'attrval2',
            width: 150,
            key: 'attrval2',
        },
    ];
    const columns_hight_cost = [
        {
            title: "НАПРАВЛЕНИЕ",
            dataIndex: 'drcl_name',
            width: 100,
            key: 'drcl_name',
        },
        {
            title: "КОД НАПРАВЛЕНИЯ",
            dataIndex: 'drcl_code',
            width: 150,
            key: 'drcl_code',
        },
        {
            title: "ВЫДЕЛЕНО ИЗ НАПРАВЛЕНИЯ",
            dataIndex: 'drcl_excl',
            width: 150,
            key: 'drcl_excl',
        },
        {
            title: "",
            dataIndex: '',
            width: 150,
            key: '',
        },
    ]
    return (
        <>
            <Table
                columns={columns_rtpl}
            />
            <Button>ТП НОВЫЙ</Button>  <Button>НОВЫЙ ТП</Button>  <Button>ТП ИЗМЕНЕНИЕ</Button>  <Button>ИЗМЕНЕНИЕ ТП</Button>
            {/* <p>ТП НОВЫЙ / НОВЫЙ ТП / ТП ИЗМЕНЕНИЕ / ИЗМЕНЕНИЕ ТП </p> */}
            <Divider/>
            <Table
                columns={columns_hight_cost}
            />
            <Button>ВЫСОКОЗАТРАТНЫЕ НАПРАВЛЕНИЯ</Button>
            {/* <p>ВЫСОКОЗАТРАТНЫЕ НАПРАВЛЕНИЯ</p> */}
        </>

    )
}
