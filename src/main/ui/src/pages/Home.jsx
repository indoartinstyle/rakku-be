import React, {Component} from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx'
import getFullUrl from "../helper/HelperUtil";

class Home extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      loggedInUser: null
    };
  }

  componentDidMount() {
    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;
    fetch(getFullUrl("/api/loggedinuser"), {
      method: 'get',
      headers: new Headers({
        'Authorization': AUTH_TOKEN,
        'Content-Type': 'application/json;charset=UTF-8',
        'Access-Control-Allow-Origin': '*'
      })
    })
      .then((res) => res.json())
      .then((json) => {
        this.setState({loggedInUser: json})
      })
  }

  render() {
    let userName = this.state.loggedInUser && this.state.loggedInUser.firstName ? this.state.loggedInUser.firstName : 'Guest';
    return (
      <div>
        <Navbar/>
        <Jumbotron title={userName}/>
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
