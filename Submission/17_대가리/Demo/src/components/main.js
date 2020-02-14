import React from "react";

import Board from './board';

import CircularProgress from '@material-ui/core/CircularProgress';

import SendTransaction from './sendTrasaction'
import { Container } from "@material-ui/core";

const style = {
    margin: '0 auto'
}
  

class Main extends React.Component {
   
    
    constructor(props){
        super(props);
        this.state={
          posts: [],
          completed:0
        }
      }
    stateRefresh = () =>{
      this.setState({
        posts: [],
        completed:0
      })
      this.callApi()
        .then(res => this.setState({posts: res}))
        .catch(err => console.log(err));

    }
    
    
      componentDidMount(){
        // this.timer = setInterval(this.progress,20);
        
        this.callApi()
        .then(res => this.setState({posts: res}))
        .catch(err => console.log(err));
        console.log(this.state.posts)
      }
    
      callApi= async() =>{  
        // const response = await fetch('http://106.10.58.158:3000/v1/addresses/0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C/transactions');
        const response = await fetch('http://api-ropsten.etherscan.io/api?module=account&action=txlist&address=0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C&startblock=0&endblock=99999999&sort=desc&apikey=HXQ6BH6EYJVXTA4F3KZV86961GZ4RVMR8K');
        
        const body = await response.json();
        console.log(body.result)
        return body.result;
      }
    
      progress = () =>{
        const {completed } = this.state;
        this.setState({completed: completed >=100? 0: completed +1});
    }
    
    render(){
        
        return (
            <div className = "ui comments" style = {style}>
              <Container>
            {this.state.posts.lenght !== 0 ? this.state.posts.map((c, idx) =>{return (<Board stateRefresh = {this.stateRefresh} key = {idx} content = {c}/>)
            }): <div> lonading </div>}
            
            </Container>
            <SendTransaction />
            </div>
          );
    }
    
};

export default Main;