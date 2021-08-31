import React, { useState } from 'react'
import { useTranslation } from "react-i18next";

import { Upload, message,Button } from 'antd';
import { InboxOutlined } from '@ant-design/icons';

const { Dragger } = Upload;


export default function DragDropImage() {


    const { t } = useTranslation();
    const [image, setImage] = useState(null);
    const [file, setFile] = useState(null);


    const handleBeforeUpload = (file, fileList) => {
        console.log('Before Upload', file)
        new Promise((resolve, reject) => {
            const reader = new FileReader();
            setFile(file);
            reader.readAsDataURL(file);
            reader.addEventListener("load", ({ target }) => resolve(target.result));
            reader.addEventListener("error", () =>
                reject(t("something went wrong while reading file")),
            );
        }).then((dataUrl) => {
            console.log("Base64");
             setImage(dataUrl);
        }).catch((err) => console.error(err));

        return false;
    };
    const handleUploadFile = () => {
        const formData = new FormData();
        formData.append("file", file);
    //     uploadFileAPI(formData)
    //       .then((x) => {
    //         // setMessage(x.data);
    //         // setFileNameRes("26fabe8256d94986a754ba6d34645d04.doc");
    //       })
    //       .catch((err) => console.log("Error Upload",err))
    //       .finally(
    //         setTimeout(() => {
    //           setMessage(null);
    //         }, 3000),
    //       );
       };


    const props = {
        name: 'file',
        multiple: false,
        maxCount: 1,
        accept: ".doc,.docx,.png,.jpg,.jpeg",
        action: 'http://localhost:5000/upload',
        onChange(info) {
            const { status } = info.file;
            if (status !== 'uploading') {
                console.log(info.file, info.fileList);
            }
            if (status === 'done') {
                message.success(`${info.file.name} file uploaded successfully.`);
            } else if (status === 'error') {
                message.error(`${info.file.name} file upload failed.`);
            }
        },
        onDrop(e) {
            console.log('Dropped files', e.dataTransfer.files);
        },
        beforeUpload: handleBeforeUpload
    };

    return (
        <div>
            <Dragger {...props}>
                <p className="ant-upload-drag-icon">
                    <InboxOutlined />
                </p>
                <p className="ant-upload-text">Click or drag image/file to this area to upload</p>
               
            </Dragger>

            <Button
            onClick={() => handleUploadFile()}
            className="main-button"
            disabled={!image}
          >
            {t("Upload")}
          </Button>
        </div>
    )
}

