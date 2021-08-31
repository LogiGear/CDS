import React from "react";
import { useSelector } from "react-redux";

import { Route, Redirect } from "react-router-dom";

function PrivateRoute({
  // isAuthenticated,
  component: Component,
  ...rest // something else: path, exact, history,...
}) {
  const { isAuthUser } = useSelector((state) => state.authentication);
  return (
    <Route
      {...rest}
      render={(props) =>
        isAuthUser ? (
          <>
            <Component {...props} />
          </>
        ) : (
          <Redirect to="/auth/signin" />
        )
      }
    />
  );
}

export default PrivateRoute;
