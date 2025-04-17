import { useDispatch } from "react-redux";
import { clearErrData, clearRap, initValue } from "../store/actions";
import { useRef, useState } from "react";
import { notification, Button, Space, Input, Modal } from "antd";
import { SearchOutlined } from '@ant-design/icons';
import axios from "axios";
import userService from "../services/userService";
import { DB_UNDEF } from "../constants/api";

export const useGetColumnSearchProps = function (dataIndex) {
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const searchInput = useRef(null);

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };
    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText('');
    };

    return ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters, close }) => (
            <div
                style={{
                    padding: 8,
                }}
                onKeyDown={(e) => e.stopPropagation()}
            >
                <Input
                    ref={searchInput}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{
                        marginBottom: 8,
                        display: 'block',
                    }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{
                            width: 90,
                        }}
                    >
                        Search
                    </Button>
                    <Button
                        onClick={() => clearFilters && handleReset(clearFilters)}
                        size="small"
                        style={{
                            width: 90,
                        }}
                    >
                        Reset
                    </Button>
                    <Button
                        type="link"
                        size="small"
                        onClick={() => {
                            confirm({
                                closeDropdown: false,
                            });
                            setSearchText(selectedKeys[0]);
                            setSearchedColumn(dataIndex);
                        }}
                    >
                        Filter
                    </Button>
                    <Button
                        type="link"
                        size="small"
                        onClick={() => {
                            close();
                        }}
                    >
                        close
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => (
            <SearchOutlined
                style={{
                    color: filtered ? '#1677ff' : undefined,
                }}
            />
        ),
        onFilter: (value, record) =>
            record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownOpenChange: (visible) => {
            if (visible) {
                setTimeout(() => searchInput.current?.select(), 100);
            }
        },
        // render: (text) =>
        //   searchedColumn === dataIndex ? (
        //     <Highlighter
        //       highlightStyle={{
        //         backgroundColor: '#ffc069',
        //         padding: 0,
        //       }}
        //       searchWords={[searchText]}
        //       autoEscape
        //       textToHighlight={text ? text.toString() : ''}
        //     />
        //   ) : (
        //     text
        //   ),
    })
};

export const useClearGlobalData = function () {
    // console.log('useClearGlobalData..');
    const dispatchData = useDispatch();
    dispatchData(clearErrData);
    dispatchData(clearRap());
    dispatchData(initValue());
}

export const isNull = (value) => {
    return (value ?? null) === null;
};

export const errorModal = function (title, content) {
    Modal.info({
        title: title,
        width: "auto",
        content: content
    })
};

export const apiPostData = async (url, body) => {
    const jwt = userService.getToken();
    try {
        const { data: response } = await axios.request({
            url: `${url}`,
            method: "POST",
            headers: {
                'Authorization': `Bearer ${jwt}`,
            },
            data: body
        });
        console.log(`apiPostData:Ok,response=${response}`);
        return response;
    } catch (error) {
        if (error?.response?.status === 401 ?? error?.request?.status === 401) {
            userService.doLogout();
            console.log("usePostData:doLogout()")
        } else {
            // notification.open({
            //     message: `ошибка выполнения запроса`,
            //     description: error?.message,
            //     className: 'notification-warn',
            // })
            console.log(`apiPostData:error,error=${error}`);
            return { error };
        };
    }
}

export const apiJsonPostData = async (url, body) => {
    const jwt = userService.getToken();
    try {
        const { data: response } = await axios.request({
            url: `${url}`,
            method: "POST",
            headers: {
                "Content-type": "application/json",
                'Authorization': `Bearer ${jwt}`,
            },
            data: body
        });
        console.log(`apiJsonPostData:Ok,response=${response}`);
        return response;
    } catch (error) {
        if (error?.response?.status === 401 ?? error?.request?.status === 401) {
            userService.doLogout();
            console.log("usePostData:Err401,doLogout()")
        } else {
            // notification.open({
            //     message: `ошибка выполнения запроса`,
            //     description: error?.message,
            //     className: 'notification-warn',
            // })
            console.log(`usePostData:error,error=${error}`);
            return { error };
        };
    }
}

export const apiGetData = async (url) => {
    console.log("apiGetData:url=", url);
    const jwt = userService.getToken();
    try {
        const { data: response } = await axios.request({
            url: url,
            method: "GET",
            headers: { 'Authorization': `Bearer ${jwt}` }
        });
        console.log(`apiGetData:Ok,response=${response}`);
        return response;
    } catch (error) {
        if (error?.response?.status === 401 ?? error?.request?.status === 401) {
            userService.doLogout();
            console.log("useFetchData:doLogout()")
        } else {
            // notification.open({
            //     message: `ошибка выполнения запроса`,
            //     description: error?.message,
            //     className: 'notification-warn',
            // });
            console.log(`apiGetData:error=${error}`);
            return (error);
        };
    };
}


