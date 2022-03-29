import axios from 'axios';
import React, {Component} from 'react';

class UploadImage extends Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedFile: null
    }
  }
  onFileChange = event => {
    this.setState({selectedFile: event.target.files[0]});
  };

  onFileUpload = (productID) => {
    const formData = new FormData();

    formData.append(
      "file",
      this.state.selectedFile,
      this.state.selectedFile.name
    );
    console.log(this.state.selectedFile);

    let token = localStorage.getItem('token');
    let AUTH_TOKEN = 'Bearer ' + token;
    axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
    axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
    axios.post('/api/product/image/upload?FY=' + productID, formData, {
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': AUTH_TOKEN,
      },
    });
  };

  fileData = () => {
    if (this.state.selectedFile) {

      return (
        <div>
          <h2>File Details:</h2>
          <p>File Name: {this.state.selectedFile.name}</p>
          <p>File Type: {this.state.selectedFile.type}</p>
          <p>
            Last Modified:{" "}
            {this.state.selectedFile.lastModifiedDate.toDateString()}
          </p>
        </div>
      );
    } else {
      return (
        <div>
          <br/>
          <h4>Choose before Pressing the Upload button</h4>
        </div>
      );
    }
  };

  render() {
    return (
      <div>
        <div>
          <input type="file" onChange={this.onFileChange}/>
          <button onClick={ ()=> {
            this.onFileUpload(this.props.match.params.productID)
          }}>
            Upload!
          </button>
        </div>
        {this.fileData()}
      </div>
    );
  }
}

export default UploadImage;