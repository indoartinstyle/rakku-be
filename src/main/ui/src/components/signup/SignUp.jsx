import React, {Component} from "react";
import {MuiThemeProvider} from "material-ui";
import SignUpContainer from "./SignUpContainer";

export default class SignUp extends Component {
  render() {
    return (
      <MuiThemeProvider>
        <SignUpContainer />
      </MuiThemeProvider>
    );
  }
}