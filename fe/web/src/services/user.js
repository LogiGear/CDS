// @flow
import { CallAPI } from "./base";


import { AUTHENTICATION_PORT } from "constants/portConst";

export const signInAPI = (data) => CallAPI(`${AUTHENTICATION_PORT}/authentication/api/auth/signin`, "post", data);
export const signUpAPI = (data) => CallAPI(`${AUTHENTICATION_PORT}/authentication/api/auth/signup`, "post", data);
export const forgotpwdAPI = (data) => CallAPI(`${AUTHENTICATION_PORT}/authentication/api/auth/forgotpwd`, "post", data);
export const resetpwdAPI = (passwordResetToken, data) =>
  CallAPI(
    `5001/authentication/api/auth/resetpwd?passwordResetToken=` + passwordResetToken,
    "patch",
    data,
  );
export const confirmEmailAPI = (data) => CallAPI(`5001/authentication/api/auth/confirm`, "post", data);
