import React, { useState, useEffect } from "react";
import { Row, Col, Spin, message } from "antd";
// import "../../assets/pages/list-employee.css";
import { getEmployeeAPI, getListRoleAPI } from "../../services/employee";
import { updateRoleAPI } from "../../services/admin";
import TableUpdateRole from "../../components/admin/tableUpdateRole";
import Container from "../../components/UI/Container/Container";
import { useTranslation } from "react-i18next";

const UpdateListRole = (props) => {
  const [dataEmpl, setDataEmpl] = useState([]);
  const [loading, setLoading] = useState(false);

  const [listEmployee, setListEmployee] = useState([]);

  const [currentListEmployee, setCurrentListEmployee] = useState([]);

  const [listRole, setListRole] = useState([]);

  const fectAPIGetEmployee = () => {
    getEmployeeAPI().then(({ data }) => {
      setCurrentListEmployee(data);
      setListEmployee(data);
      setDataEmpl(data);
    });
  };

  const fectAllRole = () => {
    getListRoleAPI().then(({ data }) => {
      setListRole(data);
    });
  };

  useEffect(() => {
    fectAllRole();
    fectAPIGetEmployee();
  }, []);

  const onChangeSearch = (e) => {
    var condition = e.target.value;
    var empFilter = [...listEmployee];

    if (condition) {
      setListEmployee(
        empFilter.filter(
          (x) => x?.email?.includes(condition) || x?.name?.includes(condition),
        ),
      );
    } else {
      setListEmployee(currentListEmployee);
    }
  };

  const onUpdateRoleFindId = (empId, roleValue) => {
    var data = { id: empId, roles: roleValue };
    setLoading(true);
    updateRoleAPI(data)
      .then((x) => {})
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <Container>
      <Row className="page">
        <Col xs={24} xl={22}>
          <Spin spinning={loading}>
            <TableUpdateRole
              dataEmpl={dataEmpl}
              onChangeSearch={(e) => onChangeSearch(e)}
              listEmployee={listEmployee}
              listRole={listRole}
              onUpdateRoleFindId={(id, e) => onUpdateRoleFindId(id, e)}
            />
          </Spin>
        </Col>
      </Row>
    </Container>
  );
};

export default UpdateListRole;
