import React from "react";
import { Row, Col } from "antd";
import { ImageCustom } from "../../components/image";

const HeaderLayout = () => {

  return (
    <>
      <Row style={{ padding: "10px" }}>
        <Col xs={8} xl={10} />
        <Col xs={8} xl={4}>
          <ImageCustom path="/images/mowede-logo.svg" />
        </Col>
        <Col xs={8} xl={10} />
      </Row>
      <Row>
        <Col xs={24} xl={24}>
          <hr />
        </Col>
      </Row>
    </>
  );
};

export default HeaderLayout;
