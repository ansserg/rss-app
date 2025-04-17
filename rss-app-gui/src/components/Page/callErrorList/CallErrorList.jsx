import React, { useState, useEffect, useRef } from 'react';
import { useSelector, useDispatch } from "react-redux";
import { Button, Divider, Input, Space, Table, notification } from 'antd';
import { mergeErrData } from "../../../services/callErrSrv";
import CallErrControl from '../callErrControl';
import { setCallErr, clearSelectedErrData, clearFilteredErrData } from '../../../store/actions';
import { SearchOutlined } from '@ant-design/icons';
import { useGetColumnSearchProps } from '../../../utils/common';
import { DB_UNDEF } from '../../../constants/api';

const CallErrorList = function ({ data }) {
    const callErrData = useSelector((state) => state.callErrReducer.errData);
    const procErrData = useSelector((state) => state.callErrReducer.procData);
    const [srcData, setSrcData] = useState();
    const [selectedData, setSelectedData] = useState([]);
    const dispatchSelData = useDispatch();
    let dataSource = data.map((v, i) => ({ ...v, key: v.callId + v.startTime }));
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const searchInput = useRef(null);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);

    const columns = [
        {
            title: 'start_time',
            dataIndex: 'startTime',
            width: 250,
            fixed: 'left',
            key: 'startTime',
        },
        {
            title: 'imsi',
            dataIndex: 'imsi',
            width: 150,
            fixed: 'left',
            key: 'imsi',
            ...useGetColumnSearchProps('imsi'),
            // ...getColumnSearchProps('imsi'),

        },
        {
            title: 'destination',
            dataIndex: 'destination',
            width: 150,
            key: 'destination',
        },
        {
            title: 'duration',
            dataIndex: 'duration',
            width: 90,
            key: 'duration',
        },
        {
            title: 'data_vol',
            dataIndex: 'dataVolume',
            width: 100,
            key: 'dataVolume',
        },
        {
            title: 'cltp',
            dataIndex: 'callType',
            width: 65,
            key: 'callType',
            filters: [
                {
                    text: 'CLTP_ID=1',
                    value: 1
                },
                {
                    text: 'CLTP_ID=2',
                    value: 2
                },
                {
                    text: 'CLTP_ID=6',
                    value: 6
                }
            ],
            onFilter: (value, record) => record.errType === value,
        },
        {
            title: 'srvc',
            dataIndex: 'serviceCode',
            width: 65,
            key: 'serviceCode',
            filters: [
                {
                    text: 'T11',
                    value: 'T11'
                },
                {
                    text: 'T12',
                    value: 'T12'
                },
                {
                    text: 'T21',
                    value: 'T21'
                },
                {
                    text: 'T22',
                    value: 'T22'
                },
                ,
                {
                    text: 'PS',
                    value: 'PS'
                }
            ],
            onFilter: (value, record) => record.serviceCode.indexOf(value) === 0,
        },
        {
            title: 'err_msg',
            dataIndex: 'errorMsg',
            width: 125,
            key: 'errorMsg',
            filters: [
                {
                    text: 'Duplicate call',
                    value: 'Duplicate call'
                },
                {
                    text: 'Value out of range',
                    value: 'Value out of range'
                },
                {
                    text: 'Code and Action which must not be transferred',
                    value: 'A combination of Service Code and Action which must not be transferred'
                },
            ],
            onFilter: (value, record) => record.errorMsg.indexOf(value) === 0,
        },
        {
            title: 'err_code',
            dataIndex: 'errorCode',
            width: 90,
            key: 'errorCode',
        },
        {
            title: 'ertp',
            dataIndex: 'errType',
            width: 65,
            key: 'errType',
            filters: [
                {
                    text: '0-дубли',
                    value: 0
                },
                {
                    text: '1-част.запись',
                    value: 1
                },
                {
                    text: '2-charge',
                    value: 2
                },
                {
                    text: '3-action',
                    value: 3
                },
                {
                    text: '4-част.зап.+',
                    value: 4
                },
            ],
            onFilter: (value, record) => record.errType === value,
        },
        {
            title: 'call_id',
            dataIndex: 'callId',
            width: 120,
            key: 'callId',
        },
        {
            title: 'call_err_del_date',
            dataIndex: 'delDate',
            width: 250,
            key: 'delDate',
        },
        {
            title: 'new_call_id',
            dataIndex: 'newCallId',
            width: 125,
            key: 'newCallId',
        },
        {
            title: 'new_start_time',
            dataIndex: 'newStartTime',
            width: 250,
            key: 'newStartTime',
        },
    ]

    const rowSelection = {
        onChange: (selKeys, selRows) => {
            // setSelectedData(selRows);
            dispatchSelData(setCallErr({ selData: selRows }));
            // console.log(`selKeys:${selKeys}`, 'selRows:', selRows);
        }
    }

    const onChange = (pagination, filters, sorter, extra) => {
        dispatchSelData(clearFilteredErrData());
        dispatchSelData(setCallErr({ filteredData: extra.currentDataSource }));
        // console.log('onChange..', extra.currentDataSource);
    }

    useEffect(() => {
        if (Array.isArray(callErrData) && Array.isArray(procErrData)) {
            setSrcData(mergeErrData(callErrData, procErrData).map(v => ({ ...v, key: v.callId })));
            setSelectedData([]);
        }
    }, [procErrData, callErrData])

    return (
        <>
            {dbName.includes(DB_UNDEF)
                ? notification.open({
                    message: 'Ошибка',
                    description: 'Укажите базу данных!',
                    className: 'notification-warn',
                })
                :
                <div className="App">
                    <CallErrControl />
                    {/* <Divider /> */}
                    <Table
                        rowSelection={{
                            type: "checkbox",
                            ...rowSelection,
                        }}
                        dataSource={dataSource}
                        columns={columns}
                        pagination={{
                            pageSizeOptions: [100, 1000, 2000, 5000, 10000],
                        }}

                        scroll={{
                            y: 550,
                            x: 1200,
                        }}
                        onChange={onChange}
                        bordered
                        title={() => 'Ошибки обработки визитерского трафика (CALL_ERRORS)'}
                    />;
                </div>}
        </>
    )
}
export default CallErrorList;

