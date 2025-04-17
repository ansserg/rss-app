import React, { useState } from 'react';
import { useSelector } from "react-redux";
import { API_UPLOAD_REPRICE,API_READ_REPRICE } from '../../../constants/api';
import { RePriceDataTmpl } from '../../../components/uiComponents/uploadXls/templates/RePriceDataTmpl';
import UploadXlsData from '../uploadXlsData/UploadXlsData';
import RepricingCtlBar from '../../../components/Page/repricingCtlBar/RepicingCtlBar';
import RepricingListData from '../repricingListData/RepricingListData';

const LoadRePriceList = function () {

    return (
        <UploadXlsData
            url={API_UPLOAD_REPRICE}
            urlnotfile={API_READ_REPRICE}
            content=<RePriceDataTmpl />
            panelctl=<RepricingCtlBar />
            datacomponent=<RepricingListData />
        />
    );
};
export default LoadRePriceList;
