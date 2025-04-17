import React from "react";
import { Flex } from 'antd';

const TextList = function ({ msg }) {
    // console.log("msg:" + msg);
    let strList;
    if (msg) {
        strList = msg.map(v => (
            <li>
                <p>
                    {v}
                </p>
            </li>
        ));
        strList=strList.map((v,i)=>({...v,key:i}));
    }
    return (
        <>
            <ul>{strList}</ul>
        </>
    )
}

export default TextList;