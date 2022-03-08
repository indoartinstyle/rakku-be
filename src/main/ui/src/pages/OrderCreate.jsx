import React, {Component} from 'react';
import axios from "axios";
import './OrderCreate.css'
import getFullUrl from "../helper/HelperUtil";
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
      .then((res) => {
        if (res.status === 200) {
          return res.json();
        }
        if (res.status === 401) {
          window.alert('Please Register yourself');
        }
      })
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
      this.setState({error: res.message})
    })
  };

  render() {
    if (!this.state.products) {
      return <div className="bg-danger"> No Stock Available</div>;
    }
    let optionItems = this.state.products.map((product, index) => <option key={product.id} title={product.id}
                                                                          value={index}>{product.itemModelName}</option>);

    return (
      <div>
        <div>
          <Jumbotron title="Take Order Portal"/>
        </div>
        <div className="form-center row">
          <div className="col-sm-4" style={{display: 'inline-block', border: '1px solid black'}}>
            <table className="col-sm-4">
              <thead>
              <tr>
                <th>
                  Item
                </th>
                <th>
                  Actions
                </th>
              </tr>
              </thead>
              <tbody>
              {this.renderRows()}
              </tbody>
            </table>
            <hr/>
          </div>
          <div className="col-sm-4" style={{display: 'inline-block', border: '1px solid black'}}>
            <h5>Order Form</h5>
            <hr/>
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
                this.setState({itemModelName: this.state.products[event.target.value].itemModelName});
                this.setState({itemID: this.state.products[event.target.value].id});
                this.setState({itemColor: this.state.products[event.target.value].itemColor});
                this.setState({itemSize: this.state.products[event.target.value].itemSize});
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
                disabled={true}
                type="text"
                value={this.state.itemColor}
                onChange={event => this.setState({itemColor: event.target.value})}
                placeholder="Enter Color"
              />
              <p></p>
              <label>Item Size: </label>
              <input
                disabled={true}
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
              <center>
                <button style={{margin: '2px'}} className="btn-warning"
                        onClick={(event) => this.handleAddItem(event)}> Add More Item
                </button>
                <button style={{margin: '2px'}} className="btn-info">Create Order</button>
                <button style={{margin: '2px'}} className="btn-danger" onClick={this.handleClear}> Clear</button>
              </center>
            </form>
          </div>
          <div className="col-sm-4" style={{display: 'inline-block', border: '1px solid black'}}>
            <h5>Order Status</h5>
            <hr/>
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
    );
  }

  handleItemDeleted(i) {
    var items = this.state.items;

    items.splice(i, 1);

    this.setState({
      items: items
    });
  }

  handleItemChanged(i, event) {
    var items = this.state.items;
    items[i] = event.target.value;

    this.setState({
      items: items
    });
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
      items: [],
    });
  }

  renderRows() {
    var context = this;

    return this.state.items.map(function (o, i) {
      return (
        <tr key={"item-" + i}>
          <td>
            <textarea
              disabled={true}
              rows="3"
              cols="35"
              value={i + 1 + '. ' + o.itemModelName + ' Rs. ' + o.itemMRP + '/- Per Unit, Total Qty: ' + o.itemQty}
              onChange={context.handleItemChanged.bind(context, i)}
            />
          </td>
          <td>
            <button
              onClick={context.handleItemDeleted.bind(context, i)}
            >
              Delete
            </button>
          </td>
        </tr>
      );
    });
  }
}

export default OrderCreate
