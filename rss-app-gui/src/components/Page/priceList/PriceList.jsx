import React from 'react';
import { useSelector } from "react-redux";
import { Table } from 'antd';
import { useGetColumnSearchProps } from '../../../utils/common';


const PriceList = function () {
  const data = useSelector((state) => state.xlsDataReducer.data);
  const ds =
    data.sort((v1, v2) => { return v1.attrId - v2.attrId })
      .map(v => ({
        ...v, key: v.attrId, navidate: new Date(v.navidate).toLocaleString("ru-RU")
        , deldate: v.deldate ? new Date(v.deldate).toLocaleString("ru-RU") : null
      }));
  const columns = [
    {
      title: 'attrId',
      dataIndex: 'attrId',
      key: 'attrId',
      width:'5%',
    },
    {
      title: 'attrdef',
      dataIndex: 'attrdef',
      key: 'attrdef',
      dataIndex: 'attrdef',
      ...useGetColumnSearchProps("attrdef"),
    },
    {
      title: 'attrvalue',
      dataIndex: 'attrvalue',
      key: 'attrvalue',
    },
    {
      title: 'attrvalue2',
      dataIndex: 'attrvalue2',
      key: 'attrvalue2',
      width:'15%',
    },
    {
      title: 'task',
      dataIndex: 'task',
      key: 'task',
      width:"5%",
    },
    {
      title: 'navidate',
      dataIndex: 'navidate',
      key: 'navidate',
      width:'10%',
    },
    // {
    //   title: 'deldate',
    //   dataIndex: 'deldate',
    //   key: 'deldate',
    // },
  ]
  return (
    <div className="App">
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
    </div>
  )
}
export default PriceList;