import React, { useState, useEffect, useMemo } from "react";
import { Row, Col, Button, Spin } from "antd";
import "../../assets/pages/list-employee.css";
import {getEmployeeByDepartment} from "../../services/employee"
import Container from "components/UI/Container/Container";
import { LIST_GENDER } from "constants/constants";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import SelectFiled from "components/ant-custom/selectFiled"
import GridEmployeeDepartment from "components/employee/gridEmployeeDepartment"
const EmployeeDepartment = () => {

    const { user } = useSelector((state) => state.authentication);
	const [dataEmplOrigin, setDataEmplOrigin] = useState([]);
	const [dataEmpl, setDataEmpl] = useState([]);
	const [listGender, setlistGender] = useState([]);
	const [loading, setLoading] = useState(false);
    const { t } = useTranslation();
    const params = useParams();
    const { textSearch } = useSelector((state) => state.search);
    console.log("Employee Department", dataEmpl)

    const [dataFilter, setDataFilter] = useState({
        departments: [],
        gender: [],
        textSearch: "",
      });
    const departmentId = useMemo(() => {
        return params?.idDepartment ? +params.idDepartment : user.id;
      }, [params, user.id]);
    console.log("deparment id",departmentId)

    useEffect(() =>{
        setlistGender(LIST_GENDER);
    }, [setlistGender])

    const onFilterGender = (gender) => {
        setDataFilter({ ...dataFilter, gender: gender });
      };
    useEffect(() => {
        console.log(textSearch);
        filterSuccess({ ...dataFilter, textSearch: textSearch });
      }, [textSearch, dataFilter ]);
    const fecthAPIGetEmployee = () => {
		setLoading(true);
		getEmployeeByDepartment(departmentId).then(({ data }) => {
			setDataEmplOrigin(data);
			setDataEmpl(data);
		}).finally((x) => {
			setLoading(false);
		});
	};
    useEffect(() => {
        fecthAPIGetEmployee();
    }, [])

    const filterSuccess = (values) => {
		var dataFilter = dataEmplOrigin;
		if (values?.textSearch !== "") {
			setDataEmpl(
				(dataFilter = dataFilter.filter(
					(x) =>
						x?.fullName.toLowerCase().includes(values.textSearch.toLowerCase())
				))
			);
		}

		if (values?.gender?.length > 0) {
			dataFilter = setDataEmpl(dataFilter.filter((x) => values.gender.includes(x?.gender)));
		}

		if (values?.gender.length === 0 && values?.departments.length === 0 && values?.textSearch === "") {
			setDataEmpl(dataEmplOrigin);
		}
	};

    return (
		<Container style={{ marginTop: 30 }}>
            <Row className="page">
				<Col xs={24} xl={24}>
					<Spin spinning={loading}>
                    <Row gutter={8} align="bottom">
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
                    </Row>
                    <GridEmployeeDepartment employeeData={dataEmpl} />
                    </Spin>
                </Col>
            </Row>
        </Container>
    )

}
export default EmployeeDepartment;