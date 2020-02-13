pragma solidity ^0.5.6;

contract EcoToken {

    string public constant name = "EcoToken";
    string public constant symbol = "ET";
    uint8 public constant decimals = 18;  

    event Approval(address indexed tokenOwner, address indexed spender, uint tokens);
    event Transfer(address indexed from, address indexed to, uint tokens);


    mapping(address => uint256) balances;
    mapping(address => mapping (address => uint256)) allowed;
    
    uint256 totalSupply_;

    using SafeMath for uint256;

    address masterAddress;


   constructor(uint256 total, address _address) public {  
	totalSupply_ = total;
    masterAddress = _address;
	balances[_address] = totalSupply_; // 지갑주소 하드코딩 mapping
    }  

    function totalSupply() public view returns (uint256) {
	return totalSupply_;
    }
    
    function balanceOf(address tokenOwner) public view returns (uint) {
        return balances[tokenOwner];
    }

    function transfer(address receiver, uint numTokens) public payable returns (bool) {
        // require(numTokens <= balances[masterAddress]);
        balances[masterAddress] = balances[masterAddress].sub(numTokens);
        balances[receiver] = balances[receiver].add(numTokens);
        emit Transfer(masterAddress, receiver, numTokens);
        return true;
    }

    function allowance(address owner, address delegate) public view returns (uint) {
        return allowed[owner][delegate];
    }
}

library SafeMath { 
    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
      assert(b <= a);
      return a - b;
    }
    
    function add(uint256 a, uint256 b) internal pure returns (uint256) {
      uint256 c = a + b;
      assert(c >= a);
      return c;
    }
}