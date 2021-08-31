import Avatar from "react-avatar-edit";
import React, { useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { imageActions } from "../../redux-flow/image/image-slice";
import {
    getEmployeeImageByIdAPI,
  } from "../../services/employee";
import CropAvatar from "./CropAvatar"

const GridAvatar = ({ idEmployee }) => {
    const [imageBase64, setImageBase64] = useState(null);
    const dispatch = useDispatch();
    const fetchAPIGetEmployeeImage = () => {
        getEmployeeImageByIdAPI(idEmployee)
          .then((response) => {
            setImageBase64(response.data);
            //save store
            dispatch(imageActions.fetchingAvatar(response.data));
          })
          .catch(function (error) {
            console.log(error);
          });
      };

      useEffect(() => {
        fetchAPIGetEmployeeImage()
      }, [])
      return (
        <CropAvatar imagePath={imageBase64} />
      )
}
export default GridAvatar;