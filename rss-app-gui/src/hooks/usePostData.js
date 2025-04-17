import React, { useState, useEffect } from "react";
import axios from "axios";
import userService from "../services/userService";

const usePostData = (url,body) => {
    const [data, setData] = useState({});
    const [error, setError] = useState('');
    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const jwt = userService.getToken();
                const { data: response } = await axios.request({
                    url: url,
                    method: "POST",
                    headers: { 'Authorization': `Bearer ${jwt}`,
                    data:body
                 }
                });
                console.log("usePostPost:response=", response);
                // const json_data = await response.data.json();
                console.log("usePostData:response_data=", response.data);
                setData(response);
            } catch (error) {
                if (error?.response?.status === 401??error?.request?.status === 401) {
                    userService.doLogout();
                    console.log("usePostData:doLogout()")
                  }
                setError(error);
                console.log("usePostData:error=",error);
            } finally {
                setLoaded(true);
            }
        };
        userService.updateToken();
        fetchData();
        userService.updateToken();
    }, []);
    console.log("usePostData:loaded=", loaded);
    console.log("usePost:error=", error);
    console.log("usePost:return data=", data);
    return { data, error, loaded, };
}

export default usePostData;