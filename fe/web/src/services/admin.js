// @flow
import { CallAPI } from "./base";

import { ADMIN_PORT, EMPLOYEE_PORT } from "constants/portConst";

export const updateRoleAPI = (data) => CallAPI(`5003/admin/api/admins/user`, "post", data);

export const updateEmployeeAPI = (data) =>
  CallAPI(`5003/admin/api/admins/update`, "patch", data);

export const getUserByRole = (data) =>
  CallAPI(`5003/admin/api/admins/user_role?roleName=` + data, "get", null);

export const updateManagerToEmployee = (id, data) =>
  CallAPI(`5003/admin/api/admins/updatemanager/` + id, "patch", data);

export const updateCdmToEmployee = (id, data) =>
  CallAPI(`5003/admin/api/admins/updatecdm/` + id, "patch", data);
export const updateProjectDepartment = (id, data) =>
  CallAPI(`5002/employees/update-project-department/` + id, "patch", data);

export const getRoleByUserAPI = (id) =>
  CallAPI(`5003/admin/api/admins/role/${id}`, "get", null);

export const updateManagerProjectDepartment = (data) =>
  CallAPI(`5003/admin/api/admins/manager-project-department`, "patch", data);
