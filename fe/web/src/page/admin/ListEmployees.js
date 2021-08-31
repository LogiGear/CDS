import React, { useState, useEffect } from "react";
import { Row, Col } from "antd";
import "../../assets/pages/list-employee.css";
import { getEmployeeManagerAPI, getDepartmentManagerAPI, getEmployeeCDMAPI } from "../../services/manager";
import { downloadFiledAPI } from "services/file";
import TableEmployee from "../../components/employee/tableEmployee";
import Container from "components/UI/Container/Container";
import { Spin, Alert } from "antd";
import { useHistory } from "react-router-dom";
import { LIST_GENDER } from "constants/constants";
import { useSelector } from "react-redux";
import { ADMIN } from "constants/roleType";
import { contains } from "utils/commom";
import fileDownload from "js-file-download";

const ListEmployees = () => {
	const { user } = useSelector((state) => state.authentication);
	const [dataEmplOrigin, setDataEmplOrigin] = useState([]);
	const [dataEmpl, setDataEmpl] = useState([]);
	const [listDepartments, setListDepartments] = useState([]);
	const [listGender, setlistGender] = useState([]);
	const history = useHistory();
	const [loading, setLoading] = useState(false);
	const [isButtonDownDisabled, setIsButtonDownDisabled] = useState(false)
	const [messageError, setMessageError] = useState(null)


	const handleDownloadFile = (id,fullName) => {
		setIsButtonDownDisabled(true);
		downloadFiledAPI("Resume-"+id+"-"+fullName+".doc").then((res) => {
			fileDownload(res.data, "Resume"+"-"+id+"-"+fullName+".doc");
		}).finally(
		  setTimeout(() => {
			setIsButtonDownDisabled(false)
		  }, 6000),
		).catch((err) => {
			setMessageError(err.response.status)
		  });
	  };
	const fectAPIGetEmployee = () => {
		var api = null;
		var listRole = user.roles?.map((x) => x.name);
		if (contains([ADMIN], listRole)) {
			api = getEmployeeManagerAPI();
		} else {
			api = getEmployeeCDMAPI(user?.id);
		}
		setLoading(true);
		api.then(({ data }) => {
			setDataEmplOrigin(data);
			setDataEmpl(data);
		}).finally((x) => {
			setLoading(false);
		});
	};

	const fectAPIGetDepartments = () => {
		getDepartmentManagerAPI()
			.then(({ data }) => {
				setListDepartments(data);
			})
			.finally((x) => {
				setLoading(false);
			});

		setlistGender(LIST_GENDER);
	};

	useEffect(() => {
		fectAPIGetDepartments();
		fectAPIGetEmployee();
	}, []);

	const filterSuccess = (values) => {
		var dataFilter = dataEmplOrigin;
		if (values?.departments)
			if (values?.departments?.length > 0) {
				setDataEmpl((dataFilter = dataFilter.filter((x) => values.departments.includes(x?.department?.id))));
			}
		if (values?.textSearch !== "") {
			setDataEmpl(
				(dataFilter = dataFilter.filter(
					(x) =>
						x?.fullName.toLowerCase().includes(values.textSearch.toLowerCase()) ||
						x?.user?.email.toLowerCase().includes(values.textSearch.toLowerCase())
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

	const onNavigation = (id) => {
		history.push(`/admin/employee/update/${id}`);
	};

	return (
		<Container style={{ marginTop: 30 }}>
			<Row className="page">
			{ (messageError==='500') &&
					<Alert
          				message="This user don't have resume"
          				type="warning"
        			/>}
				<Col xs={24} xl={24}>
					<Spin spinning={loading}>
						<TableEmployee
							dataEmpl={dataEmpl}
							onNavigation={(id) => onNavigation(id)}
							listDepartments={listDepartments}
							listGender={listGender}
							filterSuccess={(values) => filterSuccess(values)}
							handleDownloadFile={(id,fullName) => handleDownloadFile(id,fullName)}
							isButtonDownDisabled={isButtonDownDisabled}
							messageError={messageError}
						/>
					</Spin>
					
				</Col>
			</Row>
		</Container>
	);
};

export default ListEmployees;
