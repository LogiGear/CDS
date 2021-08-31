import React, { useEffect, useMemo, useState } from "react";
import { Button, Table, Row, Col, Radio, Modal, Select, Alert } from "antd";
import { useTranslation } from "react-i18next";
import "../../assets/pages/list-employee.css";
import SelectFiled from "../../components/ant-custom/selectFiled";
import { useSelector } from "react-redux";
import { Sorter } from "utils/sorter";
import GridEmployee from "./gridEmployee";
import { AppstoreFilled, MenuOutlined, DownloadOutlined } from "@ant-design/icons";

const TableEmployee = (props) => {
  const [editableColumns, setEditableColumns] = useState([
    "id",
    "fullName",
    "email",
    "action",
  ]);
  const { dataEmpl, onNavigation, listDepartments, listGender, filterSuccess, messageError, isButtonDownDisabled, handleDownloadFile } =
    props;
  const [isGrid, setIsGrid] = useState(false);
  const [isModalShown, setIsModalShown] = useState(false);
  const [modalOptions, setModalOptions] = useState(
    editableColumns.slice(0, editableColumns.length - 1),
  );
  const { t } = useTranslation();
  const { textSearch } = useSelector((state) => state.search);

  const [dataFilter, setDataFilter] = useState({
    departments: [],
    gender: [],
    textSearch: "",
  });
  const defaultColumns = [
    {
      title: "Id",
      dataIndex: "id",
      key: "id",
      sorter: (a, b) => Sorter.NUMBER(a.id, b.id),
    },
    {
      title: "Name",
      dataIndex: "fullName",
      key: "id",
      sorter: (a, b) => Sorter.TEXT(a.fullName, b.fullName),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "id",
      sorter: (a, b) => Sorter.TEXT(a.user.email, b.user.email),
      render(text, row) {
        return <>{row?.user?.email}</>;
      },
    },
    {
      title: "Gender",
      dataIndex: "gender",
      key: "id",
      sorter: (a, b) => Sorter.TEXT(a?.gender || "", b?.gender || ""),
    },
    {
      title: "Department",
      dataIndex: "department",
      key: "id",
      sorter: (a, b) =>
        Sorter.TEXT(a?.department?.name || "", b?.department?.name || ""),
      render(text, row) {
        return <>{row?.department?.name}</>;
      },
    },
    {
      title: "Job Title",
      dataIndex: "jobTitle",
      key: "id",
      sorter: (a, b) => Sorter.TEXT(a?.jobTitle || "", b?.jobTitle || ""),
      render(text, row) {
        return <>{row?.jobTitle}</>;
      },
    },
    {
      title: "Project",
      dataIndex: "project",
      key: "id",
      sorter: (a, b) =>
        Sorter.TEXT(a?.project?.[0].name || "", b?.project?.[0].name || ""),
      render(text, row) {
        return <>{row?.project?.[0].name}</>;
      },
    },
    {
      title: "Major",
      dataIndex: "major",
      key: "id",
      sorter: (a, b) => Sorter.TEXT(a?.major || "", b?.major || ""),
      render(text, row) {
        return <>{row?.major}</>;
      },
    },
    {
      title: (
        <Row justify="end">
          <Button
            className="secondary-button-outlined"
            onClick={() => setIsModalShown(true)}
          >
            {t("Edit Column")}
          </Button>
        </Row>
      ),
      dataIndex: "action",
      render(text, row) {
        return (
          <Row justify="end">
            <Button
              style={{'marginRight':'5px'}}
              
              onClick={() => onNavigation(row?.id)}
              type="link"
            >
              {t("View")}
            </Button>
            <Button onClick={() => handleDownloadFile(row?.id,row?.fullName)} 
              className="download-button"
              icon={<DownloadOutlined />}
              disabled={isButtonDownDisabled}>
             {t("Resume")}
          </Button>
          </Row>
        );
      },
    },
  ];

  const columns = useMemo(() => {
    return defaultColumns.filter((column) =>
      editableColumns.includes(column.dataIndex),
    );
  }, [editableColumns]);

  const onFilterGender = (gender) => {
    setDataFilter({ ...dataFilter, gender: gender });
  };

  const onFilterDepartments = (department) => {
    setDataFilter({ ...dataFilter, departments: department });
  };

  useEffect(() => {
    console.log(textSearch);
    filterSuccess({ ...dataFilter, textSearch: textSearch });
  }, [textSearch]);

  const handleConfirmModal = () => {
    console.log("OK");
    setEditableColumns([...modalOptions, "action"]);
    setIsModalShown(false);
  };

  const handleCancelModal = () => {
    console.log("CANCEL");
    setIsModalShown(false);
    setModalOptions(editableColumns.slice(0, editableColumns.length - 1));
  };

  // const filterEmploy = dataEmpl.filter((empl) => empl.user.email !== "admin@gmail.com")
  // .map(filterUser => {
  //     return filterUser;
  // });

  return (
    <>
      <Row gutter={8} align="bottom">
        <Col xs={24} md={8} xl={8}>
          <div className="title-input"> {t("By departments")} </div>
          <SelectFiled
            allowClear
            placeholder={t("Select departments")}
            defaultValue={[]}
            listOption={listDepartments}
            onChange={(department) => onFilterDepartments(department)}
          />
        </Col>
        <Col xs={24} md={8} xl={8}>
          <div className="title-input"> {t("Gender")} </div>
          <SelectFiled
            allowClear
            placeholder={t("Select Gender")}
            defaultValue={[]}
            listOption={listGender}
            onChange={(skills) => onFilterGender(skills)}
          />
        </Col>

        <Col xs={24} md={2} xl={2}>
          <Button
            type="danger"
            onClick={() => {
              filterSuccess({ ...dataFilter, textSearch: textSearch });
            }}
            className="main-button"
          >
            {t("Apply")}
          </Button>
        </Col>
        <Col xs={24} md={6} xl={6}>
          <Row justify="end">
            <Radio.Group 
              defaultValue="list"
              onChange={(e) => setIsGrid(e.target.value === "grid")}
              
            >
              <Radio.Button value="list" style={{theme:'red'}}>
                <MenuOutlined /> {t("List")}
              </Radio.Button>
              <Radio.Button value="grid" >
              <AppstoreFilled /> {t("Grid")}
              </Radio.Button>
            </Radio.Group>
          </Row>
        </Col>
      </Row>
      {!isGrid ? (
        <>
        <Table
          style={{ paddingTop: "10px" }}
          dataSource={dataEmpl}
          columns={columns}
          rowKey="id"
          responsive={true}
          footer={() =>  {if (messageError===500) 
            return (<Alert
                    message="This user don't have resume"
                    type="warning"
                />)
              if (messageError===429)
            return (<Alert
              message="Too many request wait some seconds"
              type="warning"
          />)}}
          pagination={{
            defaultPageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `${t("Total")} ${total} ${t("items")}`,
            total: dataEmpl?.length || 0,
            responsive: true,
          }}
          scroll={{ x: 700 }}
        />
              </>
      ) : (
        <GridEmployee employeeData={dataEmpl} />
      )}
      <Modal
        maskClosable={false}
        title="Edit Columns"
        visible={isModalShown}
        onCancel={handleCancelModal}
        onOk={handleConfirmModal}
      >
        <Select
          mode="multiple"
          defaultValue={editableColumns.slice(0, editableColumns.length - 1)}
          value={modalOptions}
          style={{ width: "100%" }}
          onChange={(values) => setModalOptions(values)}
        >
          {defaultColumns.slice(0, defaultColumns.length - 1).map((column) => (
            <Select.Option
              key={column.dataIndex}
              disabled={["id", "fullName", "email"].includes(column.dataIndex)}
            >
              {column.title}
            </Select.Option>
          ))}
        </Select>
      </Modal>
      
    </>
  );
};

export default TableEmployee;
