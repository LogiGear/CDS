import React from "react";
import { Switch, Route, Redirect } from "react-router-dom";

// import Signup from "../page/auth/Signup";
import HomePage from "../page/HomePage";
import PrivateRoute from "../router/PrivateRoute";
import PublicRoute from "../router/PublicRoute";
import PrivateAdminRoute from "../router/PrivateAdminRoute";
import Signin from "../page/auth/Signin";
import ListEmployees from "../page/admin/ListEmployees";
import EmployeeDetail from "../page/EmployeeDetail";

// import ForgotPassword from "../page/auth/ForgotPassword";
// import ResetPassword from "../page/auth/ResetPassword";

// import ChangePassword from "../page/profile/ChangePassword";
import ErrorPage from "../page/ErrorPage";
import CKEditors from "components/editor/CKEditors";
import DragDropImage from './../components/drag-drop/DragDropImage';
import TinyMCE from './../components/editor/TinyMCE';
import ReCharts from './../components/charts/ReCharts';
import GGCharts from './../components/charts/GGCharts';
import OrgCharts from './../components/charts/OrgCharts';
import DepartmentCharts from './../components/charts/DepartmentCharts';
import EmployeeDepartment from "page/employee/EmployeeDepartment";

export default function AppRouter() {
	return (
		<Switch>
			<PublicRoute path="/auth/signin" component={Signin} exact={true} />
			{/* <PublicRoute path="/auth/signup" component={Signup} exact={true} />
			<PublicRoute path="/auth/forgot-password" component={ForgotPassword} exact={true} />
			<PublicRoute path="/auth/reset-password/:passwordResetToken" component={ResetPassword} /> */}
			<PrivateRoute path="/home" component={HomePage} exact={true} />
			{/* <PrivateRoute path="/profile/changepassword" component={ChangePassword} exact={true} /> */}
			<PrivateRoute path="/employeeInformation" component={EmployeeDetail} exact={true} />
			<PrivateAdminRoute path="/admin/employee/list" component={ListEmployees} exact={true} />
			<PrivateAdminRoute path="/admin/employee/update/:idEmployee" component={EmployeeDetail} />
			<PrivateRoute path="/department/employee/:idDepartment" component={EmployeeDepartment} />
			<Redirect exact from="/" to="/home" />
			<Route path="/drag-drop-image">
				<DragDropImage />
			</Route>
			<Route path="/ckeditor">
				<CKEditors />
			</Route>
			<Route path="/tinymce">
				<TinyMCE />
			</Route>
			<Route path="/recharts">
				<ReCharts />
			</Route>
			<Route path="/ggcharts">
				<GGCharts />
			</Route>
			<Route path="/orgcharts">
				<OrgCharts />
			</Route>
			<Route path="/departmentcharts">
				<DepartmentCharts />
			</Route>
			<Route exact path="/page-not-found">
				<ErrorPage code={404} />
			</Route>
			<Redirect from="*" to="/page-not-found" />


			
		</Switch>
	);
}
