import React from "react";
import FlatButton from "material-ui/FlatButton";
import RaisedButton from "material-ui/RaisedButton";
import TextField from "material-ui/TextField";
import PasswordStr from "./PasswordStr";
import "./style.css";

const SignUpForm = ({
                      history,
                      onSubmit,
                      onChange,
                      errors,
                      user,
                      score,
                      btnTxt,
                      type,
                      pwMask,
                      onPwChange
                    }) => {
  return (
    <div className="loginBox">
      <h1>Register</h1>
      {errors.message && <p style={{color: "red"}}>{errors.message}</p>}

      <form onSubmit={onSubmit}>
        <TextField
          name="username"
          floatingLabelText="name"
          value={user.username}
          onChange={onChange}
          errorText={errors.username}
        />
        <p></p>
        <TextField
          name="mobileno"
          floatingLabelText="mobile number"
          value={user.mobileno}
          onChange={onChange}
          errorText={errors.mobileno}
        />
        <RaisedButton
          className="signUpSubmit"
          primary={true}
          type="otp"
          label="Get OTP"
          onClick={void(0)}
        />
        <p></p>
        <TextField
          name="otp"
          floatingLabelText="Enter OTP"
          value={user.otp}
          onChange={onChange}
          errorText={errors.otp}
        />
        <br/>
        <RaisedButton
          className="signUpSubmit"
          primary={true}
          type="submit"
          label="submit"
        />
      </form>
      <p>
        Aleady have an account? <br/>
        <a href="/">Log in here</a>
      </p>
    </div>
  );
};

export default SignUpForm;
