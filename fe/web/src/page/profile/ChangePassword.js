import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import "../../assets/pages/signin.css";
import { Formik } from "formik";
import * as Yup from "yup";
import * as cookie from "js-cookie";
import { TOKEN_KEY, USER_STORAGE } from "../../constants/appConst";
import { transformerDataErrorServer } from "../../utils/transformer-data-error";
import { changePasswordAPI } from "../../services/profile";
import Container from "../../components/UI/Container/Container";
import { logoutHandler } from "../../redux-flow/authentication/authentication-action";

import { SubmitButton, Input, Form, FormItem } from "formik-antd";
import { Button, Row, Col, Alert, Card } from "antd";

const ChangePassword = (props) => {
  const dispatch = useDispatch();
  const [errorRes, serErrorRes] = useState();
  const { t } = useTranslation();
  const [isSuccess, setisSuccess] = useState(false);

  const initValueForm = {
    password: "",
    confirmPassword: "",
  };

  const validateForm = Yup.object({
    password: Yup.string()
      .min(8, t("Must be 8 characters or more"))
      .required(t("Password is required")),
    confirmPassword: Yup.string()
      .required(t("Please re-enter password"))
      .oneOf([Yup.ref("password"), null], t("Passwords must match")),
  });

  const handleClickBackToSignIn = () => {
    dispatch(logoutHandler());
  };

  const onSubmit = (values, actions) => {
    const userData = JSON.parse(localStorage.getItem(USER_STORAGE));

    var formData = {
      email: userData.email,
      password: values.password,
    };

    changePasswordAPI(formData)
      .then((res) => {
        cookie.remove(TOKEN_KEY);
        actions.setSubmitting(false);
        setisSuccess(true);
      })
      .catch((e) => {
        setisSuccess(false);
        actions.setSubmitting(false);
        serErrorRes(transformerDataErrorServer(e.message));
      });
  };

  return (
    <Container>
      <Row className="auth-wrapper" justify="center">
        {!isSuccess ? (
          <Col xs={24} xl={12} style={{ padding: "10px" }}>
            <Formik
              initialValues={initValueForm}
              validationSchema={validateForm}
              onSubmit={onSubmit}
            >
              {() => (
                <Form className="form">
                  <Row>
                    <Col xs={24} xl={24}>
                      <h3>{t("Reset Password")} </h3>
                    </Col>
                  </Row>
                  <div className="label"> {t("Enter your new password")}* </div>
                  <FormItem name="password">
                    <Input.Password
                      name="password"
                      placeholder={t("Enter your new password")}
                    />
                  </FormItem>
                  <div className="label"> {t("Confirm new password")}* </div>
                  <FormItem name="confirmPassword">
                    <Input.Password
                      name="confirmPassword"
                      placeholder={t("Confirm new password")}
                    />
                  </FormItem>
                  {errorRes && <Alert message={errorRes} type="error" />}
                  <Button.Group style={{ width: "100%" }}>
                    <SubmitButton block className="main-button">
                      {t("Reset")}
                    </SubmitButton>
                  </Button.Group>
                </Form>
              )}
            </Formik>
          </Col>
        ) : (
          <Card
            title="Change password successfully!"
            bordered={false}
            style={{ width: 350 }}
          >
            <Alert
              message={t(
                "Please use new password next time you log in. Thank you!",
              )}
              type="success"
              showIcon
            />
            <br />
            <div>
              <Link onClick={handleClickBackToSignIn}>
                {t("Back to sign in")}
              </Link>
            </div>
          </Card>
        )}
      </Row>
    </Container>
  );
};

export default ChangePassword;
