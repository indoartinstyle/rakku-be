import React, {Component} from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx'

class Home extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      items: [],
      DataisLoaded: false,
      user: {
        name: 'Guest'
      }
    };
  }


  render() {
    return (
      <div>
        <Navbar/>
        <Jumbotron title="User-1" subtitle="Put something witty here!"/>
        <div className="container">
          <h2>Welcome</h2>
          <div className="App">
            <h3>Indo Art In Style Operation Portal</h3>
          </div>
        </div>
        <Footer/>
      </div>
    );
  }
}

export default Home
