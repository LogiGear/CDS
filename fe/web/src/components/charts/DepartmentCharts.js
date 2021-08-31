import React, { useState,useEffect } from 'react'
import { Tree, Switch } from 'antd';
import { Card, Col, Row, Space } from "antd";
import GridAvatar from "../avatar/gridAvatar";
import { getDepartmentStructure } from "../../services/employee";

const data =
    [
        {
            title: "Mowede",
            key: '0-0',
            // department
            department: [
                {
                    title: 'P&W',
                    key: '0-0-0',
                    // empl
                    employee: [
                        {
                            "id": 2,
                            "fullName": "Nguyen Phuc Thinh",
                            "gender": null,
                            "degree": null,
                            "major": null,
                            "image": null
                        },
                        {
                            "id": 3,
                            "fullName": "Nguyen Tuan Huy",
                            "gender": null,
                            "degree": null,
                            "major": null,
                            "image": null
                        },
                    ]

                },
                {
                    title: 'M&W',
                    key: '0-0-1',
                    // children
                    employee: [
                        {
                            "id": 5,
                            "fullName": "Nguyen Phuc Thinh2",
                            "gender": null,
                            "degree": null,
                            "major": null,
                            "image": null
                        },
                        {
                            "id": 6,
                            "fullName": "Nguyen Tuan Huy3",
                            "gender": null,
                            "degree": null,
                            "major": null,
                            "image": null
                        },
                    ]

                }
            ],




        }
    ]

   
const customCard = (employee) => {
    return (
        <>
            <Row gutter={[16, 16]} style={{ padding: "20px 0" }}>

                <Col key={employee?.id} xl={6} lg={8} md={12} sm={24} xs={24}>
                    <Card

                        style={{ height: "100%",  width: "100%"}}
                        className="cardEmployee">

                        <Space
                            direction="vertical"
                            style={{
                                width: "100%",
                                height: "100%",
                                justifyContent: "space-between",
                            }}
                        >
                            <Row justify="start" dir="vertical">
                                <GridAvatar
                                //idEmployee={employee?.id}
                                />
                            </Row>

                            <strong>{employee?.fullName}</strong>
                            {/* <Text>{employee?.user?.email}</Text>
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
                    </Text> */}



                        </Space>

                    </Card>
                </Col>


            </Row>
        </>
    )
}

const ConvertToTreeNode = (data) => {

    return data.map((item) => ({

        title: item?.title,
        key: item.key,

        children: item.department.map((de) => ({
            title: de?.title,
            key: de.key,
            children: de.employee.map((e) => ({
                title: customCard(e),
                key: e.id,
            }))


        }))

    }))

}




const DepartmentCharts = () => {


    const dataTree = ConvertToTreeNode(data);
    console.log("Data", dataTree);


    const [showLine, setShowLine] = useState(true);
    const [showIcon, setShowIcon] = useState(false);
    const [showLeafIcon, setShowLeafIcon] = useState(true);


    const [departmentStructure, setDepartmentStructure] = useState([]);

    // call API
    const fetchDepartmentStructure = () => {
        getDepartmentStructure().then(({ data }) => {
            setDepartmentStructure(data);
        });
      };
    
      useEffect(() => {
        fetchDepartmentStructure();
      }, []);

    const onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    const onSetLeafIcon = (checked) => {
        setShowLeafIcon(checked);
        setShowLine({
            showLeafIcon: checked,
        });
    };

    const onSetShowLine = (checked) => {
        setShowLine(
            checked
                ? {
                    showLeafIcon,
                }
                : false,
        );
    };

    return (
        <div>
            <div
                style={{
                    marginBottom: 16,
                }}
            >
                showLine: <Switch checked={!!showLine} onChange={onSetShowLine} />
                <br />
                <br />
                showIcon: <Switch checked={showIcon} onChange={setShowIcon} />
                <br />
                <br />
                showLeafIcon: <Switch checked={showLeafIcon} onChange={onSetLeafIcon} />
            </div>
            <Tree
                showLine={showLine}
                // showIcon={showIcon}
                defaultExpandedKeys={['0-0-0']}
                onSelect={onSelect}
                //treeData={treeData}
                treeData={dataTree}
            />
        </div>
    );

}

export default DepartmentCharts
