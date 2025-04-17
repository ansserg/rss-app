import React, { useState, useEffect } from "react";
import axios from "axios";
import userService from "../services/userService";
import { Navigate } from "react-router-dom";
import { DB_UNDEF } from "../constants/api";

const useFetchData = (url) => {
    const [data, setData] = useState({});
    const [error, setError] = useState('');
    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                userService.updateToken();
                const jwt = userService.getToken();
                const { data: response } = await axios.request({
                    url: url,
                    method: "GET",
                    headers: { 'Authorization': `Bearer ${jwt}` }
                });
                console.log(`useFetchData:Ok,url=${url},response=response`, response);
                setData(response);
            } catch (error) {
                if (error?.response?.status === 401 ?? error?.request?.status === 401) {
                    userService.doLogout();
                    console.log("useFetchData:Err401,doLogout()")
                }
                setError(error);
                console.log(`useFetchData:Err,url=${url},error=${error}`);
            } finally {
                setLoaded(true);
            }
        };
        if (url.includes(DB_UNDEF)) {
            console.log("apiGetData:url.includes(", DB_UNDEF, ")");
            return setError("Не указана база данных!");
        } else {
            fetchData();
        }
    }, []);
    return { data, error, loaded, };
}

export default useFetchData;