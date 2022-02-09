import React, {Component} from 'react';
import getFullUrl from '../helper/HelperUtil'
import './Home.css'
import Receipt from "./Receipt";

class PrintAddress extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      datas: [],
      DataisLoaded: false
    };
  }

  componentDidMount() {
    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;

    fetch(getFullUrl("/api/order/nondispatched"), {
      method: 'get',
      headers: new Headers({
        'Authorization': AUTH_TOKEN,
        'Content-Type': 'application/json;charset=UTF-8',
        'Access-Control-Allow-Origin': '*'
      })
    })
      .then((res) => res.json())
      .then((json) => {
        this.setState({
          datas: json,
          DataisLoaded: true
        });
      })
  }


  render() {
    const {DataisLoaded, datas} = this.state;
    if (!DataisLoaded) return <div>
      <h1> Pleses wait some time.... </h1></div>;
    return (
      <div>
        <div className="container">
          <div className="App">
            {
              datas.map((order) => (
                <div className="div2">
                  <b>To,</b><br></br>
                  <b>Name:</b> {order.customerName},<br></br>
                  <b>Address:</b> {order.customerAddress} <br></br>
                  <b>Phone Number:</b> {order.customerNumber}<br></br>
                  <b>Order ID:</b> {order.orderID}<br></br>
                  <p></p>
                  <b>From,</b><br></br>
                  <b>Name:</b> {order.fromName}<br></br>
                  <b>Address:</b> {order.fromAddress}<br></br>
                  <b>Phone Number:</b> {order.fromNumber}<br></br>
                  --------------------------------------
                  <Receipt items={order.items}
                           orderID = {order.orderID}
                           name = {order.customerName}
                           number = {order.customerNumber}
                           total={order.itemTotalCost}/>
                  --------------------------------------
                </div>

              ))
            }
          </div>
        </div>
      </div>
    );
  }
}

export default PrintAddress
