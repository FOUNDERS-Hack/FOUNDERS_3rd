import React from 'react';
import 'semantic-ui-css/semantic.min.css';
import faker from 'faker';
import web3 from 'web3';

import Card from 'react-bootstrap/Card'
import Button from 'react-bootstrap/Button'


class Board extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            id : '',
            userName:'',
            content: '',
            prizes : []
        }

    }
    mapping= ()=>{
        try {
            const temp =web3.utils.hexToString(this.props.content.input)
            const result = JSON.parse(temp);
            console.log(result)
            
            if(result.name){
                this.state.userName = result.name
                this.state.content = result.content
                this.state.id = this.props.content.from
                console.log("suc")
                console.log(this.state.content)
            }
                
        } 
        catch (error) {
            
        }
    }
    callApi= async() =>{  
        // const response = await fetch('http://106.10.58.158:3000/v1/addresses/0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C/transactions');
        console.log(this.state.id)
        let response = ""
        try {
         
            response = await fetch('http://api-ropsten.etherscan.io/api?module=account&action=tokentx&address='+this.state.id+'&startblock=0&endblock=999999999&sort=asc&apikey=HXQ6BH6EYJVXTA4F3KZV86961GZ4RVMR8K');
           const body = await response.json();
            return body.result;
        } catch (error) {
            return []    
        }
        
      }
    componentDidMount(){
        this.mapping()
        this.callApi().then((res)=>{
            if(res === "Error! Invalid address format"){
                this.state.prizes = []
            }else{
                for (let index = 0; index < res.length; index++) {
                    this.state.prizes[index] = res[index].tokenName+" ,";
                    
                }
            }            
        })
    }
    render() {
        return (<>
        {/* <Card style={{ width: '18rem' }}>
  <Card.Img variant="top" src="holder.js/100px180" />
  <Card.Body>
    <Card.Title>Card Title</Card.Title>
    <Card.Text>
      Some quick example text to build on the card title and make up the bulk of
      the card's content.
    </Card.Text>
    <Button variant="primary">Go somewhere</Button>
  </Card.Body>
</Card> */}
            {this.state.userName &&
            <div className="comment">
                <a className="avatar">
                    <img alt ="thmubNail" src={faker.image.avatar()} />
                </a>
                <div className="content">
                    <a className="author">{this.state.userName ? <div> {this.state.userName} </div> : <div> loading </div>
            }</a>
                    <div className="metadata">
                        <span className="date">{this.props.content.blockTime}</span>
                    </div>
                    <div className="text">
                        {this.state.content}
                    </div>
                    <div className="text">
                        {this.state.prizes}
            {/* {this.state.prizes ? this.state.prizes.map((c, idx) =>{return ({c})
            }): <div> loading </div>} */}
                    </div>
                    <div className="actions">
                        <a className="reply">Reply</a>
                    </div>
                </div>
                <hr></hr>
            </div>
            }
            </>
        )
    }
}

export default Board