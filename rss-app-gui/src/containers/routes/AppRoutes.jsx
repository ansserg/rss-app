import React from 'react';
import { Routes, Route, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import PriceListPage from '../pages/priceListPage';
import LoadPriceList from '../pages/loadPriceList';
import LoadRePriceList from '../pages/loadRePriceList';
import ExchangePage from '../pages/exchangePage';
import RapPage from '../pages/rapPage';
import CallErrPage from '../pages/callErrPage';
import AuthPage from "../pages/authPage";
import RoamOperatorsPage from '../pages/roamOperatorsPage';
import RoamOpen from '../pages/roamOpenPage/RoamOpen';
import HomePage from '../pages/homePage';
import S4h from "../pages/s4h";
import ChangeDEF from "../pages/changeDef";
import AddLac from "../pages/addLacPage";
import LogOut from '../../components/Page/logout/LogOut';
import userService from '../../services/userService';

const AppRoutes = function () {
  const auth = useSelector((state) => state.userNameReducer.auth);
  const navigate = useNavigate()
  return (
    <Routes>
      {/* <Route path="/auth" element={<AuthPage />} /> */}
      <Route path="/" element={<HomePage />} />
      <Route path="/auth" element={<HomePage />} />
      <Route path="/home" element={<HomePage />} />
      <Route path="/addoper" element={<RoamOperatorsPage />} />
      <Route path="/sets4h" element={<S4h />} />
      <Route path="/ropeninc" element={<RoamOpen roamType={1} roamStates={3} />} />
      <Route path="/ropenog" element={<RoamOpen roamType={2} roamStates={3} />} />
      <Route path="/rcloseinc" element={<RoamOpen roamType={1} roamStates={1} />} />
      <Route path="/rcloseog" element={<RoamOpen roamType={2} roamStates={1} />} />
      <Route path="/changertpl" element={<PriceListPage />} />
      <Route path="/changedef" element={<ChangeDEF />} />
      <Route path="/addlac" element={<AddLac />} />
      <Route path="/loadprice" element={<LoadPriceList />} />
      <Route path="/loadreprice" element={<LoadRePriceList />} />
      <Route path="/exchange23" element={<ExchangePage tp={0} />} />
      <Route path="/exchangebill" element={<ExchangePage tp={1} />} />
      <Route path="/rerap" element={<RapPage />} />
      <Route path="/rece" element={<CallErrPage />} />
      {/* <Route path="/logout" element={<AuthPage />} /> */}
      {/* <Route path="/logout" element={<HomePage />} /> */}
      <Route path="/logout" onClick={()=>{userService.doLogout();userService.doLogin()}} />


    </Routes>
  )
}
export default AppRoutes;