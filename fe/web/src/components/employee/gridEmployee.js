import {  Card, Col, Pagination, Row, Space } from "antd";
import useBreakpoint from "antd/lib/grid/hooks/useBreakpoint";
import Text from "antd/lib/typography/Text";
import React, { useCallback, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import GridAvatar from "../../components/avatar/gridAvatar";
import "../../assets/components/gridEmployee.css";

const GridEmployee = ({ employeeData }) => {
  const breakPoint = useBreakpoint();
  const [pageSize, setPageSize] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSizeOptions, setPageSizeOptions] = useState(null);
  const [shownEmployees, setShownEmployees] = useState(null);
  const { t } = useTranslation();
  const handleOnChange = (page, pageSize) => {
    setPageSize(pageSize);
    setCurrentPage(page);
  };

  //Function to set pageSize/pageOptions/currentPage
  const handleBreakPointChange = useCallback(
    (currentSize, defaultSize, sizeOptions) => {
      let size = currentSize;
      if (currentSize !== defaultSize && !sizeOptions.includes(currentSize)) {
        size = defaultSize;
      }
      setPageSize(size);
      setPageSizeOptions(sizeOptions);
      setCurrentPage(1);
    },
    [],
  );

  //when breakpoint changed, apply handleBreakPointChange function
  useEffect(() => {
    if (!!breakPoint) {
      if (breakPoint.xl) {
        return handleBreakPointChange(pageSize, 8, [4, 8, 12]);
      }
      if (breakPoint.lg) {
        return handleBreakPointChange(pageSize, 6, [3, 6, 9]);
      }
      if (breakPoint.xs || breakPoint.md) {
        return handleBreakPointChange(pageSize, 4, [2, 4, 8]);
      }
    }
  }, [breakPoint, handleBreakPointChange, pageSize]);

  //Set employee lists to shows when pageSize/currentPage/employeeData is changed
  useEffect(() => {
    if (pageSize && currentPage) {
      setShownEmployees(
        employeeData.slice(
          (currentPage - 1) * pageSize,
          currentPage * pageSize,
        ),
      );
    }
  }, [employeeData, pageSize, currentPage]);

  return !!shownEmployees ? (
    <>
      <Row gutter={[16, 16]} style={{ padding: "20px 0" }}>
        {shownEmployees.map((employee) => {
          const link = "/admin/employee/update/" + employee?.id;
          return (
            <Col key={employee?.id} xl={6} lg={8} md={12} sm={24} xs={24}>
              <Card

                style={{ height: "100%" }}
                className="cardEmployee">
                <Link to={link}>
                  <Space
                    direction="vertical"
                    style={{
                      width: "100%",
                      height: "100%",
                      justifyContent: "space-between",
                    }}
                  >
                    <Row justify="start" dir="vertical">
                      <GridAvatar idEmployee={employee?.id} />
                    </Row>

                    <Text><strong>{employee?.fullName}</strong></Text>
                    <Text>{employee?.user?.email}</Text>
                    <Text>
                      <strong>{t("Title")}:</strong> {employee?.jobTitle}
                    </Text>
                    <Text>
                      <strong>{t("Major")}:</strong> {employee?.major}
                    </Text>
                    <Text>
                      <strong>{t("Department")}:</strong> {employee?.department?.name}
                    </Text>
                    <Text>
                      <strong>{t("Gender")}:</strong> {employee?.gender}
                    </Text>



                  </Space>
                </Link>
              </Card>
            </Col>
          )
        })}
      </Row>
      <Row justify="end">
        <Pagination
          showSizeChanger
          pageSizeOptions={pageSizeOptions}
          total={employeeData.length}
          showTotal={(total, range) =>
            `${range[0]}-${range[1] || 0} of ${total} items`
          }
          defaultPageSize={pageSize}
          pageSize={pageSize}
          defaultCurrent={currentPage}
          current={currentPage}
          onChange={handleOnChange}
        />
      </Row>
    </>
  ) : null;
};

export default GridEmployee;
