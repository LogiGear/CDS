import React from "react";
import { useSelector } from "react-redux";
import { Route, Redirect } from "react-router-dom";

// import * as cookie from "js-cookie";
// import { TOKEN_KEY } from "../constants/appConst";

function PublicRoute({
  //   isAuthenticated,
  component: Component,
  ...rest // something else: path, exact, history,...
}) {
  const { isAuthUser } = useSelector((state) => state.authentication);
  return (
    <>
      <Route
        {...rest}
        component={(props) =>
          isAuthUser ? <Redirect to="/home" /> : <Component {...props} />
        }
      />
    </>
  );
}

export default PublicRoute;

// const mapStateToProps = (state) => ({
//   isAuthenticated: !!state.LoginReducer.isAuthUser,
// });

// export default connect(mapStateToProps)(PublicRoute);
