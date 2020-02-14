const listABI = [
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_lock",
				"type": "string"
			}
		],
		"name": "postData",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"internalType": "uint256",
				"name": "index",
				"type": "uint256"
			}
		],
		"name": "getData",
		"outputs": [
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "getPostCount",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"name": "posts",
		"outputs": [
			{
				"internalType": "string",
				"name": "lock",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	}
]
const listAddress = '0xc990b975e4d13db7ee76709c50772692c957e9ae';
const hexlantAPI = 'http://106.10.58.158:3000';

if (typeof web3 !== 'undefined') {
    console.log('Metamask가 설치되어 있습니다.')
    let listContract = web3.eth.contract(listABI);
    let listContractInstance = listContract.at('0xc990b975e4d13db7ee76709c50772692c957e9ae');
    ethereum.enable();

    $(document).ready(function() {
        
        // getTransactionReceipt
        var receipt = web3.eth
        .getTransactionReceipt('0x97093982f611ab49233f6afcfc3084463a49fbfa81de5997f1a906075016e603', function (err, res) {
            if (err) {
                console.log(err)
            } else {    
                    $('body').prepend(
                        `<div class="content" style="display:block">
                          <div class="card">
                            <div class="firstinfo">
                              <div class="profileinfo">
                                <div class='txHash'>` + res.transactionHash + `</div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div style="clear:both"></div>`
                    );
                console.log(res.transactionHash)
                }
        })

        // 등록된 환자 array length 
        const count = listContractInstance.getPostCount.call((err, res) => {
            if (err) {
                console.log(err)
            } else {
            }
        })

        // 환자 array 내부의 정확한 값 가져오는 것
        const inscription = listContractInstance.getData.call(1, function(err, res) {
            if (err) {
                console.log(err)
            } else {
                console.log(res)
            }
        });
    })

} else {
    $(function () {
        alert('Metamask 설치하세요');
        window.location.href = 'https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn?hl=ko'
	})
}