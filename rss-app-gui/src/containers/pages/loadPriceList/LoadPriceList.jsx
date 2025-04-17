import React from 'react';
import PriceList from "../../../components/Page/priceList";
import PriceCheckBar from "../../../components/Page/priceCheckBar";
import { API_UPLOAD_PRICE, API_READ_WRKDEF } from "../../../constants/api";
import { RatePlansTmpl } from '../../../components/uiComponents/uploadXls/templates/RatePlansTmpl';
import UploadXlsData from '../uploadXlsData/UploadXlsData';

const LoadPriceList = function () {
    return (
        <UploadXlsData
            url={API_UPLOAD_PRICE}
            urlnotfile={API_READ_WRKDEF}
            content=<RatePlansTmpl />
            panelctl=<PriceCheckBar />
            datacomponent=<PriceList />
        />
    );
};
export default LoadPriceList;
