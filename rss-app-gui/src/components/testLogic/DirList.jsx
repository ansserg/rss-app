import React, {useState} from "react";
import DirItem from "./DirItem";
import { Space, Table, Tag, Row, Col } from 'antd';


const DirList = function () {
  let idSeq = 1;
  const [priceDir, setPriceDir] = useState([
    { id: idSeq++, dirName: "Russia", dirPrice: 0.025 },
    { id: idSeq++, dirName: "Chine", dirPrice: 0.03 },
    { id: idSeq++, dirName: "SNG", dirPrice: 0.5 },
    { id: idSeq++, dirName: "Europa", dirPrice: 0.7 },
    { id: idSeq++, dirName: "USA,Kanada", dirPrice: 0.9 },
    { id: idSeq++, dirName: "Asia", dirPrice: 1.0 },
    { id: idSeq++, dirName: "Sputnic", dirPrice: 12 },
  ])
  const columns = [
    {
      title: 'Direction',
      dataIndex: 'dirName',
      key: 'dirName',
    },
    {
      title: 'PriceVoume',
      dataIndex: 'dirPrice',
      key: 'dirPrice',
    },
  ];

  const dataSource=priceDir.map(e=>({...e,key:e.id}));
  return (
    <div className="App">
      <Row>
        <Col md={{span:12,offset:1}}>
          <h3>"DirList:"</h3>
          <Table dataSource={dataSource} columns={columns} />;
        </Col>
      </Row>
    </div>
  )
}

export default DirList;