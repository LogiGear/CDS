import { CallAPI } from "./base";
import { AUTHENTICATION_PORT } from './../constants/portConst';


export const changePasswordAPI = (data) => CallAPI(`5001/authentication/api/auth/changepwd`, "post", data);
