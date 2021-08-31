import { ADMIN, CDM, MANAGER } from "constants/roleType";
import React from "react";
import { useSelector } from "react-redux";

import { Route, Redirect } from "react-router-dom";
import { contains } from "utils/commom";
import ErrorPage from "../page/ErrorPage";

function PrivateAdminRoute({
	// isAuthenticated,
	component: Component,
	...rest // something else: path, exact, history,...
}) {
	const { user, isAuthUser } = useSelector((state) => state.authentication);

	const resultComponent = (props) => {
		var listRole = user.roles?.map((x) => x.name);
		if (isAuthUser && contains([ADMIN, MANAGER, CDM], listRole)) {
			return <Component {...props} />;
		}
		if (isAuthUser && !contains([ADMIN, MANAGER, CDM], listRole)) {
			return <ErrorPage code={403} />;
		}

		if (!isAuthUser) {
			return <Redirect to="/auth/signin" />;
		}
	};
	return <Route {...rest} render={(props) => resultComponent(props)} />;
}

export default PrivateAdminRoute;
