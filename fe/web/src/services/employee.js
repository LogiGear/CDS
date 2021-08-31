// @flow
import { CallAPI } from "./base";

import { ADMIN_PORT, EMPLOYEE_PORT } from "constants/portConst";


export const getEmployeeAPI = () => CallAPI(`5003/admin/api/admins/user`, "get", null);

export const getListRoleAPI = () => CallAPI(`5003/admin/api/admins/role`, "get", null);

export const getEmployeeByIdAPI = (id) => CallAPI(`5002/employees/api/employees/` + id, "get");

export const getEmployeeImageByIdAPI = (id) => CallAPI(`5002/employees/api/image/` + id, "get");

export const updateEmployeeInfoAPI = (data) =>
  CallAPI(`${EMPLOYEE_PORT}/employees/api/employees/update`, "patch", data);

export const updateEmployeeImageAPI = (image) =>
  CallAPI(`5002/employees/api/image`, "post", image);

export const getDepartmentListAPI = () =>
  CallAPI(`5002/employees/api/employees/department`, "get", null);

export const getProjectListAPI = () =>
  CallAPI(`5002/employees/api/employees/project`, "get", null);

export const getDepartmentStructure = () =>
  CallAPI(`5002/employees/api/employees/department-structure` , "get", null);

export const getEmployeeByDepartment = (id) => CallAPI(`5002/employees/api/employees/employee-in-department/`+id , "get", null);
