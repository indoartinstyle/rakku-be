import React, { Component } from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx';

class About extends Component {
  render() {
    return (
      <div>
        <Navbar />
        <Jumbotron title="Contact" subtitle="Please contact me at Sanjay"/>
        <div className="container">
          <h2>Contact</h2>

        </div>
        <Footer />
      </div>
    );
  }
}

export default About
