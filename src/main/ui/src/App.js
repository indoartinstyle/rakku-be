import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import './App.css';
import Home from './pages/Home.jsx';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import SignUp from "./components/signup/SignUp";
import OrderCreate from "./pages/OrderCreate";
import PrintAddress from "./pages/PrintDispatchable";
import DispatchUpdate from "./pages/DispatchUpdate";
import AddProduct from "./pages/AddProduct";
import Navbar from "./components/Navbar";
import SalesReport from "./pages/SalesReport";

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Navbar/>
          <Switch>
            <Route exact path="/" component={Home}/>
            <Route path="/ordercreate" component={OrderCreate}/>
            <Route path="/print" component={PrintAddress}/>
            <Route path="/register" component={SignUp}/>
            <Route path="/updateorder" component={DispatchUpdate}/>
            <Route path="/addproduct" component={AddProduct}/>
            <Route path="/sales" component={SalesReport}/>
          </Switch>
        </div>
      </Router>
    );
  }
}

export default App;
