import React, { useCallback, useEffect, useState } from "react";
import CropAvatar from "../../components/avatar/CropAvatar";
import PersonalInfo from "../../components/employee/PersonalInfo";
import { Row, Col, Typography, Space } from "antd";
import { useDispatch } from "react-redux";
import { imageActions } from "../../redux-flow/image/image-slice";
import { useTranslation } from "react-i18next";
import {
  getEmployeeByIdAPI,
  getEmployeeImageByIdAPI,
} from "../../services/employee";
import Container from "../../components/UI/Container/Container";
import { USER_STORAGE } from "../../constants/appConst";
import "assets/pages/homePage.css";

const { Title } = Typography;

const EmployeeInformation = (props) => {
  const { t } = useTranslation();

  let user = JSON.parse(localStorage.getItem(USER_STORAGE));

  const id_userLogin = user.id;

  const [employeesDetail, setEmployeesDetail] = useState();
  const [imageBase64, setImageBase64] = useState();
  const [image, setImage] = React.useState(null);
  const [fullName, setFullName] = React.useState();

  const dispatch = useDispatch();

  // fetch info
  const fetchAPIGetEmployee = useCallback(() => {
    getEmployeeByIdAPI(id_userLogin)
      .then((response) => {
        setEmployeesDetail(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [id_userLogin]);
  // fetch image
  const fetchAPIGetEmployeeImage = useCallback(() => {
    getEmployeeImageByIdAPI(id_userLogin)
      .then((response) => {
        setImageBase64(response.data);
        //save store
        dispatch(imageActions.fetchingAvatar(response.data));
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [dispatch, id_userLogin]);

  const callBack = React.useCallback((childData) => {
    setImage(childData);
  }, []);
  const callBackFN = React.useCallback((childData) => {
    setFullName(childData);
  }, []);

  useEffect(() => {
    fetchAPIGetEmployee();
    fetchAPIGetEmployeeImage();
  }, [fetchAPIGetEmployeeImage, fetchAPIGetEmployee]);

  return (
    <Container className="home-page">
      <Row>
        <Col span={24}>
          <Space
            xs={{ direction: "horizontal" }}
            xl={{ direction: "vertical" }}
            className="home-page__general-info"
          >
            <CropAvatar parentCallback={callBack} imagePath={imageBase64} />
            <div style={{ paddingLeft: "30px" }}>
              <Title level={4}>
                {t("Welcome")} {fullName}
              </Title>
            </div>
          </Space>
          <div className="home-page__form-wrapper">
            <PersonalInfo
              data={employeesDetail}
              imageMultiPart={image}
              parentCallback={callBackFN}
            />
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default EmployeeInformation;
