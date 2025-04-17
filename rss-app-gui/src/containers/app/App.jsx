import React from 'react';
// import { useNavigate, Navigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
// import {
//   LaptopOutlined, NotificationOutlined, UserOutlined, HomeOutlined, ReloadOutlined, ThunderboltOutlined, LogoutOutlined, EditOutlined, UploadOutlined, SafetyOutlined, PlusCircleOutlined,
//   CalculatorOutlined, ImportOutlined, DeploymentUnitOutlined, NodeCollapseOutlined, BarsOutlined, SolutionOutlined
// } from '@ant-design/icons';
import AppRoutes from "../routes";
import AppMenu from "../../components/appMenu";
import AppHeader from "../../components/appHeader";
import { Breadcrumb, Layout, Flex, Menu, theme, Form, Select } from 'antd';
import { useClearGlobalData } from '../../utils/common';
import userService from '../../services/userService';
// import styles from "./App.module.css";
import "../../Main.css";
import { DB_NAME_RSS } from '../../constants/api';

const { Header, Content, Footer, Sider } = Layout;

function App() {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken()
  const auth = useSelector((state) => state.userNameReducer.auth);
  const dbName = useSelector((state) => state.appReducer.dataBaseName);

  return (
    userService.isLoggedIn() ?
      <Layout hasSider>
        <Sider
          style={{
            overflow: 'auto',
            height: '100vh',
            position: 'fixed',
            left: 0,
            top: 0,
            bottom: 0,
            background: colorBgContainer,
          }}
          width={230}
        >
          <AppMenu />
        </Sider>
        <Layout
          style={{
            marginLeft: 225,
            marginTop:0,
            height: '100vh',
          }}
        >
          <AppHeader onChange={useClearGlobalData} />
          <Content
            style={{
              marginTop:'1px',
              marginBottom:'1px',
              overflow: 'initial',
              // borderRadius: borderRadiusLG,
            }}
            className={dbName === DB_NAME_RSS && "rssbacgr"}
          >
            <AppRoutes />
          </Content>
          <Footer
            style={{
              margin:'0',
              textAlign: 'center',
            }}
            className={dbName === DB_NAME_RSS && "rssbacgr"}
          >
            RSS MegaFon  ©{new Date().getFullYear()}
          </Footer>
        </Layout>
      </Layout>
      :
      <>
        {userService.doLogin()}
      </>
  )
}
export default App;
