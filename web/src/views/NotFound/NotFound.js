import React from "react";
import { Empty } from 'antd';
import notFoundStyle from './NotFound.module.scss'

const NotFound = () => {

    return (
        <React.Fragment>
            <Empty className={notFoundStyle.empty} description="Page does not exist~" imageStyle={{width:'200px', height: '200px'}} />
        </React.Fragment>
    );
};

export default NotFound;
