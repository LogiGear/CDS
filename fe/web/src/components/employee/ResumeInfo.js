import React, { useEffect, useState, useCallback } from "react";
import { useTranslation } from "react-i18next";
import { SubmitButton, Input, Form, FormItem, } from "formik-antd";
import {
  Row,
  Col,
  Typography,
  Upload,
  Button,
  Space,
  Alert,
  Dropdown,
  Menu,
  Modal,
} from "antd";
import { Formik } from "formik";
import { FileWordOutlined, DownloadOutlined, UploadOutlined } from "@ant-design/icons";
import {
  downloadFiledAPI,
  uploadFileByIdAPI,
  getFileDetailAPI,
  updateStatusFile,
} from "services/file";
import { MANY_REQUEST } from "constants/messageConst";
import fileDownload from "js-file-download";
import { useSelector } from "react-redux";
import { contains } from "utils/commom";
import { ADMIN, CDM, EMPLOYEE, MANAGER } from "constants/roleType";
import moment from "moment";
const { Link, Text } = Typography;

const ResumeInfo = ({ isEdit, employeeData }) => {
  const {

    fullName: fullName,
    id: idEmployee,
  } = employeeData;
  const { t } = useTranslation();
  const [resume, setResume] = useState(null);
  const [file, setFile] = useState(null);
  const [fileNameRes, setFileNameRes] = useState("No resume");
  const [message, setMessage] = useState([]);
  const [status, setStatus] = useState(0);
  const [isButtonDownDisabled, setIsButtonDownDisabled] = useState(false);
  const { user } = useSelector((state) => state.authentication);
  const listRole = user.roles.map((role) => role.name);
  const [uploadDate, setUploadDate] = useState(null);
  const [seconds, setSeconds] = React.useState(6);
  const [countDown, setCountDown] = React.useState(false);
  const [fileName, setFileName] = useState(null);
  const [statusText, setStatusText] = useState(null);
  const [updateResume, setUpdateResume] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [modalLoading, setModalLoading] = useState(false);
  const [updatedNote, setUpdatedNote] = useState(null);
  const [messageFileName, setMessageFileName] = useState(null);
  const [uploadMoment, setUploadMoment] = useState(null);
  const [monthDiff, setMonthDiff] = useState(null);
  const [data, setData] = useState({
    status: "To be updated",
    note: ""
  });
  const idUser = user.id;

  const showModal = () => {
    setModalVisible(true)
  };

  const handleOk = (values) => {
    setModalLoading(true);
    callUpdateStatusFile(values)


  };

  const handleCancel = () => {
    setModalVisible(false);
  };


  // console.log("months diff", getTimeDiffByMonths(uploadMoment, moment(Date.now())))

  const getTimeDiffByMonths = (moment1, moment2) => {
    var now = moment(moment2); //todays date
    var end = moment(moment1); // another date
    var duration = moment.duration(now.diff(end));
    const months = duration.months();
    return months;
  }
  useEffect(() => {
    setMonthDiff(getTimeDiffByMonths(uploadMoment, moment(Date.now())))
  }, [uploadMoment])
  useEffect(() => {
    if (seconds > 0 && countDown) {
      setIsButtonDownDisabled(true);
      setTimeout(() => {
        setSeconds(seconds - 1);
      }, 1000);
    } else {
      setMessage(null);
      setStatus(1);
      setIsButtonDownDisabled(false);
    }
  }, [countDown, seconds]);

  const menu = (
    <Menu>
      {status !== 0 && (
        <Menu.Item
          key="1"
          onClick={() => {
            const status = { status: "To be updated" };
            const data = JSON.stringify(status);

            setStatus(0);
            showModal();
          }}
        >
          To be updated
        </Menu.Item>
      )}
      {status !== 2 && (
        <Menu.Item
          key="2"
          onClick={() => {
            const status = { status: "Verified" };
            const data = JSON.stringify(status);
            callUpdateStatusFile(data);
            setStatus(2);
          }}
        >
          Verified
        </Menu.Item>
      )}
    </Menu>
  );

  const handleBeforeUpload = (file, fileList) => {
    new Promise((resolve, reject) => {
      const reader = new FileReader();
      setFile(file);
      reader.readAsDataURL(file);
      reader.addEventListener("load", ({ target }) => resolve(target.result));
      reader.addEventListener("error", () =>
        reject(t("something went wrong while reading file"))
      );
    })
      .then((dataUrl) => {
        console.log(dataUrl);
        setResume(dataUrl);
      })
      .catch((err) => console.error(err));

    return false;
  };

  const callBackDate = React.useCallback((childData) => {
    setUploadDate(moment(new Date(childData)).format("DD/MM/YYYY HH:mm"));
  }, []);
  const callBackNote = React.useCallback((childData) => {
    setUpdatedNote(childData);
  }, []);

  const callBackStatus = React.useCallback((childData) => {
    setStatusText(childData);
  }, [])

  const callBackOverDate = React.useCallback((childData) => {

    setMonthDiff(getTimeDiffByMonths(childData, moment(Date.now())));
  }, [])
  useEffect(() => {
    if (callBackDate && message) {
      callBackDate(message.uploadAt);
    }
    if (callBackDate && message) {
      callBackNote(message.note);
    }
    if (callBackStatus && message) {
      callBackStatus(message.status);
    }
    if (callBackOverDate && message) {
      callBackOverDate(moment(new Date(message.uploadAt)));
    }
  }, [callBackOverDate, callBackDate, callBackNote, callBackStatus, message]);
  // console.log(Date.now()>Date(uploadDate))
  const handleUploadFile = () => {
    const formData = new FormData();
    formData.append("file", file);
    uploadFileByIdAPI(idEmployee, idUser, formData)
      .then((x) => {
        console.log(x.data)
        setMessage(x.data);
        setMessageFileName(x.data);
        setSeconds(6);
        setCountDown(true);
        // setFileNameRes(x.data);
      })
      .catch((err) => {
        setMessage(err.response)
      })
      .finally();
  };

  const callUpdateStatusFile = (data) => {
    updateStatusFile(idEmployee, data).then((response) => {
      setModalVisible(false);
      setModalLoading(false);
      setMessage(response.data)
      console.log("message", response.data)
    });
  };

  const handleDownloadFile = () => {
    downloadFiledAPI(fileNameRes)
      .then((res) => {
        fileDownload(
          res.data,
          "Resume" + "-" + idEmployee + "-" + fullName + ".doc"
        );
        setSeconds(6);
        setCountDown(true);
      })
      .finally();
  };

  const fetchAPIGetFile = useCallback(() => {
    getFileDetailAPI(idEmployee)
      .then((response) => {
        setUploadDate(
          moment(new Date(response.data.uploadAt)).format("DD/MM/YYYY HH:mm")
        );
        setUpdatedNote(response.data.note);
        setUploadMoment(moment(new Date(response.data.uploadAt)))
        const statusCode = response.data.status;
        setStatusText(statusCode);
        if (statusCode === "To be updated") {
          setStatus(0);
        }
        if (statusCode === "Verified") {
          setStatus(2);
        }
      })
      .catch((err) => console.error(err));
  }, [idEmployee]);
  useEffect(() => {
    fetchAPIGetFile();
  }, [fetchAPIGetFile]);
  useEffect(() => {
    //TODO: API to get resume and setResume
    setFileNameRes("Resume-" + idEmployee + "-" + fullName + ".doc");

  }, [setFileNameRes, idEmployee, fullName]);

  useEffect(() => {
    setFileName("Resume" + "-" + idEmployee + "-" + fullName + ".doc");
  }, [setFileName, idEmployee, fullName])

  useEffect(() => {
    setUpdateResume("The information in resume need to be update is " + updatedNote)
  }, [setUpdateResume, updatedNote])
  return (
    <>

      <Row justify="end">
        {/* <Title level={4}>{t("Resume")}</Title> */}
        {/* {contains([EMPLOYEE], listRole) && !(contains([CDM, MANAGER, ADMIN], listRole)) && (
          <Button>
            {statusText}
          </Button>
        )} */}
      </Row>
      <br />
      <Row justify="end">
        {status === 0 &&
          (contains([CDM, MANAGER, ADMIN], listRole)) && (
            <Dropdown.Button key="pending-button" overlay={menu} >
              To be updated
            </Dropdown.Button>

          )}
      </Row>
      <Modal
        visible={modalVisible}
        title={t("Update Status")}
        onOk={handleOk}
        onCancel={handleCancel}
        footer={[

        ]}
      >
        <Formik
          initialValues={data}
          enableReinitialize={true}
          onSubmit={handleOk}>
          {() => (
            <Form layout="vertical" className="form">
              <Row gutter={64}>
                <Col xs={24} lg={12}>
                  <FormItem name="status" label={t("status")}>
                    <Input name="status" readOnly />
                  </FormItem>
                </Col>
                <Col xs={24} lg={12}>
                  <FormItem name="note" label={t("note")}>
                    <Input name="note" />
                  </FormItem>
                </Col>
              </Row>
              <Row justify="space-between" align="middle" style={{ minHeight: 32 }}>
                <Space>
                  <Button key="back" onClick={handleCancel}>
                    {t("Cancel")}
                  </Button>,
                  <SubmitButton key="submit" type="primary" loading={modalLoading} onClick={handleOk}>
                    Submit
                  </SubmitButton>
                </Space>
              </Row>
            </Form>

          )}
        </Formik>
      </Modal>
      <Row justify="end">
        {status === 1 && contains([MANAGER, CDM, ADMIN], listRole) && (
          <Dropdown.Button className="pending-button" overlay={menu}>
            To be reviewed
          </Dropdown.Button>
        )}
      </Row>
      <Row justify="end">
        {status === 2 &&
          (contains([CDM, MANAGER, ADMIN], listRole)) && (
            <Dropdown.Button className="pending-button" overlay={menu}>
              Verified
            </Dropdown.Button>
          )}
      </Row>


      <Space>
        <br />

        {resume ? (
          <Link href={resume} target="_blank">
            -
            <FileWordOutlined style={{ fontSize: 24 }} />{" "}
            {file?.name || "Employee Name"}
          </Link>
        ) : (
          <Text>{t("No Resume")}</Text>
        )}
        {((!isEdit && idEmployee === user.id) ||
          (!isEdit && contains([ADMIN], listRole)) ||
          (!isEdit &&
            !contains([ADMIN], listRole) &&
            contains([MANAGER], listRole))) && (
            <Upload
              multiple={false}
              accept=".doc,.docx"
              beforeUpload={handleBeforeUpload}
              maxCount={1}
              showUploadList={false}
            >
              <Button className="secondary-button-outlined" >
                {resume ? t("Choose Another File") : t("Choose File")}
              </Button>
            </Upload>
          )}
      </Space>
      <br />
      <br />
      <Space>
        {!isEdit && (
          <>
            <Button
              onClick={() => handleUploadFile()}
              className="main-button"
              //disabled={!resume || isButtonDisabled}
              disabled={!resume}
              icon={<UploadOutlined />}
              danger
            >
              {t("Upload")}
            </Button>
          </>
        )}
        { }

        {fileNameRes && (
          <>
            <Button
              onClick={() => handleDownloadFile()}
              disabled={isButtonDownDisabled}
              className="view-button secondary-button"

              icon={<DownloadOutlined />}
            >
              {t("Download")}
            </Button>

          </>
        )}
      </Space>
      <br />
      <br />
      <Space>
       
        {fileName}
        <p style={{ 'margin': '5px', 'justifyContent': 'end' }}>
          {uploadDate}
        </p>
        {statusText}
      </Space>
      <br />
      {/* <p>
        {seconds > 0 &&
          seconds < 6 &&
          "Wait for " + seconds + "s" + " next upload/download"}
      </p> */}
      <br />
      {messageFileName && (
        <>
          <Alert
            message={messageFileName.fileName}
            type={messageFileName === MANY_REQUEST ? "error" : "success"}
            showIcon
          />
          <br />
        </>
      )}
      {updatedNote && (
        <>
          <Alert
            message={updateResume}
            type={updatedNote === MANY_REQUEST ? "error" : "success"}

          />
          <br />
        </>
      )}
      {monthDiff >= 6 && (
        <Alert
          message="Your has not been updated for more than 6 months. Need to be updated"
          type="warning"
        />
      )}
    </>
  );
};

export default ResumeInfo;
