import React, { useState } from "react";
import { ImageCustom } from "../../components/image";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import "../../assets/pages/signin.css";
import { Formik } from "formik";
import * as Yup from "yup";

import { SubmitButton, Input, Form, FormItem } from "formik-antd";
import { Button, Row, Col, Alert } from "antd";
import { forgotpwdAPI } from "../../services/user";
import Container from "../../components/UI/Container/Container";

const ForgotPassword = () => {
	const [errorRes, setErrorRes] = useState("");
	const { t } = useTranslation();
	const initValueForm = { email: "" };
	const [isResetSuccess, setIsResetSuccess] = useState(false);

	const validateForm = Yup.object({
		email: Yup.string().email(t("Invalid email address")).required(t("Email is required")),
	});

	const onSubmit = (values, actions) => {
		setErrorRes("");
		const { email } = values;
		forgotpwdAPI({ email })
			.then((res) => {
				actions.setSubmitting(false);
				setIsResetSuccess(true);
			})
			.catch((err) => {
				actions.setSubmitting(false);
				if (err?.response?.data?.message) {
					setErrorRes(err?.response?.data?.message);
				}
			});
	};

	return (
		<Container>
			<Row className="auth-wrapper" gutter={20}>
				<Col xs={24} md={24} lg={12} xl={12} style={{ textAlign: "center" }}>
					<ImageCustom path="/images/image.png" />
				</Col>
				<Col xs={24} md={24} lg={12} xl={12}>
					{!isResetSuccess && (
						<Formik initialValues={initValueForm} validationSchema={validateForm} onSubmit={onSubmit}>
							{(props) => {
								return (
									<Form className="form">
										<Row>
											<Col xs={24} xl={24}>
												<h3>{t("Recover your password")} </h3>
											</Col>
										</Row>
										{!!errorRes && (
											<Alert message={errorRes} type="error" style={{ marginBottom: 20 }} />
										)}
										<div className="label"> {t("Enter your email")}* </div>
										<FormItem name="email">
											<Input name="email" placeholder={t("Enter your email")} />
										</FormItem>
										<Button.Group style={{ width: "100%" }}>
											<SubmitButton block className="main-button">{t("Send")}</SubmitButton>
										</Button.Group>
									</Form>
								);
							}}
						</Formik>
					)}

					<Link to="/auth/signin" style={{ marginTop: 20, display: "inline-block" }}>
						{t("Back To Sign in")}
					</Link>
					{isResetSuccess && (
						<Alert
							message={t(
								"Your Request has been sent to your email. Please check your inbox to confirm your account"
							)}
							type="success"
							showIcon
						/>
					)}
				</Col>
			</Row>
		</Container>
	);
};

export default ForgotPassword;
