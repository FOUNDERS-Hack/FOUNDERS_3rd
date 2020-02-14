import React from "react";

class PrizesList extends React.Component{
    
    constructor(props){
        super(props);
        this.state={
          pirzes: [],
          completed:0
        }
      }
    callApi= async() =>{
        // const response = await fetch('http://106.10.58.158:3000/v1/addresses/0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C/transactions');
        const response = await fetch('http://api-ropsten.etherscan.io/api?module=account&action=txlist&address=0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C&startblock=0&endblock=99999999&sort=desc&apikey=HXQ6BH6EYJVXTA4F3KZV86961GZ4RVMR8K');
        
        const body = await response.json();
        
        return body.result;
    }
    componentDidMount(){

    }
    render(){
        return(
            <>
            
            </>
        )
    }
}