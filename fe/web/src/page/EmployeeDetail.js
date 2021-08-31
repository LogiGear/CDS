import React, { useCallback, useEffect, useState, useMemo } from "react";
import CropAvatar from "../components/avatar/CropAvatar";
import PersonalInfo from "../components/employee/PersonalInfo";
import { Typography, Space, Skeleton } from "antd";
import { useDispatch } from "react-redux";
import { imageActions } from "../redux-flow/image/image-slice";
import { useTranslation } from "react-i18next";
import {
  getEmployeeByIdAPI,
  getEmployeeImageByIdAPI,
} from "../services/employee";
import Container from "../components/UI/Container/Container";
import { USER_STORAGE } from "../constants/appConst";
import "assets/pages/employeeDetail.css";
import WorkingInfo from "components/employee/WorkingInfo";
import UpdateRoleComponent from "components/admin/updateRole";
import { ADMIN, CDM, MANAGER } from "constants/roleType";
import { useParams } from "react-router-dom";
import { getRoleByUserAPI } from "../services/admin";
import ResumeInfo from "components/employee/ResumeInfo";
import { contains } from "utils/commom";
import ErrorPage from "./ErrorPage";
import { Tabs } from 'antd';

const { TabPane } = Tabs;


const { Title } = Typography;

const EmployeeDetail = (props) => {
  const { t } = useTranslation();
  const [employeesDetail, setEmployeesDetail] = useState();
  const [listRolesDefault, setListRolesDefault] = useState();
  const [imageBase64, setImageBase64] = useState();
  const [image, setImage] = useState(null);
  const [fullName, setFullName] = useState();
  const dispatch = useDispatch();
  const params = useParams();
  let user = JSON.parse(localStorage.getItem(USER_STORAGE));
  const listRole = user.roles.map((role) => role.name);

  const employeeId = useMemo(() => {
    return params?.idEmployee ? +params.idEmployee : user.id;
  }, [params, user.id]);

  // fetch info
  const fetchAPIGetEmployee = useCallback(() => {
    getEmployeeByIdAPI(employeeId)
      .then((response) => {
        setEmployeesDetail(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [employeeId]);

  const fetchAPIRoleByUse = useCallback(() => {
    getRoleByUserAPI(employeeId)
      .then((response) => {
        setListRolesDefault(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [employeeId]);

  // fetch image
  const fetchAPIGetEmployeeImage = useCallback(() => {
    getEmployeeImageByIdAPI(employeeId)
      .then((response) => {
        setImageBase64(response.data);
        //save store
        dispatch(imageActions.fetchingAvatar(response.data));
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [dispatch, employeeId]);

  const callBack = useCallback((childData) => {
    setImage(childData);
  }, []);
  const callBackFN = useCallback((childData) => {
    setFullName(childData);
  }, []);

  useEffect(() => {
    fetchAPIGetEmployee();
    fetchAPIGetEmployeeImage();
    fetchAPIRoleByUse();
    return () => dispatch(imageActions.isEditAvatar(false)); // set isEditAvatar to false when component destroyed (when user leave homepage)
  }, [
    fetchAPIGetEmployeeImage,
    fetchAPIGetEmployee,
    fetchAPIRoleByUse,
    dispatch,
  ]);


  function callback(key) {
    console.log(key);
  }

  //Show 403 Error Page if Manager/CDM view employees who are not under their management
  if (
    !!employeesDetail &&
    !contains([ADMIN], listRole) &&
    contains([CDM, MANAGER], listRole) &&
    employeesDetail?.manager?.user?.id !== user.id && employeesDetail?.cdm?.user?.id !== user.id &&
    user.id !== employeeId
  ) {
    return <ErrorPage code={403} />;
  }
  return (
    <Container className="employee-detail">



      <Space>
        <CropAvatar parentCallback={callBack} imagePath={imageBase64} />
        <Title level={4}>{t(fullName)}</Title>
        <br />
      </Space>




      <Tabs
        tabPosition="top"
        //animated
        defaultActiveKey="1"
        onChange={callback}
        style={{ 'marginTop': '20px' }}
      >
        <TabPane tab="Personal Info" key="1">
          {!!employeesDetail ? (
            <PersonalInfo
              data={{ ...employeesDetail, id: employeeId }}
              imageMultiPart={image}
              parentCallback={callBackFN}
            />
          ) : (
            <Skeleton active />
          )}
        </TabPane>
        <TabPane tab="Working Info" key="2">
          {!!employeesDetail ? (
            <WorkingInfo data={{ ...employeesDetail, id: employeeId }} />
          ) : (
            <Skeleton active />
          )}
        </TabPane>
        <TabPane tab="Resume" key="3">
          <ResumeInfo employeeData={{ ...employeesDetail, id: employeeId }} />
        </TabPane>

        {user.roles.map((x) => x.name).includes(ADMIN) && (
          <TabPane tab="Role" key="4">


            {!!employeesDetail && !!listRolesDefault ? (
              <UpdateRoleComponent
                employeesDetail={employeesDetail}
                listRolesDefault={listRolesDefault}
              />
            ) : (
              <Skeleton active />
            )}
          </TabPane>
        )}
      </Tabs>

      {/* <div className="employee-detail__form-wrapper">
        {!!employeesDetail ? (
          <PersonalInfo
            data={{ ...employeesDetail, id: employeeId }}
            imageMultiPart={image}
            parentCallback={callBackFN}
          />
        ) : (
          <Skeleton active />
        )}
      </div>
      <div className="employee-detail__form-wrapper">
        {!!employeesDetail ? (
          <WorkingInfo data={{ ...employeesDetail, id: employeeId }} />
        ) : (
          <Skeleton active />
        )}
      </div>
      <div className="employee-detail__form-wrapper form">
        <ResumeInfo employeeData={{ ...employeesDetail, id: employeeId, fullName: fullName }} />
      </div>
      
      {user.roles.map((x) => x.name).includes(ADMIN) && (
        <div className="employee-detail__form-wrapper">
          {!!employeesDetail && !!listRolesDefault ? (
            <UpdateRoleComponent
              employeesDetail={employeesDetail}
              listRolesDefault={listRolesDefault}
            />
          ) : (
            <Skeleton active />
          )}
        </div>
      )}
      
      */}
    </Container>
  );
};

export default EmployeeDetail;
