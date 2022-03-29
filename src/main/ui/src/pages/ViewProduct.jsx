import React, {Component} from 'react';
import getFullUrl from '../helper/HelperUtil'
import './Home.css'
import SimpleImageSlider from "react-simple-image-slider";

class ViewProduct extends Component {

  // Constructor
  constructor(props) {
    super(props);
    this.state = {
      products: null,
      images: null,
    };
  }

  componentDidMount() {
    this.fetchProduct();
  }

  getImagesForSLider(product) {
    if (product && product.imgUrl && product.imgUrl.length > 0) {
      let imgs = [];
      product.imgUrl.forEach((url) => {
        imgs.push({
          url: url
        })
      });
      return imgs;
    }
    return null;
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
          {
            this.state.products.map((product) => (
              <div>
                {
                  (this.getImagesForSLider(product)) ? (
                    <SimpleImageSlider
                      width={300}
                      height={300}
                      images={this.getImagesForSLider(product)}
                      showBullets={true}
                      showNavs={true}
                    />
                  ) : null
                }
                <span>
                  {'ID >> ' + product.id + ' || Name >> ' + product.itemModelName + ' || Stock >> ' + product.stock + ' || cost >> ' + product.cost}
                </span>
                <hr></hr>
              </div>
            ))
          }
        </div>
      </div>
    );
  }

}

export default ViewProduct
