import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Flex } from 'antd';
import { DownloadOutlined  } from '@ant-design/icons';
import { Button, message, Upload, Input } from 'antd';
import { formApiResource } from "../../../utils/network";
import { API_UPLOAD_PRICE,URI_API_SERVER } from "../../../constants/api";
import { setValue } from "../../../store/actions";



const PriceListControl = function () {
    const [dataTbl, setDataTbl] = useState(null);
    const [fileList, setFileList] = useState([]);
    const [uploading, setUploading] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(true);
    const [taskNumber, setTaskNumber] = useState("");
    const [errApi, setErrApi] = useState(false);
    const [isTableOpen, setTableOpen] = useState(false);
    const dispatchTask = useDispatch();
    const dispatchData = useDispatch();
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const URL_API=URI_API_SERVER+"/"+dbName;

    const props = {
        accept: ".xls,.xlsx",
        onRemove: (file) => {
            const index = fileList.indexOf(file);
            const newFileList = fileList.slice();
            newFileList.splice(index, 1);
            setFileList(newFileList);
        },
        beforeUpload: (file) => {
            setFileList([...fileList, file]);
            return false;
        },
        fileList
    };

    async function getResource(url, body) {
        const res = await formApiResource(url, body);
        if (res) {
            message.success('upload successfully.');
            setErrApi(false);
            setTableOpen(true);
            const dataSource = res.map(e => ({ ...e, key: e.attrid }));
            dispatchData(setValue({ data: dataSource }));
            dispatchTask(setValue({ task: taskNumber }));
        } else {
            setErrApi(true);
            message.error('upload failed.');
        }
    };

    const handleUpload = () => {
        const formData = new FormData();
        formData.append('file', fileList[0]);
        formData.append('task', taskNumber);
        setUploading(true);
        getResource(URL_API+"/"+API_UPLOAD_PRICE, formData);
    };

    const handleOk = () => {
        if ((fileList.length !== 0) && (taskNumber.length !== 0)) {
            handleUpload();
        }
        setIsModalOpen(false);
    };

    return (
        <Flex gap="small" wrap="wrap" style={{ marginBottom: 10 }}>
            <Input

                style={{ width: 100, margin: 10, display: "inline" }}
                placeholder="RFC"
                onChange={e => setTaskNumber(e.target.value)}
            />
            <Upload {...props}>
                <Button style={{ margin: 10 }} icon={<DownloadOutlined />}>Выбрать файл</Button>
            </Upload>
            <Button type="primary" style={{ margin: 10 }} onClick={handleOk}>Загрузить</Button>
        </Flex>
    );
}
export default PriceListControl;

