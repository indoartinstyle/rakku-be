const {Component} = require("react");
const React = require("react");

class Receipt extends Component {

  render() {
    if (!this.props.items) {
      return (<span></span>);
    }
    return (
      <div>
        <b>Order ID: </b> {this.props.orderID} <br></br>
        <b>Name: </b> {this.props.name} <br></br>
        <b>Contact Number: </b> {this.props.number} <p></p>
        <table>
          <tr>
            <th>Item</th>
            <th>Color</th>
            <th>Size</th>
            <th>Price/Unit</th>
            <th>Qty</th>
          </tr>
          {
            this.props.items.map((item) => (
              <tr>
                <td> {item.itemModelName} </td>
                <td>{item.itemColor}  </td>
                <td>{item.itemSize}  </td>
                <td>{item.itemMRP}  </td>
                <td>{item.itemQty} </td>
              </tr>
            ))
          }
          <tr>
            <td>--</td>
            <td>--</td>
            <td>--</td>
            <td>--</td>
            <td>--</td>
          </tr>
          <tr>
            <td>Total</td>
            <td>{this.props.total}</td>
          </tr>
        </table>
      </div>
    );
  }
}

export default Receipt
