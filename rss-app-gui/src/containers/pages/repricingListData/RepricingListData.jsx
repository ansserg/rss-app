import React from "react";
import { useSelector } from "react-redux";
import { Table } from 'antd';


const RepricingListData = function () {

    const data = useSelector((state) => state.xlsDataReducer.data);

    const ds =
        data.sort((v1, v2) => v1.couname.localeCompare(v2.couname))
            .map(v => ({
                ...v, key: v.attrId, navidate: new Date(v.navidate).toLocaleString("ru-RU")
                , deldate: v.deldate ? new Date(v.deldate).toLocaleString("ru-RU") : null
            }));
    const columns = [
        {
            title: 'NAME_R',
            dataIndex: 'couname',
            key: 'namer',
            width: '25%',
        },
        {
            title: 'MOC_L',
            dataIndex: 'mocl',
            key: 'mocl',
            width: '10%',
        },
        {
            title: 'MOC_R',
            dataIndex: 'mocr',
            key: 'mocr',
            width: '10%',
        },
        {
            title: 'MOC_O',
            dataIndex: 'moco',
            key: 'moc0',
            width: '10%',
        },
        {
            title: 'MOC_S',
            dataIndex: 'mocs',
            key: 'mocs',
            width: '10%',
        },
        {
            title: 'MTC',
            dataIndex: 'mtc',
            key: 'mtc',
            width: '10%',
        },
        {
            title: 'SMSMO',
            dataIndex: 'smsmo',
            key: 'smsmo',
            width: '10%',
        },
        {
            title: 'GPRS',
            dataIndex: 'gprs',
            key: 'gprs',
            width: '10%',
        },
    ]

    return (
        <>
            <Table
                dataSource={ds}
                columns={columns}
                pagination={{
                    pageSizeOptions: [10, 20, 50, 100, 1000],
                }}
                scroll={{
                    y: 550,
                }}
            />
        </>
    )
}

export default RepricingListData;