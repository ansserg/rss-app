import React from 'react';
import { Table } from 'antd';
import { useGetColumnSearchProps } from '../../../utils/common';

const CallData = function ({data}) {
    const ds=data.map(v=>({...v,key:v.callId}));
    
    const columns = [
        {
          title: 'start_time',
          dataIndex: 'startTime',
          key: 'startTime',
        },
        {
          title: 'destination',
          dataIndex: 'destination',
          key: 'destination',
        },
        {
          title: 'duration',
          dataIndex: 'duration',
          key: 'duration',
        },
        {
          title: 'data_volume',
          dataIndex: 'dataVolume',
          key: 'dataVolume',
        },
        {
          title: 'in_charge',
          dataIndex: 'inCharge',
          key: 'inCharge',
        },
        {
          title: 'in_tax',
          dataIndex: 'inTax',
          key: 'inTax',
        },
        {
          title: 'service',
          dataIndex: 'service',
          key: 'service',
          ...useGetColumnSearchProps('service'),
        },
        {
            title: 'cltp',
            dataIndex: 'cltpCltpId',
            key: 'cltpCltpId',
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
          onFilter: (value, record) => record.callType === value,
          },
          {
            title: 'lcal_id',
            dataIndex: 'lcalId',
            key: 'lcalId',
          },
          {
            title: 'rtpl_id',
            dataIndex: 'rtplId',
            key: 'rtplId',
          },
          {
            title: 'country',
            dataIndex: 'couName',
            key: 'couName',
            ...useGetColumnSearchProps('couName'),
          },
          {
            title: 'rpdr_name',
            dataIndex: 'rpdrName',
            key: 'rpdrName',
            ...useGetColumnSearchProps('rpdrName'),
          },
          {
            title: 'lac',
            dataIndex: 'lac',
            key: 'lac',
          },
          {
            title: 'branch',
            dataIndex: 'branch',
            key: 'branch',
          },
          {
            title: 'rec_type',
            dataIndex: 'recType',
            key: 'recType',
          },
          {
            title: 'pset_id',
            dataIndex: 'psetId',
            key: 'psetId',
          },
          {
            title: 'srls_id',
            dataIndex: 'srlsId',
            key: 'srlsId',
          },
          {
            title: 'call_id',
            dataIndex: 'callId',
            key: 'callId',
          },
      ]
      return (
        <div className="App">
          <Table dataSource={ds} columns={columns} />;
        </div>
      )
}
export default CallData;

