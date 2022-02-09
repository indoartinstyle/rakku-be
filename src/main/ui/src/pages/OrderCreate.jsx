import React, {Component} from 'react';
import axios from "axios";
import './OrderCreate.css'
import getFullUrl from "../helper/HelperUtil";
import Navbar from "../components/Navbar";
import Jumbotron from "../components/Jumbotron";

class OrderCreate extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      products: null,
      customerName: '',
      customerNumber: '',
      customerAddress: '',
      customerFromSocial: '',

      itemModelName: '',
      itemID: '',
      itemColor: '',
      itemSize: '',
      itemMRP: '',
      itemQty: '',

      items: [],
      error: null,
      success: null,
      submitButtonActive: true,
    };
  }

  componentDidMount() {
    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;
    fetch(getFullUrl("/api/product"), {
      method: 'get',
      headers: new Headers({
        'Authorization': AUTH_TOKEN,
        'Content-Type': 'application/json;charset=UTF-8',
        'Access-Control-Allow-Origin': '*'
      })
    })
      .then((res) => res.json())
      .then((json) => {
        this.setState({products: json})
      })
  }

  handleSubmit = async (event) => {
    event.preventDefault();

    let body = {
      customerName: this.state.customerName,
      customerNumber: this.state.customerNumber,
      customerAddress: this.state.customerAddress,
      customerFromSocial: this.state.customerFromSocial,
      items: this.state.items
    }
    if (this.state.items.length === 0) {
      window.alert("Enter One item at least");
      return;
    }
    if (!this.state.submitButtonActive) {
      window.alert("Clear first");
      return;
    }
    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;
    axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
    axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
    axios({
      method: "post",
      url: getFullUrl("/api/order"),
      data: body,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': AUTH_TOKEN,
      },
    }).then(response => {
      this.setState({success: response})
      this.setState({submitButtonActive: false})
    }).catch(res => {
      this.setState({error: res.toString})
    })
  };

  render() {
    if (!this.state.products) {
      return null;
    }
    let optionItems = this.state.products.map((product, index) => <option key={product.id} title={product.id} value={index}>{product.itemModelName}</option>);

    return (
      <div>
        <Navbar/>
        <Jumbotron title="Take Order Portal"/>
        <div className="container">
          <div className="form-center">
            <div style={{display: 'inline-block'}}>
              <form onSubmit={this.handleSubmit} className="form-center">
                <span className="formtext"></span>
                <label>Customer Name: </label>
                <input
                  type="text"
                  value={this.state.customerName}
                  onChange={event => {
                    this.setState({customerName: event.target.value});
                  }}
                  placeholder="Enter Customer Name"
                  required
                />
                <p></p>
                <label>Customer Number: </label>
                <input
                  type="text"
                  value={this.state.customerNumber}
                  onChange={event => this.setState({customerNumber: event.target.value})}
                  placeholder="Enter Customer Mobile Number"
                  required
                />
                <p></p>
                <label>Customer Address: </label>
                <textarea
                  rows="5"
                  cols="20"
                  value={this.state.customerAddress}
                  onChange={event => this.setState({customerAddress: event.target.value})}
                  placeholder="Enter Address"
                  required
                />
                <p></p>
                <label>Source: </label>
                <input
                  type="text"
                  value={this.state.customerFromSocial}
                  onChange={event => this.setState({customerFromSocial: event.target.value})}
                  placeholder="Enter Social Media"
                  required
                />
                <p></p>
                <label>Product Name: </label>
                <select onChange={event => {
                  console.log(event.target.value);
                  this.setState({itemModelName: this.state.products[event.target.value].itemModelName})
                  this.setState({itemID: this.state.products[event.target.value].id})
                }}>
                  <option selected disabled>Please choose...</option>
                  {optionItems}
                </select>
                <p></p>
                <label>Item ID: </label>
                <input
                  disabled={true}
                  type="text"
                  value={this.state.itemID}
                  //onChange={event => this.setState({itemID: event.target.value})}
                  placeholder="Enter item ID"
                />
                <p></p>
                <label>Item Color: </label>
                <input
                  type="text"
                  value={this.state.itemColor}
                  onChange={event => this.setState({itemColor: event.target.value})}
                  placeholder="Enter Color"
                />
                <p></p>
                <label>Item Size: </label>
                <input
                  type="text"
                  value={this.state.itemSize}
                  onChange={event => this.setState({itemSize: event.target.value})}
                  placeholder="Enter Size"
                />
                <p></p>
                <label>Item MRP: </label>
                <input
                  type="text"
                  value={this.state.itemMRP}
                  onChange={event => this.setState({itemMRP: event.target.value})}
                  placeholder="Enter MRP"
                />
                <p></p>
                <label>Item Quantity: </label>

                <input
                  type="text"
                  value={this.state.itemQty}
                  onChange={event => this.setState({itemQty: event.target.value})}
                  placeholder="Enter Qty"
                />
                <p></p>
                <button onClick={(event) => this.handleAddItem(event)}> Add More Item</button>
                <button>Go!</button>
                <button onClick={this.handleClear}> Clear</button>
              </form>
            </div>
            <div style={{display: 'inline-block'}}>
              {
                (this.state.error === null && this.state.success !== null) ? (
                  <div>
                    <h4 style={{color: 'Green'}}> Order Created</h4><p></p>
                    <h5>Order ID: {this.state.success.data.id}</h5>
                  </div>
                ) : ((this.state.error === null && this.state.success === null) ? (<span></span>) : (
                    <div>
                      <div>
                        <h4 style={{color: 'red'}}> Order Create Fail, Try Again</h4><p></p>
                        <h5>Error Code : {this.state.error}</h5>
                      </div>
                    </div>
                  )
                )
              }

            </div>
          </div>
        </div>
      </div>
    );
  }

  handleAddItem = async (event) => {
    event.preventDefault();
    let item = {
      itemModelName: this.state.itemModelName,
      itemID: this.state.itemID,
      itemColor: this.state.itemColor,
      itemSize: this.state.itemSize,
      itemMRP: this.state.itemMRP,
      itemQty: this.state.itemQty,
    }
    let ok = true;
    Object.keys(item).forEach(key => {
      if (!item[key]) {
        ok = false;
      }
    })
    if (!ok) {
      window.alert("Enter all item data properly");
      return;
    }
    this.setState({items: [...this.state.items, item]});
    this.setState({
      itemModelName: '',
      itemID: '',
      itemColor: '',
      itemSize: '',
      itemMRP: '',
      itemQty: ''
    });
  }


  handleClear = async (event) => {
    event.preventDefault();
    this.setState({
      customerName: '',
      customerNumber: '',
      customerAddress: '',
      customerFromSocial: '',
      itemModelName: '',
      itemID: '',
      itemColor: '',
      itemSize: '',
      itemMRP: '',
      itemQty: '',
      error: null,
      success: null,
      submitButtonActive: true,
    });
  }
}

export default OrderCreate
