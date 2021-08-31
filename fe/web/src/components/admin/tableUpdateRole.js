import React from "react";
import { Input, Table, Typography } from "antd";
import { useTranslation } from "react-i18next";
import "../../assets/pages/list-employee.css";
import SelectFiled from "../../components/ant-custom/selectFiled";

const TableUpdateRole = (props) => {
	const { onChangeSearch, listEmployee, listRole, onUpdateRoleFindId } = props;
	const { t } = useTranslation();
	const { Title } = Typography;

	const columns = [
		{
			title: "Id",
			dataIndex: "id",
			key: "id",
			width: "100px",
		},
		{
			title: "Email",
			dataIndex: "email",
			key: "email",
		},
		{
			title: "Name",
			dataIndex: "name",
			key: "name",
		},
		{
			title: "Roles",
			dataIndex: "roles",
			key: "roles",
			width: "400px",
			render(text, row) {
				return (
					<SelectFiled
						allowClear
						placeholder={t("Select role")}
						defaultValue={[...row.roles.map((x) => x.id)]}
						listOption={listRole}
						onChange={(e) => onUpdateRoleFindId(row.id, e)}
					/>
				);
			},
		},
	];

  return (
    <>
      <Title className="title" level={2}>
        {t("Update List Role")}
      </Title>
      <Input.Search
        allowClear
        className="search"
        onChange={(e) => onChangeSearch(e)}
        defaultValue=""
      />
      <Table dataSource={listEmployee} columns={columns} rowKey="id" />;
    </>
  );
};

export default TableUpdateRole;
