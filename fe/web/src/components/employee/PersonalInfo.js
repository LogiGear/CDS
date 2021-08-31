import React, { useState, useEffect, useMemo } from "react";
import moment from 'moment';
import { Formik } from "formik";
import * as Yup from "yup";
import { useDispatch, useSelector } from "react-redux";
import { updateEmployeeInfoAPI, updateEmployeeImageAPI } from "../../services/employee";
import "../../assets/pages/list-employee.css";
import { SubmitButton, Input, Radio, Form, FormItem, Select, DatePicker } from "formik-antd";
import { Button, Row, Col, Typography, Space } from "antd";
import axios from "axios";
import { imageActions } from "../../redux-flow/image/image-slice";
import { useTranslation } from "react-i18next";
import { Prompt } from "react-router-dom";
import { EditOutlined } from "@ant-design/icons";
import { PRIMARY_RED } from "constants/constants";
import { contains } from "utils/commom";
import { ADMIN } from "constants/roleType";

const PersonalInfo = (props) => {
	const { t } = useTranslation();

	const dispatch = useDispatch();
	const { user } = useSelector((state) => state.authentication);
	const listRole = useMemo(() => user.roles.map((role) => role.name), [user]);

	const id_userLogin = user.id;
	const [formLayout] = useState("vertical");
	const [editDisabled, setEditDisabled] = useState(true);
	const [data, setData] = useState(props.data);
	const [loading, setLoading] = useState(false);
	const [cityOfBirth, setCityOfBirth] = useState();

	const { Title } = Typography;

	const genderOptions = [
		{ label: "Male", value: "Male" },
		{ label: "Female", value: "Female" },
	];
	const religion = [
		{ label: "None", value: "None" },
		{ label: "Buddhism", value: "Buddhism" },
		{ label: "Christ", value: "Christ" },
		{ label: "Catholicism", value: "Catholicism" },
		{ label: "Protestantism", value: "Protestantism" },
		{ label: "Hoahaoism", value: "Hoahaoism" },
		{ label: "Caodaism", value: "Caodaism" },
		{ label: "Other", value: "Other" },
	];
	const phoneRegExp = /(0[3|5|7|8|9])+([0-9]{8})\b/g;

	const getCityOfBirth = async () => {
		const response = await axios.get("https://dc.tintoc.net/app/api-customer/public/provinces");
		let list = [];
		response.data.forEach((doc) => {
			const { name } = doc;

			list.push({ label: name, value: name });
		});

		setCityOfBirth(list);
	};

	useEffect(() => {
		getCityOfBirth();
	}, []);

	useEffect(() => {
		if (props.parentCallback && data) {
			props.parentCallback(data.fullName);
		}
	}, [data, props]);

	const handleSubmit = (values, actions) => {
		callUpdateInfo(values);
		if (props.imageMultiPart) callUpdateImage(props.imageMultiPart);
		dispatch(imageActions.isEditAvatar(false));
		actions.setSubmitting(false);
	};

	const handleCancel = (resetForm) => {
		setEditDisabled(true);
		dispatch(imageActions.isEditAvatar(false));
		resetForm();
	};

	const toggleDisableEdit = () => {
		// setShowPrompt();
		setEditDisabled(!editDisabled);
		dispatch(imageActions.isEditAvatar(true));
	};

	// update image
	const callUpdateImage = (values) => {
		setLoading(true);
		try {
			const formData = new FormData();

			// Update the formData object
			formData.append("imageFile", values, "imageAvatar.png");
			formData.append("id", props.data.id);

			updateEmployeeImageAPI(formData)
				.then((response) => {
					dispatch(imageActions.fetchingAvatar(response.data));
					setLoading(false);
					// message.success(t("Update Image Employee Success"));
				})
				.catch((error) => {
					console.log(error);
				});
		} catch (error) {
			// message.error(t("Update Image Employee Error"));
		}
	};
	// update info
	const callUpdateInfo = (values) => {
		setLoading(true);
		try {
			const data = values;

			updateEmployeeInfoAPI(data)
				.then((response) => {
					setLoading(false);
					setData(values);
					setEditDisabled(true);
					// message.success(t("Update Information Employee Success"));
				})
				.catch(function (error) {
					// message.error(t("Update Information Employee Error"));
				});
		} catch (error) {
			// message.error(t("Update Information Employee Error"));
		}
	};
	const range = (start, end) => {
		const result = [];
		for (let i = start; i < end; i++) {
		  result.push(i);
		}
		return result;
	  }
	  const disabledDate = (current) => {
		return current && (current < moment().endOf('day') || current >= moment().endOf('day'));
	  }
	  const disabledDateTime = () => {
		return {
		  disabledHours: () => range(0, 24).splice(4, 20),
		  disabledMinutes: () => range(30, 60),
		  disabledSeconds: () => [55, 56],
		};
	  }

    console.log(props.imageMultiPart)

	return (
		<>
			<Row justify="space-between" style={{ marginBottom: 30 }}>
				<Title level={4}></Title>

				{editDisabled && (contains([ADMIN], listRole) || id_userLogin === props.data.id) ? (
					<Button type="danger" onClick={toggleDisableEdit} className="main-button">
						{t("Edit")}
					</Button>
				) : null}
			</Row>

			<Formik
				initialValues={data}
				enableReinitialize={true}
				validationSchema={Yup.object({
					fullName: Yup.string()
						.min(2, t("Minimum 2 characters"))
						.max(100, t("Maximum 100 characters"))
						.required(t("Required!")),
					cellPhone: Yup.string()
						.matches(phoneRegExp, t("Phone number is not valid"))
						.required(t("Required!"))
						.nullable(),
				})}
				onSubmit={handleSubmit}
			>
				{({ dirty, resetForm }) => (
					<>
						<Prompt when={dirty} message={t("You have unsaved changes, do you want to leave?")} />
						<Form layout={formLayout} className="form">
							<Row gutter={64}>
								<Col xs={24} lg={12}>
									<div className="label">
										{t("Full name")}{!editDisabled && " *"}
										{!editDisabled && <EditOutlined style={{ color: PRIMARY_RED }} />}
									</div>
									<FormItem name="fullName">
										<Input name="fullName" readOnly={editDisabled} />
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<FormItem name="email" label={t("Email")}>
										<Input name="email" readOnly />
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<FormItem name="birthDay" label={t("Date of Birth")}>
										<DatePicker name="birthDay" format="DD MMM YYYY" disabledDate={disabledDate}
                  					disabledTime={disabledDateTime} inputReadOnly/>
									</FormItem>
								</Col>
								<Col xs={24} lg={12}>
									<div className="label">
										{t("Place of Birth")}
										{!editDisabled && <EditOutlined style={{ color: PRIMARY_RED }} />}
									</div>
									<FormItem name="birthDay">
										<Select
											options={cityOfBirth}
											name="placeOfBirth"
											placeholder="placeOfBirth"
											disabled={editDisabled}
										/>
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<div className="label">
										{t("Gender")}
										{!editDisabled && <EditOutlined style={{ color: PRIMARY_RED }} />}
									</div>
									<FormItem name="gender">
										<Radio.Group name="gender" options={genderOptions} disabled={editDisabled} />
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<div className="label">
										{t("Religion")}
										{!editDisabled && <EditOutlined style={{ color: PRIMARY_RED }} />}
									</div>
									<FormItem name="religion">
										<Select
											options={religion}
											name="religion"
											placeholder="religion"
											disabled={editDisabled}
										/>
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<div className="label">
										{t("Phone Number")}{!editDisabled && " *"}
										{!editDisabled && <EditOutlined style={{ color: PRIMARY_RED }} />}
									</div>
									<FormItem name="cellPhone">
										<Input name="cellPhone" pattern="[0-9]*" readOnly={editDisabled} />
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<FormItem name="bankAccount" label={t("Account Number")}>
										<Input name="bankAccount" readOnly />
									</FormItem>
								</Col>
							</Row>
							{/* Department Information */}
							<>
								{/* <Row justify="space-around">
								<Col span={24}>
									<Title level={4}>{t("Department Information")}</Title>
								</Col>
								<Col xs={24} lg={12}>
									<FormItem name="employeeID" label={t("Employee ID")}>
										<Input name="employeeID" readOnly />
									</FormItem>
								</Col>
								<Col span={2}></Col>
								<Col xs={24} lg={12}>
									<FormItem name="Managementemail" label={t("Management email")}>
										<Input name="Managementemail" readOnly />
									</FormItem>
								</Col>

								<Col xs={24} lg={12}>
									<FormItem name="department" label={t("Department")}>
										<Input name="department" readOnly />
									</FormItem>
								</Col>
								<Col span={2}></Col>
								<Col xs={24} lg={12}>
									<FormItem name="cdmEmail" label={t("CDM Email")}>
										<Input name="cdmEmail" readOnly />
									</FormItem>
								</Col>
								<Col xs={24} lg={12}>
									<FormItem name="jobTitle" label={t("Job Title")}>
										<Input name="jobTitle" readOnly />
									</FormItem>
								</Col>
								<Col span={2}></Col>
								<Col xs={24} lg={12}>
									<FormItem name="startDate" label={t("Start Date")}>
										<DatePicker name="startDate" format="DD MMM YYYY" disabled />
									</FormItem>
								</Col>
							</Row> */}
							</>
							<Row justify="space-between" align="middle" style={{ minHeight: 32 }}>
								{!editDisabled && (
									<>
										<Space size="large">
											<i>* {t("Required")}</i>
											<i>
												<EditOutlined style={{ color: PRIMARY_RED }} /> {t("Editable")}
											</i>
										</Space>
										<Space>
											<Button
												className="secondary-button"
												onClick={() => handleCancel(resetForm)}
												style={{ marginRight: "10px" }}
											>
												{t("Cancel")}
											</Button>
											<SubmitButton
												type="danger"
												loading={loading}
												className="main-button"
												disabled={!(dirty || props.imageMultiPart)}
											>
												{t("Save")}
											</SubmitButton>
										</Space>
									</>
								)}
							</Row>
						</Form>
					</>
				)}
			</Formik>

			{/* {Prompt} */}
		</>
	);
};

export default PersonalInfo;
