import React, {Component} from 'react';
import axios from "axios";
import './OrderCreate.css'
import getFullUrl from "../helper/HelperUtil";
import Navbar from "../components/Navbar";
import Jumbotron from "../components/Jumbotron";
import Marquee from "react-fast-marquee";

class AddProduct extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      itemModelName: null,
      itemColor: null,
      itemSize: null,
      cost: null,
      stock: null,
    }
  }

  handleSubmit = async (event) => {
    event.preventDefault();
    let body = {
      itemModelName: this.state.itemModelName,
      itemColor: this.state.itemColor,
      itemSize: this.state.itemSize,
      cost: this.state.cost,
      stock: this.state.stock,
    };
    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;
    axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
    axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
    axios({
      method: "post",
      url: getFullUrl("/api/product"),
      data: body,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': AUTH_TOKEN,
      },
    }).then(response => {
      this.setState({success: response})
      window.alert('Product Added : ' + response.status);
      this.clearAll();
    }).catch(res => {
      this.setState({error: res.message})
      window.alert('Failed  : ' + response.status);
    })
  };

  clearAll = () => {
    this.setState({itemModelName: ''})
    this.setState({itemColor: ''})
    this.setState({itemSize: ''})
    this.setState({cost: ''})
    this.setState({stock: ''})
  }

  render() {
    let info = null;
    info = (
      <div className="row">
        <Marquee style={{color: 'red'}}>Enter Product Details Carefully</Marquee>
      </div>
    );
    return (
      <div>
        <div>
          <Navbar/>
          <Jumbotron title="Take Order Portal"/>
        </div>
        {info}
        <div className="form-center row">
          <div className="col-sm-4">
          </div>
          <div className="col-sm-4">
            <form onSubmit={this.handleSubmit} className="form-center">
              <span className="formtext"></span>
              <label>Model: </label>
              <input
                type="text"
                value={this.state.itemModelName}
                onChange={event => {
                  this.setState({itemModelName: event.target.value});
                }}
                placeholder="Enter Model Name."
                required
              />
              <p></p>
              <label>Color: </label>
              <input
                type="text"
                value={this.state.itemColor}
                onChange={event => this.setState({itemColor: event.target.value})}
                placeholder="Enter Color"
                required
              />
              <p></p>
              <label>Size: </label>
              <input
                rows="5"
                cols="20"
                value={this.state.itemSize}
                onChange={event => this.setState({itemSize: event.target.value})}
                placeholder="Enter Size"
                required
              />
              <p></p>
              <label>Cost: </label>
              <input
                type="text"
                value={this.state.cost}
                onChange={event => this.setState({cost: event.target.value})}
                placeholder="Cost"
                required
              />
              <p></p>
              <label>Stock: </label>
              <input
                type="text"
                value={this.state.stock}
                onChange={event => this.setState({stock: event.target.value})}
                placeholder="Enter StockD"
              />
              <center>
                <button style={{margin: '2px'}} className="btn-info">Add</button>
              </center>
            </form>
          </div>
          <div className="col-sm-4">
          </div>
        </div>
      </div>
    );
  }
}

export default AddProduct
