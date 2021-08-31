import React, { useMemo, useState } from "react";
import { useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import "../../assets/pages/signin.css";
import { Formik } from "formik";
import * as Yup from "yup";
import * as cookie from "js-cookie";
import { REMEMBER } from "../../constants/appConst";

import { SubmitButton, Input, Checkbox, Form, FormItem } from "formik-antd";
import { Button, Row, Col, Alert } from "antd";
import { signInAPI } from "../../services/user";
import { SigninHandler } from "../../redux-flow/authentication/authentication-action";
import image from "../../assets/image/image2.png";
import Container from "../../components/UI/Container/Container";

const Signin = (props) => {
  const rememberMe = useMemo(() => {
    return cookie.get(REMEMBER) ? JSON.parse(cookie.get(REMEMBER)) : null;
  }, []);
  const dispatch = useDispatch();
  const [errorRes, setErrorRes] = useState();
  const { t } = useTranslation();

  const initValueForm = {
    email: rememberMe?.email,
    password: rememberMe?.password,
    remember: rememberMe?.remember,
  };

  const validateForm = Yup.object({
    email: Yup.string().required(t("Email is required")),
    password: Yup.string().required(t("Password is required")),
  });

  const onSubmit = (values, actions) => {
    setErrorRes("");
    if (!values.remember) {
      cookie.remove(REMEMBER);
    }
    signInAPI({ email: values.email, password: values.password })
      .then((res) => {
        if (values.remember) {
          cookie.set(REMEMBER, values);
        }
        actions.setSubmitting(false);
        dispatch(SigninHandler(res.data));
      })
      .catch((err) => {
        if (err?.response?.data?.message) {
          setErrorRes(err?.response?.data?.message);
        }
        actions.setSubmitting(false);
      });
  };

  return (
    <Container className="sign-in">
      <Row className="auth-wrapper" gutter={20}>
        <Col xs={24} md={24} lg={12} xl={12} style={{ textAlign: "center" }}>
          <img src={image} alt="sign in" />
        </Col>
        <Col xs={24} md={24} lg={12} xl={12}>
          <Formik
            initialValues={initValueForm}
            validationSchema={validateForm}
            onSubmit={onSubmit}
          >
            {() => (
              <Form className="form">
                <Row>
                  <Col xs={12} xl={12}>
                    <h3>{t("Sign in")} </h3>
                  </Col>
                  {/* <Col xs={12} xl={12}>
                    <div className="auth-wrapper__link-create-account">
                      {t("Or ")}
                      <Link to="/auth/signup">{t("Create an acount")}</Link>
                    </div>
                  </Col> */}
                </Row>
                {errorRes && (
                  <Alert
                    message={errorRes}
                    type="error"
                    style={{ marginBlock: 20 }}
                  />
                )}
                <div className="label"> {t("Email")}* </div>
                <FormItem name="email">
                  <Input name="email" placeholder={t("Email")} />
                </FormItem>
                <div className="label"> {t("Password")}* </div>
                <FormItem name="password">
                  <Input.Password name="password" placeholder={t("Password")} />
                </FormItem>
                <FormItem name="remember">
                  <Checkbox name="remember" style={{ fontWeight: "normal" }}>
                    {t("Remember Me")}
                  </Checkbox>
                </FormItem>
                <Button.Group style={{ width: "100%" }}>
                  <SubmitButton block className="main-button">
                    {t("Sign in")}
                  </SubmitButton>
                </Button.Group>
              </Form>
            )}
          </Formik>
          {/* <Link
            to="/auth/forgot-password"
            style={{ display: "inline-block", marginTop: 20 }}
          >
            {t("Forgot your Password?")}
          </Link> */}
        </Col>
      </Row>
    </Container>
  );
};

export default Signin;
