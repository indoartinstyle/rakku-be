import React, {Component} from 'react';
import getFullUrl from '../helper/HelperUtil'
import './Home.css'

class UpdateProduct extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      products: null,
    };
  }

  componentDidMount() {
    this.fetchProduct();
  }

  fetchProduct() {
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


  render() {
    if (!this.state.products) {
      return null;
    }
    return (
      <div>
        <div className="container">
          <ol>
            {
              this.state.products.map((product) => (
                <li>
              <span className="div2">
                <a
                  href={'/upload/' + product.id + '/' + product.itemModelName}> {'ID >> ' + product.id + ' || Name >> ' + product.itemModelName + ' || Stock >> ' + product.stock + ' || cost >> ' + product.stock}
                </a>
              </span>
                </li>
              ))
            }
          </ol>
        </div>
      </div>
    );
  }
}

export default UpdateProduct
