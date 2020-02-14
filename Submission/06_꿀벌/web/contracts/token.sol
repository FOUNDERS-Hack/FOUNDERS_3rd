pragma solidity >=0.5.0 <0.6.2;

import "./SafeMath.sol";

contract DappToken {
    
    using SafeMath for uint256;
    
    string  public name = "Bee Token";
    string  public symbol = "BEE";
    string  public standard = "Bee Token final v2";
    uint256 public totalSupply;
    
    address private publisher = 0x782F8853443AB778784DdF03D6835d7d068641F6;

    event Transfer(
        address indexed _from,
        address indexed _to,
        uint256 _value
    );

    event Approval(
        address indexed _owner,
        address indexed _spender,
        uint256 _value
    );

    mapping(address => uint256) public balanceOf;
    mapping(address => mapping(address => uint256)) public allowance;

    // deploy Token
    function Token (uint256 _initialSupply) public {
        balanceOf[msg.sender] = _initialSupply;
        totalSupply = _initialSupply;
    }

    function approve(address _spender, uint256 _value) public returns (bool success) {
        allowance[msg.sender][_spender] = _value;

       emit Approval(msg.sender, _spender, _value);

        return true;
    }
    
    // ********************************************************************************************************
    
    function balance(address userAddress) external view returns (uint256 balance) {
        return balanceOf[userAddress];
    }
    
    function transfer(address _to, uint256 _value) public returns (bool success) {
        require(balanceOf[msg.sender] >= _value);
        
        balanceOf[msg.sender] = balanceOf[msg.sender].sub(_value);
        balanceOf[_to] =  balanceOf[_to].add(_value);

        emit Transfer(msg.sender, _to, _value);

        return true;
    }
}