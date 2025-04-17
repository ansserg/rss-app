import React, { useEffect, useState } from "react";
import { Space, Table, Tag, Row, Col } from 'antd';

const TestData = function () {
    console.log("TsstData begin..");
    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
    ]
    const [dataTest, setDataTest] = useState();
    function fetchData() {
        fetch("http://msk-rss-appa0:8443/rss/ocsdb/get-testData")
            .then(response => response.json())
            .then((result)=>setDataTest(result));
            console.log("fetch - Ok")
    }
    useEffect(fetchData,[]);
    console.log(dataTest);
    return (
        <>
            <h3>TestData:</h3>
            <Table dataSource={dataTest} columns={columns} />
        </>
    );
}
export default TestData;