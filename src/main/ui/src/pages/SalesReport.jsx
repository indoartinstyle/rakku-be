import React, {Component} from 'react';
import getFullUrl from '../helper/HelperUtil'
import './Home.css'
import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine.css';

class SalesReport extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      sales: null,
      DataisLoaded: false,
      filterYear: '2021',
      filterMonth: '01',
      filterDate: '01',
    };
  }

  componentDidMount() {
    this.fetchSalesReport();
  }


  fetchSalesReport() {
    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;

    fetch(getFullUrl("/api/sales?year=" + this.state.filterYear + "&month=" + this.state.filterMonth + "&day=" + this.state.filterDate + "&isItemReq=true"), {
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

          sales: json,
          column: [
            {field: 'date', resizable: true},
            {field: 'orderID', resizable: true},
            {field: 'itemModelName', resizable: true},
            {field: 'itemMRP', resizable: true},
            {field: 'itemQty', resizable: true},
            {field: 'itemID', resizable: true},
            {field: 'itemColor', resizable: true},
            {field: 'itemSize', resizable: true},
            {field: 'courierPartner', resizable: true},
            {field: 'trackingNo', resizable: true},
          ],
          gridDefault: {
            defaultColDef: {
              filter: true,
            },
          },
          DataisLoaded: true
        });
      })
  }

  onGridReady(param) {
    param.api.sizeColumnsToFit();
  }

  render() {
    const {DataisLoaded} = this.state;
    if (!DataisLoaded) return <div>
      <h1> Pleses wait some time.... </h1></div>;
    return (
      <div>
        <center>
          <div className="card card-header">
            <div className="card-body">
              <h4>Total T/O : <font color="red">Rs. {this.state.sales.totalRevenue}/-</font></h4>
              <h4>Total Cost : <font color="red">Rs. {this.state.sales.totalCost}/-</font></h4>
              <h4>Total Items : <font color="red">{this.state.sales.noOfItem}</font></h4>
            </div>
          </div>
          <div className="ag-theme-alpine" style={{height: '700px', width: '80%'}}>
            <AgGridReact
              onGridReady={this.onGridReady.bind(this)}
              rowData={this.state.sales.allItems}
              defaultColDef={this.state.gridDefault.defaultColDef}
              columnDefs={this.state.column}>
            </AgGridReact>
          </div>
        </center>
      </div>
    );
  }
}

export default SalesReport;
