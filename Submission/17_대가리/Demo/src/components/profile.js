import React from "react";
import Web3 from "web3";


class Profile extends React.Component {
    constructor(props){
        super(props);
        this.state={
          id : '',
          prizes: [],
          profile:'',
          completed:0
        }

    }
    // stateRefresh = () =>{
    //   this.setState({
    //     profile:'',
    //     completed:0
    //   })
    // }
    callApi= async(res) =>{  
      // const response = await fetch('http://106.10.58.158:3000/v1/addresses/0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C/transactions');
      this.state.id = res
      const response = await fetch('http://api-ropsten.etherscan.io/api?module=account&action=tokentx&address='+res+'&startblock=0&endblock=999999999&sort=asc&apikey=HXQ6BH6EYJVXTA4F3KZV86961GZ4RVMR8K');
      const body = await response.json();
      return body.result;
      
      
    }
    
      componentDidMount(){
        // this.timer = setInterval(this.progress,20);
        if (window.ethereum) {
            window.ethereum.enable().then((res) => {
                this.web3 = new Web3(window.ethereum);
                this.findAccount()
                .then(res => this.callApi(res).then((res)=>{
          if(res === "Error! Invalid address format"){
            
              this.state.prizes = []
          }else{
              for (let index = 0; index < res.length; index++) {
                  this.state.prizes[index] = res[index].tokenName+" ,";
                  
              }
          }            
      }))
                .catch(err => console.log(err));
            });
        }
        
      }
    
    findAccount = async () =>{
        const account = await this.web3.eth.getAccounts()
        
        console.log(account[0])
        return account;
    }

        

    render(){
    return (
      <div>
          {this.state.id ? <div> {this.state.id} <p>
                        {this.state.prizes}</p></div>: <div> loading </div>
            }
      </div>
    );
    }
};
  
  export default Profile;