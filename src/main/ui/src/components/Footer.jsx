import React, { Component } from 'react';
import './Footer.css';

class Footer extends Component {
  render() {
    return (
      <footer className="footer">
        <div className="blockquote-footer">
          <span className="text-muted">My Website &#169; {new Date().getFullYear()}</span>
        </div>
      </footer>
    );
  }
}

export default Footer;
