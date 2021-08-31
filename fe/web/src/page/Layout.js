import React, { useState } from "react";

import { useTranslation } from "react-i18next";
import { logoutHandler } from "../redux-flow/authentication/authentication-action";
import "assets/pages/layoutPage.css";
import { useHistory, NavLink, useLocation } from "react-router-dom";
import { Layout, Menu } from "antd";
import { UserOutlined, HomeOutlined, LogoutOutlined, TableOutlined} from "@ant-design/icons";
import mowede from "../assets/image/MWD-logo-white.svg";
import fb from "../assets/image/fb.svg";
import youtube from "../assets/image/youtube.svg";
import linkedin from "../assets/image/linkedin.svg";

import AppRouter from "../router/AppRouter";

import { useSelector, useDispatch } from "react-redux";
import AppHeader from "components/header/appHeader";

import useWindowDimensions from '../components/hook-custom/useWindowDimensions';

const { Sider } = Layout;
const { SubMenu } = Menu;

const LayoutPage = (props) => {
    const { t } = useTranslation();

    const history = useHistory();
    const dispatch = useDispatch();

    const { isAuthUser, user } = useSelector((state) => state.authentication);
    const location = useLocation();

    const [siderCollapsed, setSiderCollapsed] = useState(false);

    const { height, width } = useWindowDimensions();

    const signout = () => {
        dispatch(logoutHandler());
    };
   
   

    return (

       

        <div className="wrapper">
            <>
              
                <div className="menu">
                    {isAuthUser && (
                        <>
                            {/* left sider bar */}
                            <Sider
                                breakpoint="lg"
                                collapsedWidth={width > 500 ? '70' : '0'}
                                collapsible
                                collapsed={siderCollapsed}
                                onBreakpoint={(broken) => {
                                    // console.log(broken);
                                }}
                                onCollapse={(collapsed, type) => {
                                    setSiderCollapsed(collapsed);

                                }}
                                className="sider-ant"
                               
                            >
                                <div className="sider">
                                    <div className="sider__logo" onClick={() => history.push("/home")}>
                                        <img
                                            src={mowede}
                                            alt="mowede"
                                            className={siderCollapsed ? 'sider__logo-imgRound' : 'sider__logo-img'}

                                        />
                                    </div>
                                    <hr className="sider__lineDivider" />
                                    {/* menu */}
                                    <div className="sider__menu">
                                        <Menu mode="inline" selectedKeys={[location.pathname]} defaultSelectedKeys={["1"]}>
                                            <SubMenu key="sub1" title={t(`Hi, ${user.name}`)} icon={<UserOutlined />}>
                                                <Menu.Item key="/employeeInformation">
                                                    <NavLink
                                                        to="/employeeInformation"
                                                    >
                                                        {t("My Information")}
                                                    </NavLink>
                                                </Menu.Item>
                                            </SubMenu>
                                            <Menu.Item key="/home" icon={<HomeOutlined />}>
                                                <NavLink to="/home">{t("Home")}</NavLink>
                                            </Menu.Item>
                                            <Menu.Item key="/admin/employee/list" icon={<TableOutlined />}>
                                                <NavLink to="/admin/employee/list">{t("Employee List")}</NavLink>
                                            </Menu.Item>

                                            <hr className="sider__lineDivider" />
                                            <Menu.Item key="/signout" icon={<LogoutOutlined />} onClick={signout}>
                                                {t("Sign out")}
                                            </Menu.Item>

                                        </Menu>

                                    </div>
                                    {/* footer social */}
                                    <div

                                        className={siderCollapsed ? 'displayNone' : 'footerSider'}
                                    >
                                        <div className="footerSider__social">
                                            <img src={fb} alt="" className="social__img" width={50} height={50} />
                                            <img src={youtube} alt="" className="social__img" width={50} height={50} />
                                            <img src={linkedin} alt="" className="social__img" width={50} height={50} />
                                        </div>
                                        <div className="footerSider__copyRight">
                                            &copy; 2021 MOWEDE.
                                            <br />
                                            {t("All rights reserved")}.
                                        </div>
                                    </div>
                                </div>
                            </Sider>
                        </>
                    )}
                </div>
                <div className="container">
                    <div className="headerFixed">
                        <AppHeader />
                    </div>
                    <div className={isAuthUser ? "content" : ""}>


                        <AppRouter />
                    </div>

                </div>
            </>
        </div>


    );
};

export default LayoutPage;
