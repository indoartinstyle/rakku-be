import React, {Component} from "react";
import Jumbotron from "../Jumbotron";
import getFullUrl from "../../helper/HelperUtil";
import {Redirect} from "react-router-dom";
import axios from "axios";

export default class SignUp extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      redirect: false,
      otpMsg: null,
      lastName: null,
      firstName: null,
      email: null,
      phoneNumber: null,
      otp: null
    };
  }

  handleSubmit = (event) => {
    event.preventDefault();
    let body = {
      firstName: this.state.firstName,
      lastName: this.state.lastName,
      email: this.state.email,
      phoneNumber: '91' + this.state.phoneNumber
    }
    axios({
      method: "post",
      url: getFullUrl("/open/users"),
      data: body,
      headers: {
        'totp': this.state.otp,
        'Content-Type': 'application/json;charset=UTF-8',
        'Access-Control-Allow-Origin': '*'
      },
    }).then((res) => {
      if (res.status === 201) {
        localStorage.setItem('token', res.headers.token);
        this.setState({redirect: true});
      } else {
        window.alert(' User registration failed! Status=' + res.status)
      }
    })
  }

  getOtp = (event) => {
    event.preventDefault();
    fetch(getFullUrl("/open/mobileverification?mobilenumber=91" + this.state.phoneNumber), {
      method: 'post',
      headers: new Headers({
        'Content-Type': 'application/json;charset=UTF-8',
        'Access-Control-Allow-Origin': '*'
      })
    }).then((res) => {
      this.setState({otpMsg: res.status === 200});
    })
  }

  render() {
    if (this.state.redirect) {
      return <Redirect to='/'/>;
    }
    return (
      <div>
        <Jumbotron title="Take Order Portal"/>
        <center>
          <div style={{display: 'inline-block', border: '1px solid black'}}>
            <form onSubmit={this.handleSubmit} className="form-center">
              <span className="formtext"></span>
              <label>First Name: </label>
              <input
                type="text"
                value={this.state.firstName}
                onChange={event => {
                  this.setState({firstName: event.target.value});
                }}
                placeholder="Enter First Name"
                required
              />
              <p></p>
              <label>Last Name: </label>
              <input
                type="text"
                value={this.state.lastName}
                onChange={event => this.setState({lastName: event.target.value})}
                placeholder="Enter Last Name"
                required
              />
              <p></p>
              <label>Email: </label>
              <input
                value={this.state.email}
                onChange={event => this.setState({email: event.target.value})}
                placeholder="Enter Email"
                required
              />
              <p></p>
              <label>Mobile number(10 Digit): </label>
              <input
                type="text"
                value={this.state.phoneNumber}
                onChange={event => this.setState({phoneNumber: event.target.value})}
                placeholder="Enter Social Media"
                required
              />
              <p></p>
              <label>OTP: </label>
              <input
                disabled={!this.state.otpMsg}
                type="text"
                value={this.state.otp}
                onChange={event => this.setState({otp: event.target.value})}
                placeholder={this.state.otpMsg ? 'OTP Sent, Enter now' : 'OTP'}
              />
              <center>
                <button style={{margin: '2px'}} className="btn-warning"
                        onClick={(event) => this.getOtp(event)}> Get OTP
                </button>
                <button style={{margin: '2px'}} className="btn-info">Register</button>
              </center>
            </form>
          </div>
        </center>
      </div>
    );
  }
}