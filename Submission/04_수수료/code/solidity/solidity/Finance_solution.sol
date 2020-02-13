//Solidity 버전 불러오기
pragma solidity >=0.4.22 <0.7.0;

//사용내역 업로드 및 선언
contract Finance {
    //학생회 변수 선언
    address public Council;
    address public last_sender;
    uint public Council_received_wei;

    //이벤트 선언
    event Newfinance(string purpose, string subject, string price, string amount, string totalprice);

    //사용내역 구조체 선언
    struct UseResult {
        string purpose;
        string subject;
        string price;
        string amount;
        string totalprice;
    }

    //useresult라는 사용내역 배열 선언
    UseResult[] public useresults;

    //학생회(Council)에 학생회비 전송하는 학생 구조체 선언
    struct Student {
        uint money; //학생 보유금
        address studentId; //학생 아이디
    }

    //Student 구조체를 가지는 students를 선언
    mapping (address => Student) students;
    
    //사용내역 업로드 함수 선언
    //이벤트 선언
    function putResult(string memory _purpose, string memory _subject, string memory _price, string memory _amount, string memory _totalprice) public {
        useresults.push(UseResult(_purpose, _subject, _price, _amount, _totalprice));
        emit Newfinance(_purpose, _subject, _price, _amount, _totalprice);
    }

    //사용내역 불러오는 함수 선언
    function getResult(uint _number) public view returns(string memory getpurpose, string memory getsubject, string memory getprice, string memory getamount, string memory gettotalprice){
        getpurpose = useresults[_number].purpose;
        getsubject = useresults[_number].subject;
        getprice = useresults[_number].price;
        getamount = useresults[_number].amount;
        gettotalprice = useresults[_number].totalprice;
    }

    //학생회에 돈 보냄 web3로 프론트 상에 하면 되지만 혹시 몰라 놔둔다
    function sending() payable public {
        last_sender = msg.sender;
        Council_received_wei += msg.value;
        students[msg.sender].money -= msg.value;
    }
}