import React, { useState, useEffect } from "react";
import "../../assets/pages/list-employee.css";

import { Button, Row, Col, Typography } from "antd";

import { useTranslation } from "react-i18next";
import SelectFiled from "components/ant-custom/selectFiled";
import {  updateRoleAPI } from "services/admin";
import { getListRoleAPI } from "services/employee";

const UpdateRoleComponent = (props) => {
  const { employeesDetail, listRolesDefault } = props;
  const { t } = useTranslation();
  const [editDisabled, setEditDisabled] = useState(true);
  const [loading, setLoading] = React.useState(false);
  const [listRole, setListRole] = useState([]);
  const [listRoleChange, setListRoleChange] = useState([]);

  // const [Prompt, setShowPrompt, setNOTShowPrompt] = useUnsavedChangesWarning();

  const { Title } = Typography;

  const fectAllRole = () => {
    getListRoleAPI().then(({ data }) => {
      setListRole(data);
    });
  };

  useEffect(() => {
    fectAllRole();
  }, []);

  useEffect(() => {}, [employeesDetail]);

  const onUpdateRoleFindId = () => {
    setLoading(true);
    setEditDisabled(!editDisabled);
    var data = { id: employeesDetail?.id, roles: listRoleChange };
    updateRoleAPI(data)
      .then((x) => {})
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <>
      <Row justify="space-between" style={{ marginBottom: 30 }}>
        <Title level={4}></Title>
        {editDisabled ? (
          <Button
            type="danger"
            onClick={() => {
              setEditDisabled(!editDisabled);
            }}
            className="main-button"
          >
            {t("Edit")}
          </Button>
        ) : null}
      </Row>

      <Row>
        <Col xs={24} lg={11}>
          <SelectFiled
            allowClear
            disable={editDisabled}
            placeholder={t("Select role")}
            defaultValue={[...listRolesDefault.map((x) => x.id)]}
            listOption={listRole}
            onChange={(e) => setListRoleChange(e)}
          />
        </Col>
      </Row>
      {editDisabled ? null : (
        <div
          style={{ float: "right", paddingBottom: "50px", paddingTop: "10px" }}
        >
          <Button.Group>
            <Button
              className="secondary-button"
              onClick={() => setEditDisabled(true)}
              style={{ marginRight: "10px" }}
            >
              {t("Cancel")}
            </Button>
            <Button
              type="danger"
              onClick={() => onUpdateRoleFindId()}
              loading={loading}
              className="main-button"
            >
              {t("Save")}
            </Button>
          </Button.Group>
        </div>
      )}
    </>
  );
};

export default UpdateRoleComponent;
