// @flow
import { CallAPI } from "./base";

import { MANAGER_PORT } from "constants/portConst";


export const getEmployeeManagerAPI = () =>
  CallAPI(`5004/manager/api/manager/employee`, "get", null);

export const getDepartmentManagerAPI = () =>
  CallAPI(`5004/manager/api/manager/department`, "get", null);

export const getEmployeeCDMAPI = (id) =>
  CallAPI(`5004/manager/api/manager/employee/${id}`, "get", null);

export const updateEmployeeProjectDepartment = (data) =>
  CallAPI(`5004/manager/api/manager/employee-project-department`, "patch", data);
