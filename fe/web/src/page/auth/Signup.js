import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, Prompt } from "react-router-dom";
import "../../assets/pages/signup.css";
import * as Yup from "yup";
import { signUpAPI, confirmEmailAPI } from "../../services/user";
import * as cookie from "js-cookie";
import { SubmitButton, Input, Form, FormItem } from "formik-antd";
import { Button, Row, Col, Alert, Card } from "antd";
import { Formik } from "formik";
import { REMEMBER } from "../../constants/appConst";
import Container from "../../components/UI/Container/Container";
import image from "../../assets/image/image2.png";

const Signup = () => {
	const { t } = useTranslation();
	const [isRegisterSuccess, setIsRegisterSuccess] = useState(false);
	const [emailRegister, setEmailRegister] = useState("");
	const validateForm = Yup.object({
		fullName: Yup.string().required(t("Please enter an full name")),
		email: Yup.string().email(t("Invalid email address")).required(t("Please enter an email address")),
		password: Yup.string().min(8, t("Must be 8 characters or more")).required(t("Please enter password")),
		comfirmPassword: Yup.string()
			.required(t("Please enter confirm password"))
			.oneOf([Yup.ref("password")], t("Passwords must match")),
	});

	const [errorRes, setErrorRes] = useState(false);

	const onSubmit = (values, actions) => {
		var dataForm = {
			fullName: values.fullName,
			email: values.email,
			password: values.password,
		};
		signUpAPI(dataForm)
			.then((x) => {
				actions.setSubmitting(false);
				confirmEmailAPI({ email: values.email });
				setIsRegisterSuccess(true);
				setEmailRegister(values.email);
				if (cookie.get(REMEMBER)) {
					cookie.remove(REMEMBER);
				}
				cookie.set(REMEMBER, JSON.stringify({ ...dataForm, remember: true }));
			})
			.catch((err) => {
				actions.setSubmitting(false);
				console.log(err?.response?.data);
				if (err?.response?.data?.message) {
					setErrorRes(err?.response?.data?.message);
				}
			});
	};

	return (
		<Container className="sign-up">
			<Row className="auth-wrapper" justify="center" gutter={20}>
				<Col xs={24} md={24} lg={12} xl={12} style={{ textAlign: "center" }}>
					<img src={image} alt="sign up" />
					{/* <ImageCustom path="/images/image.png" /> */}
				</Col>
				<Col xs={24} md={24} lg={12} xl={12}>
					{!isRegisterSuccess && (
						<Formik
							initialValues={{
								email: "",
								fullName: "",
								password: "",
								comfirmPassword: "",
								agreeTerm: false,
							}}
							validationSchema={validateForm}
							onSubmit={onSubmit}
							render={({ errors, dirty, isValid }) => (
								<Form className="form">
									<Prompt when={dirty} message={"Are you sure to leave? You have unsaved changes"} />
									<Row>
										<Col xs={12} xl={12}>
											<h3>{t("Create an account")} </h3>
										</Col>
										<Col xs={12} xl={12}>
											<div className="auth-wrapper__link-create-account">
												{t("Or ")}
												<Link to="/auth/signin">{t("Sign in")}</Link>
											</div>
										</Col>
									</Row>
									{!!errorRes && (
										<Alert message={errorRes} type="error" style={{ margin: "10px 0" }} />
									)}
									<div className="label"> {t("Full Name")}* </div>
									<FormItem name="fullName">
										<Input name="fullName" placeholder={t("Full Name")} />
									</FormItem>
									<div className="label"> {t("Email")}* </div>
									<FormItem name="email">
										<Input name="email" placeholder={t("Email")} />
									</FormItem>
									<div className="label"> {t("Password")}* </div>
									<FormItem name="password">
										<Input.Password name="password" placeholder={t("Password")} />
									</FormItem>
									<div className="label"> {t("Confirm Password")}* </div>
									<FormItem name="comfirmPassword">
										<Input.Password name="comfirmPassword" placeholder={t("Confirm Password")} />
									</FormItem>
									<Button.Group style={{ width: "100%" }}>
										<SubmitButton block  className="main-button">{t("Create an account")}</SubmitButton>
									</Button.Group>
								</Form>
							)}
						/>
					)}
					{isRegisterSuccess && (
						<Card title="Please confirm you email" bordered={false} style={{ width: 350 }}>
							<Alert
								message={t(
									`You Request has been sent to ${emailRegister}. Please check your inbox to confirm your account`
								)}
								type="success"
								showIcon
							/>
							<br />
							<div>
								<Link to="/auth/signin">{t("Back Sign in")}</Link>
							</div>
						</Card>
					)}
				</Col>
			</Row>
		</Container>
	);
};

export default Signup;
