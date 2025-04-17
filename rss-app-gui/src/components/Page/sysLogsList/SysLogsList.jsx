import React from 'react';
import { Table } from 'antd';
import { useGetColumnSearchProps } from '../../../utils/common';

const SysLogsList = function ({ data }) {
  const ds = data.map(v => ({
    ...v, key: v.slogId
    , logDate: new Date(v.logDate).toLocaleString("ru-RU")
  }));
  const columns = [
    {
      title: 'slogId',
      dataIndex: 'slogId',
      key: 'slogId',
    },
    {
      title: 'message',
      dataIndex: 'message',
      ...useGetColumnSearchProps("message"),
      key: 'message',
    },
    {
      title: 'apmt_id',
      dataIndex: 'apmt',
      key: 'apmt',
      filters: [
        {
          text: '1',
          value: 1
        },
        {
          text: '2',
          value: 2
        },
        {
          text: '3',
          value: 3
        },
        {
          text: '4',
          value: 4
        },
      ],
      onFilter: (value, record) => record.apmt === value,
    },
    {
      title: 'process',
      dataIndex: 'process',
      key: 'process',
    },
    {
      title: 'log_date',
      dataIndex: 'logDate',
      key: 'logDate',
    },
  ]
  return (
    <div className="App">
      <Table
        dataSource={ds}
        columns={columns}
        pagination={{
          defaultPageSize:50,
          pageSizeOptions: [100, 1000, 2000, 5000, 10000],
        }}
      />
    </div>
  )
}
export default SysLogsList;

