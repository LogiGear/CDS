import React, { useState } from "react";
import { ImageCustom } from "../../components/image";
import { useTranslation } from "react-i18next";
import "../../assets/pages/signin.css";
import { Formik } from "formik";
import * as Yup from "yup";
import { useParams, Link } from "react-router-dom";

import { SubmitButton, Input, Form, FormItem } from "formik-antd";
import { Button, Row, Col, Alert } from "antd";
import { resetpwdAPI } from "../../services/user";
import Container from "../../components/UI/Container/Container";

const ResetPassword = (props) => {
	const [errorRes, setErrorRes] = useState("");
	const [isSubmitSuccess, setIsSubmitSuccess] = useState(false);
	const { t } = useTranslation();
	const { passwordResetToken } = useParams();

	const initValueForm = { newPassword: "", confirmPassword: "" };

	const validateForm = Yup.object({
		newPassword: Yup.string().min(8, t("Must be 8 characters or more")).required(t("Password is required")),
		confirmPassword: Yup.string()
			.required(t("Please enter comfirm password"))
			.oneOf([Yup.ref("newPassword")], t("Passwords must match")),
	});

	const onSubmit = (values, actions) => {
		setErrorRes("");
		var dataForm = {
			newPassword: values.newPassword,
			confirmPassword: values.confirmPassword,
		};
		resetpwdAPI(passwordResetToken, dataForm)
			.then((res) => {
				setIsSubmitSuccess(true);
				actions.setSubmitting(false);
			})
			.catch((err) => {
				setIsSubmitSuccess(false);
				actions.setSubmitting(false);
				if (err.response?.data?.message) {
					setErrorRes(err.response?.data?.message);
				}
			});
	};

	return (
		<Container>
			<Row className="auth-wrapper" gutter={64}>
				<Col xs={24} md={24} lg={12} xl={12}>
					<ImageCustom path="/images/image.png" />
				</Col>
				<Col xs={24} md={24} lg={12} xl={12}>
					{!isSubmitSuccess ? (
						<Formik initialValues={initValueForm} validationSchema={validateForm} onSubmit={onSubmit}>
							<Form>
								<Row>
									<Col xs={24} xl={24}>
										<h3>{t("Change your password")} </h3>
									</Col>
								</Row>
								{errorRes && <Alert message={errorRes} type="error" style={{ marginBlock: 20 }} />}
								<div className="label"> {t("Enter your new password")}* </div>
								<FormItem name="newPassword">
									<Input.Password name="newPassword" placeholder={t("Enter your new password")} />
								</FormItem>
								<div className="label"> {t("Confirm new password")}* </div>
								<FormItem name="confirmPassword">
									<Input.Password name="confirmPassword" placeholder={t("Confirm new password")} />
								</FormItem>
								<Button.Group style={{ width: "100%" }}>
									<SubmitButton block disabled={false}>
										{t("Apply")}
									</SubmitButton>
								</Button.Group>
							</Form>
						</Formik>
					) : (
						<>
							<Alert message={t("Your password has been changed successfully")} type="success" showIcon />
							<Link to="/auth/signin" style={{ display: "inline-block", marginTop: 20 }}>
								{t("Back Sign in")}
							</Link>
						</>
					)}
				</Col>
			</Row>
		</Container>
	);
};

export default ResetPassword;
