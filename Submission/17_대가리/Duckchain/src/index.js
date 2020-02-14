import React, { Component } from "react";
import ReactDOM from 'react-dom'
import { BrowserRouter } from "react-router-dom";


import Header from './components/header';
import HeaderS from './components/headerS';
import Footer from './components/footer';
import { Switch, Route } from "react-router-dom";
// import { Main, Auth, NotFound } from "pages";
import Main from "./components/main"
import Profile from "./components/profile"
import SmartContract from "./components/smartContract"
import 'semantic-ui-css/semantic.min.css';
import './App.css'
class App extends React.Component{
    render(){
        return (<>
        <BrowserRouter>
        {/* <HeaderS /> */}
        <Header />
            <Route path="/" exact={true}  component={Main} />
            <Switch>
            <Route path="/profile" exact={false} component={Profile} />
            <Route path="/smartContract"component={SmartContract} />
            </Switch>

        </BrowserRouter>
        {/* <Footer/> */}
        </>)
    }
}

ReactDOM.render(<App />, document.querySelector('#root'));