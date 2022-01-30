import React, {Component} from 'react';
import Footer from '../components/Footer.jsx';
import Navbar from '../components/Navbar.jsx';
import Jumbotron from '../components/Jumbotron.jsx';

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
    fetch(
      "https://jsonplaceholder.typicode.com/users")
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
            {
              items.map((item) => (
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
