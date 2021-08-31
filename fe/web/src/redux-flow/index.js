import { configureStore } from "@reduxjs/toolkit";
import authenticationReducer from "./authentication/authentication-slice";
import imageReducer from "./image/image-slice";
import searchReducer from "./search/search-slice";

const store = configureStore({
  reducer: {
    authentication: authenticationReducer,
    image: imageReducer,
    search: searchReducer,
  },
});

export default store;
