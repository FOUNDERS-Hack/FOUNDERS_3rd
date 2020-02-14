pragma solidity >=0.5.1 <0.7.0;

import "./models.sol";
import "./galaxy.sol";
pragma experimental ABIEncoderV2;



//One Action is one contract
contract Action {
    
    address _userInfoContractAddr;

    uint256 Day = 86400;
    uint256 Week = 604800;
    

    Models.Planet _planet;
    uint256 _biddingDeadline;
    uint256 _proofBiddingDeadline;
    uint256 _revealTime;
    
    mapping(address => bytes32) _bidderAmount;
    
    address _highestBidder;
    uint256 _highestAmount;
    
    Galaxy _galaxy;
    
    
    constructor(address userInfoContractAddr, Models.Planet memory planet, uint256 deadline, uint256 revealTime) public {
        _userInfoContractAddr = userInfoContractAddr;
        _planet = planet;
        _biddingDeadline = deadline;
        _proofBiddingDeadline = deadline + 3*Day;
        _revealTime = revealTime;
    }
    
    /*
    constructor(address userInfoContractAddr) public {
        _userInfoContractAddr = userInfoContractAddr;
    }
    */
    function bidding(bytes32 amount) public {
        require(
            _biddingDeadline > now,
            "deadline is finished"
        );
        
        _bidderAmount[msg.sender] = amount;
    }
    
    
    function proofMyBidding(bytes32 secret, uint256 value) public {
        require(
            _proofBiddingDeadline < now,
            "deadline is finished"
        );
        
        bytes32 biddinghash1 = keccak256(abi.encodePacked(secret, value));
        bytes32 biddinghash2 = _bidderAmount[msg.sender];
        
        for(uint256 i=0; i<32; i++) {
            if(biddinghash1[i] != biddinghash2[i]) return ;
        }
        
        //check highest amount
        if ( _highestAmount < value ) {
            _highestBidder = msg.sender;
            _highestAmount = value;
        }
        
    }
    
    function getGalaxy(string memory name, string memory symbol, uint256 value) public returns (Galaxy) {
       /*
        require(
            msg.sender == _highestBidder &&
            address(_galaxy) == address(0) &&
            _proofBiddingDeadline > now,
            "not permissioned"
        );
        */
        Galaxy g = new Galaxy(name, symbol, value, msg.sender, address(this));
        _galaxy = g;
        g.setDeposit.value(_highestAmount)();
        return g;
    }
    
    /*
    function getUserHashRoot_ASM(address addr) public returns (bytes32 hashroot) {
         bytes4 sig = bytes4(keccak256("getUserHashRoot(bytes32)"));
        assembly {
            // move pointer to free memory spot
            let ptr := mload(0x40)
            // put function sig at memory spot
            mstore(ptr,sig)
            // append argument after function sig
            mstore(add(ptr,0x20), addr)

            let result := call(
              210000, // gas limit
              sload(_userInfoContractAddr_slot),  // to addr. append var to _slot to access storage variable
              0, // not transfer any ether
              ptr, // Inputs are stored at location ptr
              0x20, // Inputs are 36 bytes long
              ptr,  //Store output over input
              0x20) //Outputs are 32 bytes long
            
            if eq(result, 0) {
                revert(0, 0)
            }
            
            hashroot := mload(ptr) // Assign output to answer var
            mstore(0x40,add(ptr,0x20)) // Set storage pointer to new space
        }
    }
    */
}