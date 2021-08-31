import React from "react";

export const ImageCustom = (props) => {
  const { width, height, path } = props;
  return (
    <>
      <img
        alt="logo"
        src={process.env.PUBLIC_URL + path}
        width={width}
        height={height}
      />
    </>
  );
};
