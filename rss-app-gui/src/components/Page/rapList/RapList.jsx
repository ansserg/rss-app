import React, { useRef, useState } from 'react';
import { useDispatch } from "react-redux";
import { Table } from 'antd';
import { setRap } from '../../../store/actions';
import { useGetColumnSearchProps } from '../../../utils/common';

const RapList = function ({ data }) {
  const dispatchSelData = useDispatch();
  const dispatchFilteredData = useDispatch();
  const ds = data.map(v => ({ ...v, key: v.call_id+v.startTime }));
  // const ds = data.map(v => ({ ...v, key: v.call_id, startTime: new Date(v.startTime).toLocaleString("ru-RU") }));

  const columns = [
    {
      title: 'error_message',
      dataIndex: 'errorMsg',
      key: 'errorMsg',
    },
    {
      title: 'error_code',
      dataIndex: 'errorCode',
      key: 'errorCode',
      filters: [
        {
          text: '200',
          value: 200
        },
        {
          text: '255',
          value: 255
        },
      ],
      onFilter: (value, record) => record.errorCode === value,
    },
    {
      title: 'imsi',
      dataIndex: 'imsi',
      key: 'imsi',
      ...useGetColumnSearchProps('imsi'),
    },
    {
      title: 'start_time',
      dataIndex: 'startTime',
      key: 'startTime',
    },
    {
      title: 'duration',
      dataIndex: 'duration',
      key: 'duration',
    },
    {
      title: 'data_volume',
      dataIndex: 'dataVolume',
      key: 'DataVolume',
    },
    {
      title: 'file_name',
      dataIndex: 'fileName',
      key: 'fileName',
    },
    {
      title: 'vpmn_name',
      dataIndex: 'vpmnName',
      key: 'vpmnName',
      ...useGetColumnSearchProps('vpmnName'),
    },
    {
      title: 'hpmn_name',
      dataIndex: 'hpmnName',
      key: 'hpmnName',
      ...useGetColumnSearchProps('hpmnName'),
    },
    {
      title: 'Call_id',
      dataIndex: 'callId',
      key: 'callId',
    },
    {
      title: 'del_date',
      dataIndex: 'delDate',
      key: 'delDate',
    },
    {
      title: 'new_call_id',
      dataIndex: 'newCallId',
      key: 'newCallId',
    },
  ]

  const rowSelection = {
    onChange: (selKeys, selRows) => {
      dispatchSelData(setRap({ selData: selRows }));
      console.log(`selKeys:${selKeys}`, 'selRows:', selRows);
    }
  }

  const onChange = (pagination, filters, sorter, extra) => {
    dispatchFilteredData(setRap({ filteredData: extra.currentDataSource }));
    console.log('onChange..', extra.currentDataSource);
  }

  return (
      <div>
        <Table
          rowSelection={{
            type: "checkbox",
            ...rowSelection,
          }}
          dataSource={ds}
          columns={columns}
          bordered
          scroll={{
            y: 500,
          }}
          onChange={onChange}
        />
      </div>
  )
}
export default RapList;
