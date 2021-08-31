import React from "react";
import Container from "../components/UI/Container/Container";
import "assets/pages/homePage.css";
import OrgCharts from './../components/charts/OrgCharts';


const HomePage = (props) => {

	return (
		<Container className="home-page">
			<h2 style={{ 'textAlign': 'center','fontWeight':'bold'}}>Organization Chart</h2>
			<OrgCharts />
		</Container>
	);
};

export default HomePage;
