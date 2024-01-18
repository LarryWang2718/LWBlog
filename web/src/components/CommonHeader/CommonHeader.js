import {Input, Tabs, Modal, Form, Radio, message, InputNumber, Menu, Dropdown, Avatar, Button } from 'antd';
import commonHeaderStyle from './CommonHeader.module.scss'
import logoImg from "../../assets/logo.jpg";
import { useHistory } from 'react-router-dom';
import axios from "axios";
import CommonContext from '../../context/CommonContext';
import {useState, useContext, useEffect } from "react";
import event from "../../event";
const { TabPane } = Tabs;
const { TextArea } = Input;

const CommonHeader = (props) => {

    const [loginVisible, setLoginVisible] = useState(false);
    const [registerVisible, setRegisterVisible] = useState(false);
    const [searchContent, setSearchContent] = useState("");
    const [loginUser, setLoginUser] = useState({});
    const { getFieldDecorator, validateFields } = props.form;
    const commonContext = useContext(CommonContext);
    const history = useHistory();


    useEffect(() => {
        event.addListener('refreshUser',
            () => {
                checkLogin();
            }
        );
        checkLogin();
        return () => {
        }
    }, []);

    const checkLogin = () => {
        let token = global.tools.getLoginUser();
        axios.post(commonContext.serverUrl + '/web/user/check_login',{token: token})
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    if (global.tools.isNotEmpty(resp.data.token)) {
                        setLoginUser(resp.data);
                    }
                }
            })
            .catch(function (error) {
            })
    };


    const submitRegister = () => {
        validateFields((err, values) => {
            if (err) {
                const errs = Object.keys(err);
                if (errs.includes('username')) {
                    message.warn("Please enter username.");
                    return
                }
                if (errs.includes('password')) {
                    message.warn("Please enter user password.");
                    return
                }
                if (errs.includes('phone')) {
                    message.warn("Please enter phone number.");
                    return
                }
            }
            axios.post(commonContext.serverUrl+ '/web/user/register', values)
                .then(function (response) {
                    let resp = response.data;
                    if(resp.code === 0){
                        message.success(resp.msg);
                        setRegisterVisible(false)
                    }else{
                        message.error(resp.msg);
                    }
                }).catch(function (error) {
                console.error(error);
                message.error("Network error. Fail to register.~");
            })
        });
    };

    const submitLogin = () => {
        validateFields((err, values) => {
            if (err) {
                const errs = Object.keys(err);
                if (errs.includes('username')) {
                    message.warn("Please enter username.");
                    return
                }
                if (errs.includes('password')) {
                    message.warn("Please enter user password.");
                    return
                }
            }
            axios.post(commonContext.serverUrl+ '/web/user/login', values)
                .then(function (response) {
                    let resp = response.data;
                    if(resp.code === 0){
                        message.success(resp.msg);
                        global.tools.setLoginUser(resp.data.token);
                        setLoginUser(resp.data);
                        setLoginVisible(false);
                        window.location.reload();
                    }else{
                        message.error(resp.msg);
                    }
                }).catch(function (error) {
                console.error(error);
                message.error("Network error. Fail to log in~");
            })
        });
    };

    const logoutUser = () => {
        let token = global.tools.getLoginUser();
        axios.post(commonContext.serverUrl + '/web/user/logout', {token: token})
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    global.tools.setLoginUser("");
                    setLoginUser({});
                    message.success(resp.msg);
                    window.location.reload();
                }
            }).catch(function (error) {
            message.error('Network error. Fail to log out.');
        })
    };

    const formItemLayout = {
        labelCol: {
            xs: { span: 20 },
            sm: { span: 6 },
        },
        wrapperCol: {
            xs: { span: 24 },
            sm: { span: 16 },
        },
    };

    const dropdownMenu = (
        <Menu>
            <Menu.Item>
                <div style={{cursor: 'pointer'}} onClick={() => history.push("/user/" + loginUser.id)}>
                    Profile
                </div>
            </Menu.Item>
            <Menu.Item>
                <div style={{cursor: 'pointer'}} onClick={() => logoutUser()}>
                    Log Out
                </div>
            </Menu.Item>
        </Menu>
    );



    return (
        <>
            <div className={commonHeaderStyle.header}>
                <div className={commonHeaderStyle.title} onClick={() => history.push("/index")}>
                    <div style={{marginRight: '1rem'}}>
                        <img src={logoImg} alt='' width='30' height='30'/>
                    </div>
                    <div style={{marginTop: '0.1rem'}}>
                        LWBlog
                    </div>
                </div>
                <div className={commonHeaderStyle.menu}>
                    <Tabs defaultActiveKey={props.tabKey} activeKey={props.tabKey} onChange={(e) => {
                        if(e === "1") {
                            history.push("/index");
                        } else if (e === "2") {
                            history.push("/forum");
                        }
                    }} >
                        <TabPane tab="Blog" key="1">
                        </TabPane>
                        <TabPane tab="Forum" key="2">
                        </TabPane>
                    </Tabs>
                </div>
                <div style={{width: '15%'}}></div>
                <div className={commonHeaderStyle.user}>
                    {
                        props.showSearch &&
                            <>
                                <div style={{marginRight: '1rem', marginTop: '0.2rem'}}>
                                    <Input defaultValue={searchContent} value={searchContent} onChange={(e) => setSearchContent(e.target.value)} placeholder="Search for ..." />
                                </div>
                                <div>
                                    <Button style={{marginTop: '0.2rem', marginRight: '1rem'}} type="primary" onClick={() => {
                                        event.emit("searchArticle", searchContent);
                                    }}>search</Button>
                                </div>
                            </>
                    }
                    {
                        loginUser.id ? (
                            <Dropdown overlay={dropdownMenu}>
                                <div className={commonHeaderStyle.loginUser}>
                                    <div>
                                        <Avatar style={{marginTop: '0.2rem'}} src={commonContext.serverUrl + '/common/photo/view?filename=' + loginUser.headPic} />
                                    </div>
                                    <div className={commonHeaderStyle.name}>
                                        <span>{loginUser.username || ''}</span>
                                    </div>
                                </div>
                            </Dropdown>
                        ) : (
                            <>
                                <div onClick={() => setLoginVisible(true)} className={commonHeaderStyle.button}>Log in</div>
                                <div onClick={() => setRegisterVisible(true)} className={commonHeaderStyle.button}>Sign up</div>
                            </>
                        )
                    }

                </div>
            </div>
            <Modal
                destroyOnClose={true}
                maskClosable={false}
                title="Log in"
                okText="confirm"
                onOk={() => submitLogin()}
                onCancel={() => setLoginVisible(false)}
                cancelText="cancel"
                visible={loginVisible}
            >
                <Form {...formItemLayout} >
                    <Form.Item label="username">
                        {getFieldDecorator('username', {
                            rules: [
                                {
                                    required: true,
                                    message: 'Please enter username.',
                                }
                            ],
                        })(<Input placeholder="Please enter user name"/>)}
                    </Form.Item>
                    <Form.Item label="password">
                        {getFieldDecorator('password', {
                            rules: [
                                {
                                    required: true,
                                    message: 'Please enter user password.',
                                }
                            ],
                        })(<Input type="password" placeholder="Please enter user password"/>)}
                    </Form.Item>
                </Form>
            </Modal>
            <Modal
                destroyOnClose={true}
                maskClosable={false}
                title="register"
                okText="confirm"
                onOk={() => submitRegister()}
                cancelText="cancel"
                onCancel={() => setRegisterVisible(false)}
                visible={registerVisible}
            >
                <Form {...formItemLayout} >
                    <Form.Item label="username">
                        {getFieldDecorator('username', {
                            rules: [
                                {
                                    required: true,
                                    message: 'Please enter user name.',
                                }
                            ],
                        })(<Input placeholder="Please enter user name"/>)}
                    </Form.Item>
                    <Form.Item label="password">
                        {getFieldDecorator('password', {
                            rules: [
                                {
                                    required: true,
                                    message: 'Please enter user password.',
                                }
                            ],
                        })(<Input type="password" placeholder="Please enter user password"/>)}
                    </Form.Item>
                    <Form.Item label="phone number">
                        {getFieldDecorator('phone', {
                            rules: [
                                {
                                    required: true,
                                    message: 'Please enter phone number.',
                                }
                            ],
                        })(<InputNumber style={{width: '100%'}} min={0} placeholder="Please enter phone number"/>)}
                    </Form.Item>
                    <Form.Item label="Gender">
                        {getFieldDecorator('sex', {
                            initialValue: 3
                        })(
                        <Radio.Group>
                            <Radio value={1}>Male</Radio>
                            <Radio value={2}>Female</Radio>
                            <Radio value={3}>Others</Radio>
                        </Radio.Group>)}
                    </Form.Item>
                    <Form.Item label="Profile">
                        {getFieldDecorator('info', {
                            initialValue: ''
                        })(
                            <TextArea rows={4} />)}
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )

};

export default Form.create()(CommonHeader);
