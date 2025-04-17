import { Button, Divider, Table } from "antd";
import React from "react";

export const RePriceDataTmpl = () => {
    const columns_rtpl = [
        {
            title: "%[Страна|NAME_R]%",
            dataIndex: 'mocl',
            width: 100,
            key: 'mocl',
        },
        {
            title: "[%[Исходящие вызовы|MOC]%по стране пребывания%]|VMOC-L",
            dataIndex: 'mocl',
            width: 100,
            key: 'mocl',
        },
        {
            title: "[%[Исходящие вызовы|MOC]%]в Россию]%]|VMOC-R",
            dataIndex: 'mocr',
            width: 100,
            key: 'mocr',
        },
        {
            title: "[%[Исходящие вызовы|MOC]%на[%глобальные международные|спутниковые сети]%]|VMOC-S",
            dataIndex: 'mocs',
            width: 100,
            key: 'mocs'
        },
        {
            title: "[%[Исходящие вызовы|MOC]%[на прочие направления|в другие страны]%]|VMOC-O",
            dataIndex: 'moco',
            width: 100,
            key: 'moco',
        },
        {
            title: "[%[Входящие вызовы|(MTC)]%]|VMT",
            dataIndex: 'mts',
            width: 100,
            key: 'mts',
        },
        {
            title: "%[Исходящие короткие сообщения|SMS MO|SMSMO]%",
            dataIndex: 'smsmo',
            width: 100,
            key: 'smsmo',
        },
        {
            title: "[%[Пакетная передача данных|GPRS]%]|DATA",
            dataIndex: 'gprs',
            width: 100,
            key: 'gprs',
        },
    ];

    return (
        <>
            <Table
                columns={columns_rtpl}
            />
            <Button>Тарифы</Button>
        </>

    )
}