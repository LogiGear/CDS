import { Row, Col, Button, Space } from "antd";
import moment from 'moment';
import Title from "antd/lib/typography/Title";
import { EditOutlined } from "@ant-design/icons";
import { Formik } from "formik";
import {
  Form,
  Select,
  FormItem,
  SubmitButton,
  Input,
  DatePicker,
} from "formik-antd";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import * as Yup from "yup";
import { PRIMARY_RED } from "constants/constants";
import { Prompt } from "react-router-dom";
import { getDepartmentListAPI, getProjectListAPI } from "services/employee";
import {
  getUserByRole,
  updateManagerProjectDepartment,
} from "services/admin";
import { updateEmployeeProjectDepartment } from "services/manager";
import { useSelector } from "react-redux";
import { contains, transformToSelections } from "utils/commom";
import { ADMIN, CDM, MANAGER } from "constants/roleType";
const WorkingInfo = ({ data }) => {
  const {
    department,
    employeeID,
    jobTitle,
    manager,
    cdm,
    project,
    projects,
    email,
    startDate,
    id: idEmployee,
  } = data;
  const [isEdit, setIsEdit] = useState(false);
  const [initialValues, setInitialValues] = useState({
    manager: manager?.id,
    jobTitle,
    department: department?.id,
    employeeID,
    // project: !!projects.length ? projects.map((project) => project.id) : null,
    project,
    email,
    startDate,
    cdm: cdm?.id,
  });

  const [departmentOptions, setDepartmentOptions] = useState(
    transformToSelections(department),
  );
  const [projectOptions, setProjectOptions] = useState(
    transformToSelections(projects),
  );
  const [managerOptions, setManagerOptions] = useState(
    transformToSelections(manager),
  );
  const [cdmOptions, setCdmOptions] = useState(transformToSelections(cdm));

  const { user } = useSelector((state) => state.authentication);
  const listRole = user.roles.map((role) => role.name);

  const { t } = useTranslation();
  const validationSchema = Yup.object({
    department: Yup.string()
      .required(t("Please select a department"))
      .nullable(),
    project: Yup.string().required(t("Please select a project")).nullable(),
    manager: contains([ADMIN], listRole)
      ? Yup.string().required(t("Please select a manager")).nullable()
      : null,
    cdm: contains([ADMIN], listRole)
      ? Yup.string().required(t("Please select a CDM")).nullable()
      : null,
  });

  const handleSubmit = (values, actions) => {
    const dataRequest = {
      id: idEmployee,
      department: values.department,
      //   project: values.project,
      projects: [values.project],
      managerId: values.manager,
      cdmId: values.cdm,
    };

    if (contains([ADMIN], listRole)) {
      updateManagerProjectDepartment(dataRequest)
        .then((response) => {
          setInitialValues(values);
          setIsEdit(false);
          actions.setSubmitting(false);
        })
        .catch(function (error) {
          if (error.response.data.message === "AUTHORITY_NOT_ACCEPTED") {
            setInitialValues(values);
            setIsEdit(false);
            actions.setSubmitting(false);
            return;
          }
          actions.setSubmitting(false);
        });
    }
    if (contains([CDM, MANAGER], listRole)) {
      updateEmployeeProjectDepartment(dataRequest)
        .then((response) => {
          setInitialValues(values);
          setIsEdit(false);
          actions.setSubmitting(false);
        })
        .catch(function (error) {
          if (error.response.data.message === "AUTHORITY_NOT_ACCEPTED") {
            setInitialValues(values);
            setIsEdit(false);
            actions.setSubmitting(false);
            return;
          }
          console.log(error.response);
          actions.setSubmitting(false);
        });
    }
    

    // Promise.all([updateProjectDepartment(idEmployee, dataRequest)])
    // 	.then((response) => {
    // 		setInitialValues(values);
    // 		setIsEdit(false);
    // 		actions.setSubmitting(false);
    // 	})
    // 	.catch(function (error) {
    // 		if (error.response.data.message === "AUTHORITY_NOT_ACCEPTED") {
    // 			setInitialValues(values);
    // 			setIsEdit(false);
    // 			actions.setSubmitting(false);
    // 			return;
    // 		}

    // 		console.log(error.response);
    // 		actions.setSubmitting(false);
    // 	});
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

  const handleCancel = (resetForm) => {
    setIsEdit(false);
    resetForm();
  };
  const disabledDateTime = () => {
    return {
      disabledHours: () => range(0, 24).splice(4, 20),
      disabledMinutes: () => range(30, 60),
      disabledSeconds: () => [55, 56],
    };
  }

  useEffect(() => {
    const promiseArray = !contains([ADMIN], listRole)
      ? [getDepartmentListAPI(), getProjectListAPI()]
      : [
          getDepartmentListAPI(),
          getProjectListAPI(),
          getUserByRole("MANAGER"),
          getUserByRole("CDM"),
        ];

    Promise.all(promiseArray)
      .then((resultArray) => {
        const departmentList = resultArray[0]?.data.map((item) => {
          return { label: item.name, value: item.id };
        });
        const projectList = resultArray[1]?.data.map((item) => {
          return { label: item.name, value: item.id };
        });
        const managerList = resultArray[2]?.data[0].users.filter((user) => user.email !== email)
        .map(filterUser => {
            return { label: filterUser.email, value: filterUser.id };
        });
        const cdmList = resultArray[3]?.data[0].users.filter((user) => user.email !== email)
        .map(filterUser => {
            return { label: filterUser.email, value: filterUser.id };
        });

        setDepartmentOptions(departmentList);
        setProjectOptions(projectList);
        setManagerOptions(managerList);
        setCdmOptions(cdmList);
      })
      .catch((err) => {
        console.log(err.response);
      });
  }, []);

  return (
    <>
      <Row justify="space-between">
        <Title level={4}></Title>
        {((!isEdit && contains([ADMIN], listRole)) ||
          (!isEdit &&
            !contains([ADMIN], listRole) &&
            contains([MANAGER], listRole) &&
            idEmployee !== user.id)) && (
          <Button className="main-button" onClick={() => setIsEdit(true)}>
            Edit
          </Button>
        )}
      </Row>
      <Formik
        onSubmit={handleSubmit}
        validationSchema={validationSchema}
        initialValues={initialValues}
        enableReinitialize
      >
        {({ resetForm, dirty }) => (
          <Form className="form" layout="vertical">
            <Prompt
              when={dirty}
              message="You have unsaved changes, do you want to leave?"
            />
            <Row gutter={64}>
              <Col lg={12} xs={24}>
                <FormItem name="employeeID" label={t("Employee ID")}>
                  <Input name="employeeID" readOnly></Input>
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <FormItem name="jobTitle" label={t("Job Title")}>
                  <Input name="jobTitle" readOnly></Input>
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <FormItem name="email" label={t("Email")}>
                  <Input name="email" readOnly></Input>
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <FormItem name="startDate" label={t("Start Date")}>
                  <DatePicker name="startDate" format="DD MMM YYYY" disabledDate={disabledDate}
                  disabledTime={disabledDateTime} inputReadOnly />
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <div className="label">
                  {t("Department")}
                  {isEdit && " *"}
                  {isEdit && contains([ADMIN, MANAGER], listRole) && (
                    <EditOutlined style={{ color: PRIMARY_RED }} />
                  )}
                </div>
                <FormItem name="department" labelCol={{ span: 24 }}>
                  <Select
                    name="department"
                    options={departmentOptions}
                    disabled={!isEdit || !contains([ADMIN, MANAGER], listRole)}
                  ></Select>
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <div className="label">
                  {t("Direct Manager")}
                  {isEdit && contains([ADMIN], listRole) && " *"}
                  {isEdit && contains([ADMIN], listRole) && (
                    <EditOutlined style={{ color: PRIMARY_RED }} />
                  )}
                </div>
                <FormItem name="manager" labelCol={{ span: 24 }}>
                  <Select
                    name="manager"
                    options={managerOptions}
                    disabled={!isEdit || !contains([ADMIN], listRole)}
                  ></Select>
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <div className="label">
                  {t("Project")}
                  {isEdit && " *"}
                  {isEdit && contains([ADMIN, MANAGER], listRole) && (
                    <EditOutlined style={{ color: PRIMARY_RED }} />
                  )}
                </div>
                <FormItem name="project">
                  {/* <Select
                                        name="projects"
                                        mode="multiple"
                                        allowClear
                                        placeholder={t("Select Project")}
                                        options={projectOptions}
                                        disabled={!isEdit || !contains([ADMIN,MANAGER], listRole)}
                                    ></Select> */}

                  <Select
                    name="project"
                    options={projectOptions}
                    disabled={!isEdit || !contains([ADMIN, MANAGER], listRole)}
                  />
                </FormItem>
              </Col>
              <Col lg={12} xs={24}>
                <div className="label">
                  {t("CDM")}
                  {isEdit && contains([ADMIN], listRole) && " *"}
                  {isEdit && contains([ADMIN], listRole) && (
                    <EditOutlined style={{ color: PRIMARY_RED }} />
                  )}
                </div>
                <FormItem name="cdm" labelCol={{ span: 24 }}>
                  <Select
                    name="cdm"
                    options={cdmOptions}
                    disabled={!isEdit || !contains([ADMIN], listRole)}
                  ></Select>
                </FormItem>
              </Col>
            </Row>
            <Row
              justify="space-between"
              align="middle"
              style={{ minHeight: 32 }}
            >
              {isEdit && (
                <>
                  <Space size="large">
                    <i>* {t("Required")}</i>
                    <i>
                      <EditOutlined style={{ color: PRIMARY_RED }} />{" "}
                      {t("Editable")}
                    </i>
                  </Space>

                  <Space>
                    <Button
                      className="secondary-button"
                      onClick={() => handleCancel(resetForm)}
                    >
                      {t("Cancel")}
                    </Button>
                    <SubmitButton className="main-button" disabled={!dirty}>
                      {t("Save")}
                    </SubmitButton>
                  </Space>
                </>
              )}
            </Row>
          </Form>
        )}
      </Formik>
    </>
  );
};

export default WorkingInfo;
