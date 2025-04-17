import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Flex, notification } from 'antd';
import { QuestionOutlined, UploadOutlined } from '@ant-design/icons';
import { Button, message, Upload, Input, Modal } from 'antd';
import { formApiResource } from "../../../utils/network";
import { setValue, initValue } from "../../../store/actions";
import { apiGetData, apiPostData } from '../../../utils/common';
import { LacTmpl } from './templates/LacTmpl';
import { DB_UNDEF } from '../../../constants/api';
import "./../../../Main.css";

const UploadXls = function ({ url, urlnotfile, content }) {
    const [dataTbl, setDataTbl] = useState(null);
    const [fileList, setFileList] = useState([]);
    const [uploading, setUploading] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(true);
    const [taskNumber, setTaskNumber] = useState("");
    const [errApi, setErrApi] = useState(false);
    const [isTableOpen, setTableOpen] = useState(false);
    const dispatchTask = useDispatch();
    const dispatchData = useDispatch();
    const [isLoading, setIsLoading] = useState(false);
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    // const URL_API=URI_API_SERVER+"/"+dbName;

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
    const handleHelp = () => {
        Modal.info({
            title: "формат файла исходных данных",
            width: "auto",
            content: content
        });
    }

    const handleUpload = async () => {
        const formData = new FormData();
        let res = undefined;
        if ((fileList.length !== 0) || (urlnotfile !== undefined)) {
            if (fileList.length !== 0) {
                formData.append('file', fileList[0]);
                formData.append('task', taskNumber);
                // getResource(formData);
                // readSrcFile(formData);
                res = await apiPostData(url, formData);
            } else if (urlnotfile !== undefined) {
                res = await apiGetData(`${urlnotfile}/${taskNumber}`);
            }
            if (res !== undefined && !res?.error && res?.length !== 0) {
                message.success('Данные прочитаны успешно.');
                const dataSource = res?.map(e => ({ ...e, key: e.attrid }));
                dispatchData(setValue({ data: dataSource }));
                dispatchTask(setValue({ task: taskNumber }));
            } else {
                console.log("handeUploadError:",res)
                notification.open({
                    message: 'Ошибка',
                    description:`Не выбран файл с исходными данными и нет ранее введенных данных!  -  ${res.error?.message}`,
                    className: 'notification-warn',
                });
            }
        } else {
            notification.open({
                message: 'Ошибка',
                description: 'Не выбран файл с исходными данными!',
                className: 'notification-warn',
            });
        }
    };

    const readSrcFile = async (body) => {
        const res = await apiPostData(url, body);
        if (!res?.error) {
            message.success('Данные прочитаны успешно.');
            const dataSource = res.map(e => ({ ...e, key: e.attrid }));
            dispatchData(setValue({ data: dataSource }));
            dispatchTask(setValue({ task: taskNumber }));
        } else {
            notification.open({
                message: 'Ошибка',
                description: 'Не не выбран файл с исходными данными и нет ранее введенных данных!',
                className: 'notification-warn',
            });
        }
    }
    const handleOk = () => {
        if (dbName === DB_UNDEF) {
            notification.open({
                message: 'Ошибка:',
                description: 'Укажите базу данных!',
                className: 'notification-warn',
            });
        } else {
            // setIsModalOpen(true);
            setIsLoading(true);
            if (
                // (fileList.length !== 0) &&
                (taskNumber.length !== 0)) {
                handleUpload();
            } else {
                notification.open({
                    message: 'Ошибка исходных данных:',
                    description: 'Не задан RFC и/или не выбран файл с исходными данными!',
                    className: 'notification-warn',
                });
            }
            setIsLoading(false);
        }
    };

    const changeTask = (e) => {
        setTaskNumber(e.target.value);
        // console.log("taskNumber:", taskNumber);
        dispatchTask(setValue({ task: e.target.value }));
    }

    return (
        <Flex gap="small" wrap="wrap" style={{ marginLeft: 5 }}>
            <Input
                style={{ width: 120, marginLeft: 0, display: "inline" }}
                className={taskNumber.length === 0 ? "valundef" : ""}
                placeholder="RFC"
                // onChange={e => {setTaskNumber(e.target.value); dispatchTask(setValue({ task: taskNumber }));}}
                onChange={changeTask}
                required={true}
            />
            <Upload {...props}>
                <Button style={{ marginLeft: 0 }} icon={<UploadOutlined />}>Выбрать файл</Button>
            </Upload>
            <Button type="primary" style={{ margin: 0 }} onClick={handleOk}>Загрузить</Button>
            {content
                ? <Button
                    type="default"
                    size="small"
                    style={{ marginLeft: 5, size: 8 }}
                    shape="circle"
                    icon={<QuestionOutlined />}
                    onClick={handleHelp}
                >
                </Button>
                : <></>
            }
            {
                isLoading
                    ? <h2>Подождите, идет загрузка информации..</h2>
                    : <>
                    </>
            }
        </Flex>
    );
}
export default UploadXls;