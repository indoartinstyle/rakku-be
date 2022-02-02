import React, {Component} from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx'
import getFullUrl from '../helper/HelperUtil'

class Home extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      items: [],
      DataisLoaded: false
    };
  }

  componentDidMount() {
    fetch(getFullUrl("/open/users"), {
      method: 'get',
      headers: new Headers({
        'Authorization': 'Basic ' + btoa('username:password'),
        'Content-Type': 'application/json;charset=UTF-8'
      })
    })
      .then((res) => res.json())
      .then((json) => {
        this.setState({
          items: json,
          DataisLoaded: true
        });
      })
  }


  render() {
    const {DataisLoaded, items} = this.state;
    if (!DataisLoaded) return <div>
      <h1> Pleses wait some time.... </h1></div>;
    return (
      <div>
        <Navbar/>
        <Jumbotron title="Welcome" subtitle="Put something witty here!"/>
        <div className="container">
          <h2>Welcome</h2>
          <div className="App">
            <h3>Found {items.count} users</h3>
            {
              items.users.map((item) => (
                <ol key={item.id}>
                  User_Name: {item.username},
                  Full_Name: {item.name},
                  User_Email: {item.email}
                </ol>
              ))
            }
          </div>
        </div>
        <Footer/>
      </div>
    );
  }
}

export default Home
