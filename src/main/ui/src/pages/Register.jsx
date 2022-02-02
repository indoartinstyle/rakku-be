import React, { Component } from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx';

class Register extends Component {
  render() {
    return (
      <div>
        <Navbar />
        <div className="container">
          <h2>Form Will appear here</h2>
        </div>
        <Footer />
      </div>
    );
  }
}

export default Register
