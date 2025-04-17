import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { notification, message, Table, Button, Modal, Input, Select, Typography, ConfigProvider, theme, Layout } from "antd";
import { QuestionOutlined, DeleteOutlined, EditOutlined } from "@ant-design/icons/lib/icons"
import { apiJsonPostData, useGetColumnSearchProps } from '../../../utils/common';
import { URI_API_SERVER, API_ADD_LAC_REG, API_OCS_ADD_LAC_REG, API_GET_REGIONS, DB_NAME_RSS } from "../../../constants/api";
import SysLogsList from "../sysLogsList/SysLogsList";
import { postApiResource, openIfoModal, getApiResource } from "../../../utils/network";
import { initValue, setValue } from "../../../store/actions";
import ErrorPage from "../errorPage/ErrorPage";
import userService from "../../../services/userService";

const LacList = function ({ dbName }) {

    const dispatchData = useDispatch();
    const userName = useSelector((state) => state.userNameReducer.userName);
    const xlsData = useSelector((state) => state.xlsDataReducer.data);
    const [dataSource, setSourceData] = useState(null);
    // const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const task = useSelector((state) => state.xlsDataReducer.task);
    const URL_API = URI_API_SERVER + "/" + dbName;
    const url_get_regions = `${URL_API}/${API_GET_REGIONS}`;
    const [isLoading, setIsLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [lacEditing, setLacEditing] = useState(null);
    const [regList, setRegList] = useState(null);
    const { Title } = Typography;


    const addXlsData = () => {
        // console.log("addXlsData..");
        setSourceData(xlsData?.map(v => ({ ...v, key: v.region + v.lac })));
    };
    const getRegions = async () => {
        if (!regList) {
            // console.log("getRegions..")
            setIsLoading(true);
            const regLst = await getApiResource(url_get_regions);
            setIsLoading(false);
            regLst ? setRegList(regLst) : message.error('ошибка чтения справочника регионов !');
        };
        addXlsData();
    }

    const handleOk = async () => {
        console.log("task:" + task);
        if (task > 0) {
            const urlo = `${URL_API}/${API_OCS_ADD_LAC_REG}`;
            const urlr = `${URL_API}/${API_ADD_LAC_REG}`;
            if (dataSource) {
                const data = ({ lacList: dataSource, userName: userService.getUsername(), task: task, });
                // console.log("data:", data);
                setIsLoading(true);
                const bodyo = await apiJsonPostData(urlo, JSON.stringify(data));
                // const bodyo = await postApiResource(urlo, JSON.stringify(data));
                if (!bodyo?.error) {
                    const bodyr = await apiJsonPostData(urlr, JSON.stringify(data));
                    // const bodyr = await postApiResource(urlr, JSON.stringify(data));
                    if (!bodyr?.error) {
                        openIfoModal("Ввод новых LAC", <SysLogsList data={[...bodyo, ...bodyr]} />)
                    } else {
                        openIfoModal("Ошибка", <ErrorPage message="Ошибка ввода новых LAC в схему RSSMB !" />);
                    }
                } else {
                    openIfoModal("Ошибка", <ErrorPage message="Ошибка ввода новых LAC в схему OCSDB !" />);
                };
                setIsLoading(false);
            } else {
                notification.open({
                    message: 'Ошибка исходных данных:',
                    description: 'Не задан(ы) LAC(и)!',
                    className: 'notification-warn',
                });
            }

        } else {
            notification.open({
                message: 'Ошибка исходных данных:',
                description: 'Не задан RFC!',
                className: 'notification-warn',
            });
        };
    };
    const addLac = () => {
        const key = parseInt(Math.random() * 10000);
        const newLacReg = {
            // branch: "СтФ",
            region: "Москва",
            lac: "0000",
            key: key,
        };
        dataSource
            ? setSourceData([...dataSource, newLacReg])
            : setSourceData([newLacReg]);
    };
    const onDeleteLac = (rec) => {
        setSourceData(pre => {
            return pre.filter(lac => lac.key !== rec.key);
        })
    }
    const onEditLac = (record) => {
        // console.log("onEditLac:record=", record);
        setIsEditing(true);
        setLacEditing({ ...record });
        // console.log("onEditLac:lacEditing=", lacEditing);
    }
    const clearSourceData = () => {
        dispatchData(initValue());
        setSourceData(null);
    }

    const handleHelp = () => {

    }
    useEffect(() => {
        // console.log("useEffect..");
        getRegions();
    }, [xlsData]);

    const columns = [
        {
            title: 'Lac',
            dataIndex: 'lac',
            width: 100,
            key: 'name',
            fixed: 'left',
        },
        {
            title: 'Регион',
            dataIndex: 'region',
            width: 150,
            key: 'region',
            fixed: 'left',
            ...useGetColumnSearchProps('region'),
        },
        // {
        //     title: 'Филиал',
        //     dataIndex: 'branch',
        //     width: 150,
        //     key: 'branch',
        //     // fixed: 'left',
        //     ...useGetColumnSearchProps('branch'),
        // },
        {
            title: 'Действия',
            width: 50,
            key: 'action',
            render: (record) => {
                return (
                    <>
                        <EditOutlined
                            style={{ color: "green" }}
                            onClick={
                                () => {
                                    onEditLac(record);
                                }
                            }
                        />
                        <DeleteOutlined
                            style={{ color: "red", marginLeft: 25 }}
                            onClick={() => {
                                onDeleteLac(record);
                            }}

                        />
                    </>
                )
            }
        },
    ]

    return (
        <>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'right', marginRight: 10, marginTop: '5' }}>
                <Button type="primary" onClick={clearSourceData}>Очистить</Button>
                <Button type="primary" style={{ marginLeft: 30 }} onClick={addLac}>Добавить</Button>
                <Button type="primary" style={{ marginLeft: 30 }} onClick={handleOk}>Ввод изменений</Button>
            </div>
            {
                isLoading
                    ? <h2>Подождите, идет ввод изменений LAC..</h2>
                    : <Layout
                        style={{
                            margin: '5px',
                            height: '67vh'
                        }}
                    >
                        <Table
                            dataSource={dataSource}
                            columns={columns}
                            pagination={{
                                defaultPageSize: 50,
                                pageSizeOptions: [100, 1000,],
                            }}
                            scroll={{
                                y: 450,
                                x: 900,
                            }}
                        />
                    </Layout>
            }
            <Modal
                title="Редактирование записи о закреплении LAC"
                open={isEditing}
                okText="Записать"
                cancelText="Отменить"
                onCancel={() => {
                    setIsEditing(false);
                }}
                onOk={() => {
                    setSourceData((pre) => {
                        return pre.map((lacReg) => {
                            if (lacReg.key === lacEditing.key) {
                                return lacEditing;
                            } else {
                                return lacReg;
                            }
                        });
                    });
                    setIsEditing(false);
                }}
            >
                <Input
                    value={lacEditing?.lac}
                    onChange={(e) => {
                        setLacEditing((pre) => {
                            return { ...pre, lac: e.target.value }
                        })
                    }}
                />
                <Select
                    style={{
                        width: '100%',
                    }}
                    value={lacEditing?.region}
                    onChange={(e) => {
                        setLacEditing((pre) => {
                            return { ...pre, region: e }
                        })
                    }}
                >
                    {
                        regList?.sort((a, b) => a.name.localeCompare(b.name))
                            .map((e => ({ ...e, key: e.regId })))
                            .map(e =>
                                <Select.Option value={e.name}>{e.name}</Select.Option>
                            )
                    }
                </Select>
                <br />
                {/* <Select
                    style={{
                        width: '100%',
                    }}
                    value={lacEditing?.branch}
                    onChange={(e) => {
                        setLacEditing((pre) => {
                            return { ...pre, branch: e }
                        })
                    }}
                    options={[
                        {
                            value: "ДВФ",
                            label: "ДВФ",
                        },
                        {
                            value: "КФ",
                            label: "КФ",
                        },
                        {
                            value: "ПФ",
                            label: "ПФ",
                        },
                        {
                            value: "СЗФ",
                            label: "СЗФ",
                        },
                        {
                            value: "СИБФ",
                            label: "СибФ",
                        },
                        {
                            value: "СТФ",
                            label: "СтФ",
                        },
                        {
                            value: "УФ",
                            label: "УФ",
                        },
                        {
                            value: "ЦФ",
                            label: "ЦФ",
                        },

                    ]}
                >
                </Select> */}
            </Modal>
        </>
    )
}

export default LacList;