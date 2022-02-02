import React, { Component } from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx';

class About extends Component {
  render() {
    return (
      <div>
        <Navbar />
        <Jumbotron title="About Me!" subtitle="This page is all about me and my work!"/>
        <div className="container">
          <h2>About</h2>

        </div>
        <Footer />
      </div>
    );
  }
}

export default About
