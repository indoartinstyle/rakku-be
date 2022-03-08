import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import './Navbar.css';
import {black} from "material-ui/styles/colors";
import getFullUrl from "../helper/HelperUtil";

class Navbar extends Component {

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
      <nav className="navbar navbar-toggleable-md">
        <button className="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
                data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false"
                aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="container">
          <div className="collapse navbar-collapse" id="navbarCollapse">
            <ul className="navbar-nav ml-auto" style={{color: black}}>
              <li className="nav-item active">
                <Link className="nav-link" to="/">Home <span className="sr-only">(current)</span></Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/about">About</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/contact">Contact</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/register">Register</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/ordercreate">Create Order</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/updateorder">Update Dispatch</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/print">Print Non Dispatch Order</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/addproduct">Stock Entry</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/sales">Sale Report</Link>
              </li>
            </ul>
          </div>
        </div>
        <span className="text-black-50" style={{'margin-right': '5px'}}>{userName}</span>
      </nav>
    );
  }
}

export default Navbar;
