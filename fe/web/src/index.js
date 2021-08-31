import React from "react";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import "./index.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";

//import 'bootstrap/dist/css/bootstrap.min.css';
// import configureStore from './redux/configureStore';
// import { Provider } from 'react-redux';
// const store = configureStore();

// const ReduxApp = () => (
//   <Provider store={store}>
//     {/* re-render twice (dev env - can remove StrictMode*/}
//     <React.StrictMode>
//       <App />
//     </React.StrictMode>
//   </Provider>
// );

ReactDOM.render(
	// <React.StrictMode>
		<App />,
	// </React.StrictMode>,
	document.getElementById("root")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
