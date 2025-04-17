import { useDispatch,useSelector } from 'react-redux';
import { Modal } from 'antd';
import { API_CALL_ERRORS } from "../constants/api";
import { setValue } from "../store/actions";
import { getApiResource } from "../utils/network";

// export const fetchCallErrData = async function() {
//     const res = await getApiResource(API_CALL_ERRORS);
//     return res;
// };


export const mergeErrData = (errData, procData) => {
    let resValue = [...errData];
    for (let i in errData) {
        for (let j in procData) {
            if (
                (errData[i].imsi === procData[j].imsi) &&
                (errData[i].callType === procData[j].callType) &&
                (errData[i].duration === procData[j].duration)
            ) {
                resValue[i].newCallId = procData[j].callId;
                resValue[i].newStartTime = procData[j].startTime;
                resValue[i].delDate = procData[j].callErrDelDate;
            }
        }
    }
    return resValue;
}

