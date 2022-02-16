import React, {Component} from 'react';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import './App.css';
import Home from './pages/Home.jsx';
import About from './pages/About.jsx';
import Contact from './pages/Contact.jsx';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import SignUp from "./components/signup/SignUp";
import OrderCreate from "./pages/OrderCreate";
import PrintAddress from "./pages/PrintDispatchable";
import DispatchUpdate from "./pages/DispatchUpdate";

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Route exact path="/" component={Home}/>
          <Route path="/about" component={About}/>
          <Route path="/contact" component={Contact}/>
          <Route path="/ordercreate" component={OrderCreate}/>
          <Route path="/print" component={PrintAddress}/>
          <Route path="/register" component={SignUp}/>
          <Route path= "/updateorder" component={DispatchUpdate}/>
        </div>
      </Router>
    );
  }
}

export default App;
