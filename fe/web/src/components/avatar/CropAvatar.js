import Avatar from "react-avatar-edit";
import React, { useState } from "react";
import Modal from "react-modal";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { Button } from "antd";

const customStyles = {
	content: {
		top: "50%",
		left: "50%",
		right: "auto",
		bottom: "auto",
		marginRight: "-50%",
		transform: "translate(-50%, -50%)",
	},
};

const CropAvatar = ({ parentCallback, imagePath }) => {
	const { t } = useTranslation();

	const [preview, setPreview] = useState(imagePath ? imagePath : "");
	const [file, setFile] = useState(null);
	const isEditableAvatar = useSelector((state) => state.image.isEditableAvatar);

	React.useEffect(() => {
		setPreview(imagePath);
	}, [imagePath]);

	// truyền state từ Child sang Parent
	// React.useEffect(() => {
	//     if (parentCallback) {
	//         parentCallback(preview);
	//     }
	// }, [preview])
	React.useEffect(() => {
		if (parentCallback) {
			parentCallback(file);
		}
	}, [file, parentCallback]);

	function onClose() {
		//setPreview(null);
	}
	function onCrop(pv) {
		setPreview(pv);
		fetch(pv) //base64
			.then((res) => res.blob())
			.then((blob) => {
				const file = new File([blob], "image.png", { type: "image/png" });
				setFile(file);
			});
	}

	function onBeforeFileLoad(elem) {
		if (elem.target.files[0].size > 2000000) {
			alert("File is too big!");
			elem.target.value = "";
		}
	}
	function onImageLoad(i) {
		//console.log("IMAGE", i)
	}

	const [modalIsOpen, setIsOpen] = React.useState(false);

	function openModal() {
		setIsOpen(true);
	}

	function closeModal() {
		setIsOpen(false);
	}

	const imageHandler = (e) => {
		e.preventDefault();
		let image_as_base64;
		if (e.target.files[0] != null) {
			image_as_base64 = URL.createObjectURL(e.target.files[0]);

			setPreview(image_as_base64);
			openModal();
		}
	};
	return (
		<div className="avatar">
			{/* <img src="https://drive.google.com/uc?export=view&id=1puvVTeQsH9zJUa0HMHXTnrLOmTwH1EwJ" width={300} height={200} /> */}

			<Modal
				isOpen={modalIsOpen}
				ariaHideApp={false}
				// onAfterOpen={afterOpenModal}
				onRequestClose={closeModal}
				style={customStyles}
				contentLabel="Example Modal"
			>
				<Avatar
					width={400}
					height={300}
					onCrop={onCrop}
					onClose={onClose}
					onBeforeFileLoad={onBeforeFileLoad}
					onImageLoad={onImageLoad}
					src={preview}
					exportAsSquare={true}
				>
					<img src={preview} alt="Preview" />
				</Avatar>

				<Button type="danger" onClick={closeModal}>{t("OK")}</Button>
			</Modal>
			<div>
				{preview && (
					<>
						<img
							src={preview}
							style={{
								height: "auto",
								width: 100,
								borderRadius: 50,
							}}
							alt="Preview"
						/>
						{/* <a href={preview} download="avatar">
                        Download image
                    </a> */}
					</>
				)}
				{/* <button onClick={openModal} value="open">Change Avatar</button> */}
				<div style={{position: "relative"}}>
					<input
						type="file"
						accept="image/*"
						name="image-upload"
						id="image-upload"
						style={{ display: "none" }}
						onChange={imageHandler}
					/>
					{isEditableAvatar ? (
						<label htmlFor="image-upload" className="avatar__edit-button">
							{t("Upload Image")}
						</label>
					) : null}
				</div>
			</div>
		</div>
	);
};
export default CropAvatar;
