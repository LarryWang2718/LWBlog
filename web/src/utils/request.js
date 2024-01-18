import axios from "axios";
import { message } from 'antd';
// import { createHashHistory } from 'history';
// const history = createHashHistory();



axios.interceptors.request.use(config => {
    console.log("Request：", config);
    let token = global.tools.getLoginUser();
    console.log("loadd token：", token);
    if (global.tools.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("Request headers to token:", token);
    }
    return config;
}, error => {});


axios.interceptors.response.use(response => {
    console.log("Response：", response);
    return response;
}, error => {
    return Promise.reject(error);
});

