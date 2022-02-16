import React, {Component} from 'react';
import getFullUrl from '../helper/HelperUtil'
import './Home.css'
import Navbar from "../components/Navbar";
import Jumbotron from "../components/Jumbotron";
import axios from "axios";

class DispatchUpdate extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      datas: [],
      DataisLoaded: false,
      track: {},
      updatedData: {
        orders: []
      },
    };
  }

  componentDidMount() {
    this.loadInitialData();
  }

  loadInitialData = () => {
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

  addToUpdate = (enrichedOrder, index) => {
    let order = {
      id: enrichedOrder.orderID,
      itemCourierPartner: enrichedOrder.cname,
      itemCourierTrackID: enrichedOrder.tid,
    }

    let error = false;
    Object.values(order).forEach(val => {
      if (!val) {
        error = true;
      }
    });
    if (error) {
      window.alert('Enter Properly');
      return;
    }

    let trackObj = this.state.track
    trackObj[index] = order;
    this.setState({track: trackObj})
    console.log('Track: ', this.state.track);
  }
  updateAll = () => {
    let body = {
      orders: Object.values(this.state.track)
    }

    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;
    axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
    axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
    axios({
      method: "post",
      url: getFullUrl("/api/order/update"),
      data: body,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': AUTH_TOKEN,
      },
    }).then(response => {
      this.setState({success: response})
      this.loadInitialData();
    }).catch(res => {
      this.setState({error: res.message})
    })

  }

  render() {
    const {DataisLoaded, datas} = this.state;
    if (!DataisLoaded) return <div>
      <h1> Pleses wait some time.... </h1></div>;
    return (
      <div>
        <div className="row">
          <Navbar/>
          <Jumbotron title="Take Order Portal"/>
        </div>
        <div className="overflow-scroll container" style={{' max-height': '100%'}}>
          <div className="card">
            {
              datas.map((order, index) => (
                <div className="card-body">
                  {index + 1}
                  <hr/>
                  <b>Name:</b> {order.customerName},<br></br>
                  <b>Address:</b> {order.customerAddress} <br></br>
                  <b>Phone Number:</b> {order.customerNumber}<br></br>
                  <b>Order ID:</b> {order.orderID}<br></br>
                  <input
                    key={index}
                    type="text"
                    // value={order.cname}
                    onChange={event => {
                      order.cname = event.target.value;
                    }}
                    placeholder="Enter Courier Partner"
                    required
                  />
                  <input
                    type="text"
                    key={index}
                    // value={order.tid}
                    placeholder="Enter Tracking ID"
                    onChange={event => {
                      order.tid = event.target.value;
                    }}
                    required
                  />
                  <button key={index} style={{margin: '2px'}} className="btn-warning"
                          onClick={(event) => {
                            this.addToUpdate(order, index)
                          }}> Attach
                  </button>
                  <hr/>
                </div>
              ))
            }
          </div>
          <hr/>
          <center>
            <button className="btn btn-info align-items-center" onClick={(event) => {
              this.updateAll()
            }}> Update All
            </button>
          </center>
        </div>
      </div>
    );
  }
}


export default DispatchUpdate
