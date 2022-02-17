import React, {Component} from 'react';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx'
import getFullUrl from "../helper/HelperUtil";
import './Home.css';

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
            <center>
              <iframe
                title="From Facebook"
                id="facebook"
                src="https://www.facebook.com/plugins/post.php?href=https%3A%2F%2Fwww.facebook.com%2Fpermalink.php%3Fstory_fbid%3D115843897685337%26id%3D106889208580806&show_text=true&width=500"
                width="100%" height="600px"
                allowFullScreen="true"
                allow="autoplay; clipboard-write; encrypted-media; picture-in-picture; web-share">
              </iframe>
            </center>
          </div>
        </div>
      </div>
    );
  }
}

export default Home
