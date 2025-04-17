import React from "react";
import { useNavigate, Navigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { Menu, Button, Flex, Layout, Select, Dropdown } from 'antd';
import { LogoutOutlined } from '@ant-design/icons';
import { clearErrData, clearRap, initValue, setAppDataBaseName } from "../../store/actions";
import { DB_UNDEF, DB_NAME_RSS, DB_NAME_RSSREP, DB_NAME_RSSTEST, DB_NAME_RSSTEST2 } from "../../constants/api";
import stl from "./appHeader.module.css"
import "../../Main.css";
import userService from "../../services/userService";
const { Header } = Layout;

const options = [
    {
        label: DB_UNDEF,
        value: DB_UNDEF
    },
    {
        label: DB_NAME_RSS,
        value: DB_NAME_RSS
    },
    {
        label: DB_NAME_RSSREP,
        value: DB_NAME_RSSREP
    },
    {
        label: DB_NAME_RSSTEST,
        value: DB_NAME_RSSTEST
    },
    {
        label: DB_NAME_RSSTEST2,
        value: DB_NAME_RSSTEST2
    },
]

const AppHeader = function () {
    const dbName = useSelector((state) => state.appReducer.dataBaseName);
    const navigate = useNavigate();
    const dd = useDispatch();
    const setDBName = (val) => {
        navigate("/");
        dd(setAppDataBaseName({ dataBaseName: val }));
        dd(clearErrData());
        dd(clearRap());
        dd(initValue());
    };
    const userName = userService.getUsername();

    const handleExit = () => {
        navigate("/");
        userService.doLogout();
    }
    return (
        <Header
            style={{
                margin: 0,
                paddingLeft: 10,
                color: "white",
                background: "black",
                // background: "cadetblue",
            }}
        >
            <div style={{ display: 'flex' }}>
                <div style={{ width: '110px' }}>
                    {'База данных'}
                </div>
                <div >
                    <Select
                        style={{ width: 100 }}
                        status={dbName === DB_UNDEF ? "error" : ""}
                        defaultValue={DB_UNDEF}
                        onChange={setDBName}
                        options={options}
                    />
                </div>
                <div style={{ width: '100%', textAlign: 'right' }}>
                    {/* <Dropdown menu={[{ label: "Выход", key: '1', }]} onClick={handeOnClick}
                    > */}
                    <Button variant="solid" color="default" onClick={handleExit}>
                        {userName}<LogoutOutlined />
                    </Button>
                    {/* </Dropdown> */}
                </div>
            </div>
        </Header>

    )
}
export default AppHeader;