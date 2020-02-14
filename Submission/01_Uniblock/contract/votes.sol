pragma solidity >=0.5.1 <0.7.0;
pragma experimental ABIEncoderV2;

contract Vote {

    mapping(address => int8) check;
    
    uint256 agreeVoteCnt = 0;
    uint256 disAgreeVoteCnt = 0;

    address public _govContractAddr;
    
    modifier only_once {
        check[msg.sender] == 0;
        _;
    }
    
    function setGovContractAddr(address addr) internal {
        _govContractAddr = addr;
    }
    
    function agree(uint256 governanceCnt) public only_once returns (bool) {
        //if(!isInGovernence_ASM(msg.sender)) return false;
        
        agreeVoteCnt++;
        return innerResultChecker(agreeVoteCnt, governanceCnt);
    }
    
    function disagree(uint256 governanceCnt) public only_once returns (bool) {
        //if(!isInGovernence_ASM(msg.sender)) return false;
        
        disAgreeVoteCnt++;
        return innerResultChecker(disAgreeVoteCnt, governanceCnt);
    }
    
    function innerResultChecker(uint256 cnt, uint256 governanceCnt) internal returns (bool) {
        check[msg.sender] = 1;
        uint256 f = (governanceCnt-uint256(1))/uint256(3);
        
        if(2*f+1 <= cnt) return true;
        
        return false;   
    }
    
    function resultChecker(uint256 governanceCnt) public view returns (bool) {
        uint256 f = (governanceCnt-uint256(1))/uint256(3);
        if(2*f+1 <= agreeVoteCnt) return true;
        return false;   
    }
    
    /*
    function isInGovernence_ASM(address addr) public payable returns (bool result) {
        bytes4 sig = bytes4(keccak256("isInGovernance(address)"));
        assembly {
            // move pointer to free memory spot
            let ptr := mload(0x40)
            // put function sig at memory spot
            mstore(ptr,sig)
            // append argument after function sig
            mstore(add(ptr,0x20), addr)
            let result := call(
              210000, // gas limit
              sload(_govContractAddr_slot),  // to addr. append var to _slot to access storage variable
              0, // not transfer any ether
              ptr, // Inputs are stored at location ptr
              0x20, // Inputs are 32 bytes address
              ptr,  //Store output over input
              0x01) //Outputs are bool
            
            if eq(result, 0) {
                revert(0, 0)
            }
            
            result := mload(ptr) // Assign output to answer var
            mstore(0x40,add(ptr,0x01)) // Set storage pointer to new space
        }
    }
    */
    
}
