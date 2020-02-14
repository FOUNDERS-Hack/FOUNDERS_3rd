pragma solidity >=0.5.1 <0.7.0;
pragma experimental ABIEncoderV2;

import "https://github.com/vittominacori/solidity-linked-list/blob/master/contracts/StructuredLinkedList.sol";
import "./models.sol";
import "./votes.sol";
import "./galaxy.sol";

contract Governance {
    
    event voteToCompanyCall(voteResult result);
    
    enum voteResult{
        SUCCESS,
        FAIL,
        ERROR
    }
    constructor () public {
        _owner = msg.sender;
    }

    //functions for Governance;
    struct CandidateInfo{
        bool isFinished;
        bool nullCheck;
        string coName;
        string coInfo;
        
        CandidateVote candidateVote;
    }
    
    using StructuredLinkedList for StructuredLinkedList.List;
    StructuredLinkedList.List _companys;
    
    mapping(uint256 => address) _coAddr;
    mapping(address => uint256) _coKeyMap;
    mapping(address => CandidateInfo) _candidates;
    
    address payable _owner;
    uint256 _coKey = 1;
    
    
    function restrictRegist(address addr) public {
        if(msg.sender != _owner) return;
        toBeGovernance(addr);
    }
    
    function registration(string memory coName, string memory coInfo) public {
        Models.Company memory company = Models.Company(coName, coInfo);
        _candidates[msg.sender] = CandidateInfo(false, 
                                                    true, 
                                                    coName,
                                                    coInfo,
                                                    new CandidateVote(company, address(this)));
    }
    
    /*
        @dev tests 
    */
    function toBeGovernance(address newCo) internal{
        _companys.push(_coKey, true);
        _coAddr[_coKey] = newCo;
        _coKeyMap[newCo] = _coKey;
        _coKey++;
    }
    
    /*
        @dev This process is not completable. should fix some code.
    */
    function voteToCompany(address newCo, bool bullet) payable public {
        address sender = msg.sender;
        uint256 coKey = _coKeyMap[sender];
        
        //is in Governance?
        if(coKey == 0 || !_companys.nodeExists(coKey) || !_candidates[newCo].nullCheck){
          emit voteToCompanyCall(voteResult.FAIL);  
        }
        
        if(bullet){
            if(_candidates[newCo].candidateVote.agree(_companys.sizeOf())){
                toBeGovernance(newCo);
                emit voteToCompanyCall(voteResult.SUCCESS);
            }
        }
        else{
            if(_candidates[newCo].candidateVote.disagree(_companys.sizeOf())){
                emit voteToCompanyCall(voteResult.SUCCESS);
            }
        }
        
        _candidates[newCo].nullCheck  = true;
        emit voteToCompanyCall(voteResult.ERROR);
    }
                                                                                                                                                                                                        
 
    function isInGovernance(address addr) public view returns (bool) {
        uint256 coKey = _coKeyMap[addr];
        
        if(coKey == 0 || !_companys.nodeExists(coKey)){
          return false;
        }
        return true;
    }
 
 
  //functions for planet
    uint256 _planetKey;
    mapping(string => PlanetVote) _planetVotes;
    mapping(string => Models.Planet) _planetModels;
    string[] _planetList;
    
    function makePlanetVote(string memory planetName, string memory planetInfo, uint256 deadline, uint256 revealTime) public {
        //is in Governance?
        if(!isInGovernance(msg.sender)){
            return;
        } 
        
        Models.Planet memory p = Models.Planet(planetName, planetInfo);
        PlanetVote pv = new PlanetVote(p, deadline, revealTime, address(this));
        _planetModels[planetName] = p;
        _planetVotes[planetName] = pv;
        _planetList.push(planetName);
    }
    
    function getPlanetVote(string memory planetName) internal view returns (PlanetVote) {
        return _planetVotes[planetName];
    }
    
    function getPlanetVoteList() public view returns (string[] memory, PlanetVote[] memory) {
        string[] memory planetName = new string[](_planetList.length);
        PlanetVote[] memory planetVoteAddress = new PlanetVote[](_planetList.length);
        
        for(uint256 i=0; i<_planetList.length; i++){
            string memory pName = _planetList[i];
            PlanetVote pv = _planetVotes[pName];
            
            planetName[i] = pName;
            planetVoteAddress[i] = pv;

        }
        return (planetName, planetVoteAddress);
    }
    
    function voteToPlanet(string memory planetName, bool bullet) payable public {
        PlanetVote pv = _planetVotes[planetName];
    
        //is in Governance?
        if(!isInGovernance(msg.sender) || address(auctions[planetName]) != address(0)) return;
        
         if(bullet){
            if(pv.agree(_companys.sizeOf())){
                makeAuction(pv.getCandidate(), pv.getDeadLine(), pv.getRevealTime());
            }
        }
        else{
            if(pv.disagree(_companys.sizeOf())){
            }
        }
    }
    
    mapping(string => Auction) auctions;
    mapping(uint256 => string) auctionKeyToPName;
    uint256 auctionKey = 1;
    
    
    
    function makeAuction(Models.Planet memory planet, uint256 deadline, uint256 revealTime) internal {
        auctions[planet.planetName] = new Auction(planet, deadline, revealTime);
        auctionKeyToPName[auctionKey] = planet.planetName;
        auctionKey++;
    }
    
    function getAuctions() public view returns (string[] memory) {
        string[] memory auctionArr = new string[](auctionKey-1);
        for(uint256 i=0; i<auctionKey-1; i++) {
            auctionArr[i] = auctionKeyToPName[i+1];
        }
        
        return auctionArr;
    }
    
    
    function getAuction(string memory planetName) public view returns (Auction){
        return auctions[planetName];
    }
    /*
    function test_createGalaxy(address addr, string memory name, string memory token, uint256 value) public payable  {
        Auction(addr).genGalaxy(name, token, value);
    }
    
    function test_getGalaxy(address addr) public view returns(Galaxy) {
        return Auction(addr).getGalaxy();
    }
    
    function test_proofMyBidding(address addr, string memory secret, uint256 value) public {
        Auction(addr).proofMyBidding(secret, value);
    }

    //test auction controll
    function test_bidding(address addr, bytes32 amount) public {
        Auction(addr).bidding(amount);
    }
    
    function test_galaxy_finish(address addr) public {
        Galaxy(addr).test_finish();
    }
    
    function test_galaxy_nonFinish(address addr) public {
        Galaxy(addr).test_noneFinish();
    }
    
    function test_buyToken(address addr) payable public {
        Galaxy(addr).buyToken();
    }
    */
}

//////////////Auction/////////////////////////
contract Auction {
    
    //address _userInfoContractAddr;

    uint256 Day = 86400;
    uint256 Week = 604800;
    

    Models.Planet _planet;
    uint256 _biddingDeadline;
    uint256 _revealTime;
    
    mapping(address => bytes32) _bidderAmount;
    
    address _highestBidder;
    uint256 _highestAmount;
    
    Galaxy _galaxy;
    
    
    constructor( Models.Planet memory planet, uint256 deadline, uint256 revealTime) public {
        //_userInfoContractAddr = userInfoContractAddr;
        _planet = planet;
        _biddingDeadline = deadline;
        _revealTime = revealTime;
    }
    
    /*
    constructor(address userInfoContractAddr) public {
        _userInfoContractAddr = userInfoContractAddr;
    }
    */
    
    function getName() public view returns (string memory) {
        return _planet.planetName;
    }
    
    function bidding(bytes32 amount) public {
        /*
        require(
            _biddingDeadline > now,
            "deadline is finished"
        );
        */
        _bidderAmount[msg.sender] = amount;
    }
    
    
    function proofMyBidding(string memory secret, uint256 value) public {
        /*
        require(
            _revealTime < now,
            "deadline is finished"
        );
        */
        
        bytes32 biddinghash1 = keccak256(abi.encodePacked(secret, uint2str(value)));
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
    
    function uint2str(uint256 _i) internal pure returns(string memory _uintAsString) {
        if (_i == 0) {
            return "0";
        }
        uint j = _i;
        uint len;
        while (j != 0) {
            len++;
            j /= 10;
        }
        bytes memory bstr = new bytes(len);
        uint k = len - 1;
        while (_i != 0) {
            bstr[k--] = byte(uint8(48 + _i % 10));
            _i /= 10;
        }
        return string(bstr);
    }
    
    function getGalaxy() public view returns(Galaxy){
        return _galaxy;
    }
    
    function genGalaxy(string memory name, string memory symbol) payable public returns (Galaxy) {
        /*
        require(
            msg.sender == _highestBidder &&
            address(_galaxy) == address(0) &&
            msg.value == _highestAmount &&
            _proofBiddingDeadline > now,
            "not permissioned"
        );
        */
        Galaxy g = new Galaxy(name, symbol, msg.sender, address(this));
        g.setDeposit.value(_highestAmount)();
        _galaxy = g;
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

contract CandidateVote is Vote{
    Models.Company candidate;
    
    constructor(Models.Company memory company, address govContractAddr) public {
        candidate = company;    
        setGovContractAddr(govContractAddr);
    }
    
}

contract PlanetVote is Vote {
    Models.Planet _candidate;
    uint256 _deadline;
    uint256 _revealTime;
    
    function getCandidate() view public returns (Models.Planet memory) {
        return _candidate;
    }
    
    function getDeadLine() view public returns (uint256) {
        return _deadline;
    }
    
    function getRevealTime() view public returns (uint256) {
        return _revealTime;
    }
    
    
    constructor(Models.Planet memory planet, uint256 deadline, uint256 revealTime, address govContractAddr) public {
        _candidate = planet;
        _deadline = deadline;
        _revealTime = revealTime;
        setGovContractAddr(govContractAddr);
    }
    
}