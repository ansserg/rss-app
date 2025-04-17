import React from 'react';
import { Menu } from "antd";
import { useNavigate, Navigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import {
    HomeOutlined, ReloadOutlined, ThunderboltOutlined, LogoutOutlined, EditOutlined, UploadOutlined, SafetyOutlined, PlusCircleOutlined,
    CalculatorOutlined, ImportOutlined, DeploymentUnitOutlined, NodeCollapseOutlined, BarsOutlined, SolutionOutlined,
    PlusOutlined, MenuUnfoldOutlined, DownloadOutlined, RiseOutlined
} from "@ant-design/icons/lib/icons"
import { useClearGlobalData } from '../../utils/common';
import { initValue } from '../../store/actions';
import userService from '../../services/userService';


const AppMenu = function () {
    // const auth = useSelector((state) => state.userNameReducer.auth);
    const navigate = useNavigate();
    const dd = useDispatch();
    return (
        <Menu
            mode="inline"
            theme="dark"
            style={{
                height: '100%',
            }}
            onClick={({ key }) => {
                // console.log('menuOnClick..');
                navigate(key);
            }}
            onDeselect={({ key }) => {
                // console.log('onDeselectMenu..')
                // dd(initValue());
            }
            }
            onSelect={({ key }) => {
                // console.log('onSelectMenu..')
                dd(initValue());
            }
            }
            items={[
                {
                    label: "Оператор",
                    icon: <MenuUnfoldOutlined />,
                    children: [{
                        label: "новый оператор",
                        key: "/addoper",
                        icon: <DownloadOutlined />,
                    },
                    {
                        label: "контировки",
                        key: "/sets4h",
                        icon: <DownloadOutlined />,
                    },
                    {
                        label: "курсы валют",
                        icon: <MenuUnfoldOutlined />,
                        children: [
                            {
                                label: "на 23-е число",
                                key: "/exchange23",
                            },
                            {
                                label: "на п.д. месяца",
                                key: "/exchangebill",
                            },
                        ]
                    },
                    {
                        label: "роуминг",
                        icon: <MenuUnfoldOutlined />,
                        children: [
                            {
                                label: "входящий",
                                icon: <MenuUnfoldOutlined />,
                                key: "/roinc",
                                children: [
                                    {
                                        label: "открыть",
                                        key: "/ropeninc",
                                    },
                                    {
                                        label: "закрыть",
                                        key: "/rcloseinc",
                                    }
                                ]
                            },
                            {
                                label: "исходящий",
                                icon: <MenuUnfoldOutlined />,
                                key: "/roog",
                                children: [
                                    {
                                        label: "открыть",
                                        key: "/ropenog",
                                    },
                                    {
                                        label: "закрыть",
                                        key: "/rcloseog",
                                    }
                                ],
                            },
                        ]
                    },
                    ]
                },
                {
                    label: "Тарифы", key: "/changeprice", icon: <MenuUnfoldOutlined />,
                    children: [{
                        label: "визитеры",
                        key: "/loadprice",
                        icon: <DownloadOutlined />
                    },
                    {
                        label: "репрайсинг",
                        key: "/loadreprice",
                        icon: <DownloadOutlined />
                    },
                    {
                        label: "DEF",
                        key: "changedef",
                        icon: <DownloadOutlined />
                    },
                    {
                        label: "Lac",
                        key: "addlac",
                        icon: <DownloadOutlined />
                    },
                    ]
                },
                {
                    label: "Перетарификация",
                    key: "/retar",
                    icon: <MenuUnfoldOutlined />,
                    children: [
                        { label: "RAP", key: "rerap", icon: <ReloadOutlined />, },
                        { label: "CALL_ERR", key: "rece", icon: <ReloadOutlined />, }
                    ]
                },
                // { label: "Выход", key: "/logout", icon: <LogoutOutlined /> },
                {
                    label: "Выход",
                    key: "/",
                    onClick: () => { 
                        navigate("/");
                        userService.doLogout() 
                    },
                    icon: <LogoutOutlined />
                },

            ]}
        />
    )
}

export default AppMenu;