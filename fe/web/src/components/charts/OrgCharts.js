import React, { useEffect,useState } from "react";
import OrganizationChart from "@dabeng/react-orgchart";

import MyNode from './nodeChart/CustomNode';
import "../../assets/components/orgsChart.css";

import { getDepartmentStructure } from "../../services/employee";


// recursive


const getNestedChildren= (models, parentId)=> {
  const nestedTreeStructure = [];
  const length = models.length;

  for (let i = 0; i < length; i++) { // for-loop for perf reasons, huge difference in ie11
      const model = models[i];

      if (model.parent === parentId) {
          const children = getNestedChildren(models, model.id);

          if (children.length > 0) {
              model.children = children;
          }

          nestedTreeStructure.push(model);
      }
  }

  return nestedTreeStructure;
}



const DefaultChart = () => {


  const [departmentStructure, setDepartmentStructure] = useState({});

  // call API
  const fetchDepartmentStructure = () => {
    getDepartmentStructure().then(({ data }) => {
      const ds = {
        id: "n1",
        name: "LogiGear",
        title: "LogiGear",
        manager: {id:1,name:'Minh Ngo',image:''}, 
        children: data    
        
      };
          setDepartmentStructure(ds);
      });
    };
  
    useEffect(() => {
      fetchDepartmentStructure();
      
    }, []);

 

  return (
    <OrganizationChart
     
      
      datasource={departmentStructure}
      chartClass="myChart"
      NodeTemplate={MyNode}
    />
  )
    ;
};

export default DefaultChart;
