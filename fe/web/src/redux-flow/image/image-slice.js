import { createSlice } from "@reduxjs/toolkit";

const imageSlice = createSlice({
  name: "image",
  initialState: {
    image: "",
    isEditableAvatar: false,
  },
  reducers: {
    fetchingAvatar: (state, action) => {
      return {
        ...state,
        image: action.payload,
      };
    },
    isEditAvatar: (state, action) => {
      state.isEditableAvatar = action.payload;
    },
  },
});

export const imageActions = imageSlice.actions;
export default imageSlice.reducer;
