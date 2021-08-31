import React from "react";
import PropTypes from "prop-types";
import './customNode.css'
import {  useHistory } from "react-router-dom";



import { Popover } from 'antd';

const propTypes = {
    nodeData: PropTypes.object.isRequired
};



const MyNode = ({ nodeData }) => {
	const history = useHistory();


    const content = (

        <div>
            <img src={nodeData?.manager?.imageEncode} width={50} height={50} />

            <p>Employee Id: {nodeData?.manager?.employeeId}</p>
            <p>Name: {nodeData?.manager?.fullName}</p>
            <p>Gender: {nodeData?.manager?.gender}</p>
            <p>Degree: {nodeData?.manager?.degree}</p>
           
        </div>
    );

    const redirectEmployee = `/department/employee/${nodeData?.id}`;

    const selectNode = () => {
        console.log("NodeData", nodeData)
        { nodeData?.id !== "n1" && history.push(redirectEmployee) }

    };
    // styled base on Role

    return (
        <div className="node">
           

            {((nodeData?.id !== "n1") && nodeData.manager != null) ? (
                <>
                 <div className="node__department" onClick={selectNode}>{nodeData?.name}</div>

                <Popover placement='right' content={content} title="INFORMATION">
                    <div
                        className="node__manager"
                    //className={customStyle(nodeData)}
                    >

                        <div className="node__manager--avatar">
                            {nodeData?.manager?.imageEncode ?
                                <img
                                    style={{ 'borderRadius': '15px' }}
                                    width={30}
                                    height={30}
                                    src={nodeData?.manager?.imageEncode}
                                /> : ''}

                        </div>
                        <div className="node__manager--name">
                            {nodeData?.manager?.fullName}
                        </div>


                    </div>
                    </Popover>
                    </>
            ) : (
               
                <div className="node__departmentFull" onClick={selectNode}>{nodeData?.name}</div>

                )}



        </div>

    );
};

MyNode.propTypes = propTypes;

export default MyNode;