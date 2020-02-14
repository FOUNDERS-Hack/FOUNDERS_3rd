import React from "react";


const Web3 = require('web3');
const solc = require('solc');
const erc20 = require('./contract/index');
const abi = require('./contract/abi')

const gasPrice = 2000001;
const gasLimit = 2000000;
const network = 'ropsten';
const selectedNetwork = 'http://106.10.58.158:3000/v1/rpc';
const accounts={ address: '0x1057B46cd3aB3c770e0a04e8D55Dd972faF8Ac4C',
    key: '018B02E5F84DD6430066E2BA3B4CF3BB6BF3D53AE9CA5BB2E11ACD9FA44B1919'}
const contractFile = 'erc20.sol'
const mainContract =  'ERC20'


function build(name, symbol, supply) {
    if (!contractFile || !mainContract || !name || !symbol || !supply) {
        console.log()
        console.log('========================================')
        console.log()
        console.log('config.js 파일안에 deployInfo를 모두 입력해주세요!'.warn)
        console.log()
        console.log('========================================')
        console.log()
        return;
    } 
    var input = {
        language: 'Solidity',
        sources: {
        [`${contractFile}`]: {
            content: erc20(name, symbol, supply)
        }
        },
        // settings: {
        // outputSelection: {
        //     '*': {
        //     '*': ['*']
        //     }
        // }
        // }
    };
    console.log("PLE : ",JSON.stringify(input))
    const output = JSON.parse(solc.compile(JSON.stringify(input)));
    const bytecode = output.contracts[contractFile][mainContract].evm.bytecode.object;
    const abi = output.contracts[contractFile][mainContract].abi;
    const result = {
        bytecode,
        abi,
    }
    return result;
}
      



class Deploy extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            deployTokenSymbol : '',
            content: '',
            open: false,
            web3: new Web3()
        }

    }
    
deploy = async (deployTokenName, deployTokenSymbol, deployTokenSupply) =>{
    console.log("AAAAAAAAAAAAAAAAAAAAAAAA")
    try { 
        const web3 = this.state.web3
      const buildRet = build(deployTokenName, deployTokenSymbol, deployTokenSupply);
      const newContract = new web3.eth.Contract(buildRet.abi);
    
      let deploy = newContract.deploy({
        data: buildRet.bytecode,
        from: web3.eth.getAccounts()}).encodeABI();
    
      const nonce = await web3.eth.getTransactionCount(this.state.web3.eth.getAccounts(), 'pending');
      const rawTx = {
        nonce,
        gasPrice,
        gasLimit,
        data: '0x' + deploy,
      }
      await web3.eth.sendTransaction(rawTx)
    //   await web3.eth.sendSignedTransaction(signedTx.rawTransaction)
    //     .once('transactionHash', (hash) => {
    //       console.log('트랜잭션 해쉬 : ', hash.info);
    //     })
    //     .once('receipt', (receipt) => {
    //       console.log('컨트랙트 배포 완료, 현재는 Pending 상태 일 수 있습니다.')
    //       console.log('새로 발행된 컨트랙트 주소 : ', receipt.logs[0].address.info)
    //       console.log('새로 발행된 컨트랙트 주소를 config.js 파일 안 contract의 address에 추가해주세요'.todo)
    //     })
    //     .on('error', () => {
    //       console.log('컨트랙트 배포 중 예러 발생'.warn)
    //     })
    } catch (e) {
      return
    } 
  };
  
    componentDidMount = ()=>{
        if (window.ethereum) {
            window.ethereum.enable().then((res) => {
                this.state.web3 = new Web3(window.ethereum);

            });
        }
    }
    render(){
        return (
            <>
                <button onClick= {this.deploy("ASD","asd","10")}></button>
            </>
        )
    }
}
export default Deploy