pragma solidity ^0.6.1;

contract Auction {

// 자동차 소유자의 주소 변수 
address internal auction_owner;
// 충전 시작과 끝나는 시간 변수
uint256 public charge_start;
uint256 public charge_end;
// 현재까지 최고 할인가격으로 단위는 ETH이다.
uint256 public highestBid;
// 최고 할인가를 말하는 참가자의 이더리움 주소.
address public highestBidder;
 
// 열거형 자료형식 , 일련의 명명된 상수들로 구성되어 있음.(ENUMERATION TYPE)
// auction_state.STARTED == 0 || auction_state.CANCELLED == 1
enum auction_state{
    CANCELLED,STARTED
}

// 다수의 형식을 하나의 이름으로 지칭 Struct.
// EX) car public Mycar; 는 Mycar 라는 공용 car 객체를 선언한것.
struct  car{
    // 차종
    string  Brand;
    // 차량등록번호
    string  Rnumber;
}
    car public Mycar;
    // 모든 에너지 판매하는 신청자의 주소를 담는 동적배열
    address[] bidders;
    // 각 에너지 판매자의 주소를 그 참가자가 부른 가격으로 사상하는 매핑객체 자료구조.
    mapping(address => uint) public bids;
    // 충전 상태(진행 중인지 , 취소되었는지)를 나타낸다. 열거형 형식의 공용변수 State(cancelled, stated)
    auction_state public STATE;

    // 함수 수정자(function modifier) 다른 함수의 행동을 수정하는 데 쓰인다.
    // 수정자 본문의 밑줄(_)는 수정자가 적용된 함수의 본문으로 대체됨

    // 첫 수정자는 충전 중인지 점검.
    modifier an_ongoing_auction(){
        require(now <= charge_end);
        _;
    }
    
    // 둘째 수정자는 함수의 호출자가 계약의 소유자(auction_owner)인지 점검한다
    // only_owner() 같이 수정자를 이용해서 계약 소유자에게 특별한 권한을 부여하는 계약을 소유가능(ownable)계약이라고 부른다. 흔히쓰는 스마트 계약 패턴
    modifier only_owner(){
        require(msg.sender==auction_owner);
        _;
    }
    // 함수양식. 
    // function 함수_이름(<매개변수 목록>) {internal | extenal | private | public} [pure | constant | view | payable] [returns (<반환 형식들>)]{}
    // constant 와 view : 역할 같음 , 함수가 계약의 상태를 변경하지 못함을 나타냄. 단 constant 지정자는 이후 폐기될 예정.
    // pure : 이 지정자는 좀 더 엄격한 조건을 명시하는 것으로 , 함수가 계약에 저장된 상태를 변경하기는커녕 읽지도 않음을 뜻함.
    // payable : 이 지정자는 함수가 이더를 받을 수 있음을 뜻한
   
    // 최고할인를 부른 참가자를 이기려면 다음번에 할인금액 더 불러야함.
    function bid() public payable returns (bool){}
    // 할인경매가 끝났을 때 참가자가 자신의 매수 신청액을 회수하는 데 쓰인다.
    function withdraw() public returns (bool){}
    // 할인경매 소유자가 자신이 시작한 경매를 취소하는 데 쓰인다.
    function cancel_auction() external returns (bool){}
    function MyCarIF (uint _biddingTime, address _owner,string _brand,string _Rnumber) public {}
    
    event BidEvent(address indexed highestBidder, uint256 highestBid);
    event WithdrawalEvent(address withdrawer, uint256 amount);
    event CanceledEvent(string message, uint256 time);  
    //event MyAuction(uint _biddingTime, address _owner,string _brand,string _Rnumber);
}


contract MyAuction is Auction{
    
    
     function () 
    {
        
    }
    
  
function MyCarIF (uint _biddingTime, address _owner,string _brand,string _Rnumber) public {
        auction_owner = _owner;
        charge_start=now;
        charge_end = charge_start + _biddingTime*1  hours;
        STATE=auction_state.STARTED;
        Mycar.Brand=_brand;
        Mycar.Rnumber=_Rnumber;
        
    }
 

 function bid() public payable an_ongoing_auction returns (bool){
      
        require(bids[msg.sender]+msg.value> highestBid,"You can't bid, Make a higher Bid");
        highestBidder = msg.sender;
        highestBid = msg.value;
        bidders.push(msg.sender);
        bids[msg.sender]=  bids[msg.sender]+msg.value;
        emit BidEvent(highestBidder,  highestBid);

        return true;
    }
    
 
  
function cancel_auction() external only_owner  an_ongoing_auction returns (bool){
    
        STATE=auction_state.CANCELLED;
        emit CanceledEvent("Auction Cancelled", now);
        return true;
     }
    
    
    
function destruct_auction() external only_owner returns (bool){
        
    require(now > charge_end,"You can't destruct the contract,The auction is still open");
     for(uint i=0;i<bidders.length;i++)
    {
        assert(bids[bidders[i]]==0);
    }

    selfdestruct(auction_owner);
    return true;
    
    }

    
function withdraw() public returns (bool){
        require(now > charge_end ,"You can't withdraw, the auction is still open");
        uint amount;

        amount=bids[msg.sender];
        bids[msg.sender]=0;
        msg.sender.transfer(amount);
        emit WithdrawalEvent(msg.sender, amount);
        return true;
      
    }
    
function get_owner() public view returns(address){
        return auction_owner;
    }
}
