import { Modal } from "antd";
import axios from "axios";
import { isNull } from "./common";
import userService from "../services/userService";

export const getApiResource = async function (url) {
    if (!userService.isLoggedIn()) {
        await userService.doLogin();
    }
    else {
        console.log('getApiResource:updateYoken..');
        userService.updateToken();
        console.log('getApiResource:updateYoken end!');
    }
    try {
        console.log("getApiResource:getToken..");
        const jwt_token = userService.getToken();
        console.log("getApiResource:jwt_token=", jwt_token);
        console.log("getApiResource:fetch..");
        const res = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwt_token}`
            },
        });

        if (!res.ok) {
            if (res?.status === 401??res?.status === 401) {
                userService.doLogout();
                console.log("useFetchData:doLogout()")
              }
            console.error("getApiResource:fetch-Err", res.status);
            return false;
        }
        console.log("getApiResource:fetch-Ok,res=", res);
        return await res.json();
    } catch (err) {
        if (err?.response?.status === 401??err?.request?.status === 401) {
            userService.doLogout();
            console.log("useFetchData:doLogout()")
          }
        console.log("Could not fetch.", err);
        return false;
    }
}

export const formApiResource = async (url, body) => {
    const formData = new FormData();
    if (!userService.isLoggedIn()) {
        await userService.doLogin();
    }
    userService.updateToken();
    try {
        const jwt_token = userService.getToken();
        const res = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${jwt_token}`
            },
            method: 'POST',
            body: body,
        });
        if (!res.ok) {
            if (res?.status === 401??res?.status === 401) {
                userService.doLogout();
                console.log("useFetchData:doLogout()")
              }
            console.log("Could not fetch.", res);
            return false;
        }
        return await res.json();
    } catch (err) {
        if (err?.response?.status === 401??err?.request?.status === 401) {
            userService.doLogout();
            console.log("useFetchData:doLogout()")
          }
        console.error('Could not fetch.');
        return false;
    }
};

export const postApiResource = async (url, body) => {
    if (!userService.isLoggedIn()) {
        await userService.doLogin();
    }
    console.log('postApiResource:updateYoken..');
    userService.updateToken();
    console.log('postApiResource:updateYoken - end');
    try {
        console.log('postApiResource:getToken..');
        const jwt_token = userService.getToken();
        console.log('postApiResource:getToken end,jwt_token=', jwt_token);
        console.log('postApiResource:fetch..');
        const res = await fetch(url, {
            headers: {
                "Content-type": "application/json",
                'Authorization': `Bearer ${jwt_token}`
            },
            method: 'POST',
            body: body,
        });
        if (!res.ok) {
            if (res?.status === 401??res?.status === 401) {
                userService.doLogout();
                console.log("useFetchData:doLogout()")
              }
            console.log("Could not fetch.", res);
            console.log('postApiResource:fetch - Err,status=', res.status);
            return false;
        }
        console.log('postApiResource:fetch - Ok');
        return await res.json();
    } catch (err) {
        if (err?.response?.status === 401??err?.request?.status === 401) {
            userService.doLogout();
            console.log("useFetchData:doLogout()")
          }
        console.error("Could not fetch.", err);
    }
};

export const openIfoModal = function (title, content) {
    Modal.info({
        title: title,
        width: "auto",
        content: content
    });

}

