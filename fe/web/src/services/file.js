// @flow
import { CallAPI } from "./base";

import { EMPLOYEE_PORT } from "constants/portConst";


const configHeaders = {
  "content-type": "multipart/form-data",
};

export const uploadFileAPI = (data) =>
  CallAPI(`5002/employees/api/employees/upload-file`, "post", data, configHeaders);

export const downloadFiledAPI = (data) =>
  CallAPI(`5002/employees/api/employees/download-file/` + data, "get", null, null, "blob");

export const uploadFileByIdAPI = (idEmployee, idUser, data) =>
  CallAPI(`${EMPLOYEE_PORT}/employees/api/employees/upload-file/` + idEmployee + "?idUser=" + idUser, "post", data, configHeaders);

export const getFileDetailAPI = (idEmployee) =>
  CallAPI(`${EMPLOYEE_PORT}/employees/api/employees/file/` + idEmployee, "get", null);

export const updateStatusFile = (idEmployee, data) =>
  CallAPI(`${EMPLOYEE_PORT}/employees/api/employees/file/` + idEmployee, "patch", data);
