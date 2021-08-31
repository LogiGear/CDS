import React, { useMemo } from "react";
import { Row } from "antd";
import Container from "../UI/Container/Container";
import Title from "antd/lib/typography/Title";
import Search from "antd/lib/input/Search";
import { useTranslation } from "react-i18next";
import {  useLocation } from "react-router-dom";
import "../../assets/components/appHeader.css";
import logo from "../../assets/image/mowede.svg";
import { useSelector, useDispatch } from "react-redux";
import { searchActions } from "redux-flow/search/search-slice";

function AppHeader() {
	const { t } = useTranslation();
	const location = useLocation();
	const { isAuthUser } = useSelector((state) => state.authentication);
	const dispatch = useDispatch();
	const pageTitle = useMemo(() => {
		let title = "Page Not Found";
		if (location.pathname === "/" || location.pathname === "/home") return (title = "Home Page");
		if (location.pathname === "/profile/changepassword") return (title = "Change Password");
		if (location.pathname === "/admin/employee/list") return (title = "Employees List");
		if (location.pathname === "/employeeInformation") return (title = "My Information");
		if (location.pathname.includes("/admin/employee/update/")) return (title = "Employees Detail");
		if (location.pathname.includes("/department/employee/")) return (title = "Employee In Department");
		return title;
	}, [location]);

	const handleSearch = (value) => {
		console.log(value);
		dispatch(searchActions.saveTextSearch(value));
		//TODO: dispatch search value to redux store
	};

	return (
		<div className="header">
			<Container>
				<Row justify="space-between" align="middle">
					{!isAuthUser ? (
						<img src={logo} alt="Mowede logo" />
					) : (
						<>
							<Title level={1}>{t(pageTitle)}</Title>
							{((pageTitle === "Employees List") || (pageTitle === "Employee In Department")) && (
								<Search
									className="header__search-box"
									onSearch={handleSearch}
									placeholder={t("search employee by name/email")}
								/>
							)}
						</>
					)}
				</Row>
			</Container>
		</div>
	);
}

export default AppHeader;
