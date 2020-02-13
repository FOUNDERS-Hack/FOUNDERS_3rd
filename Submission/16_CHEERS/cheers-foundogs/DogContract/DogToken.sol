pragma solidity ^0.5.6;

// import "./ERC721/ERC721.sol";
// import "./ERC721/ERC721Enumerable.sol";
import "openzeppelin-solidity/contracts/token/ERC721/ERC721Full.sol";

contract MakersToken is ERC721Full {

    //***event 잘 모르겠음****//
    event DogsUploaded
    (uint256 indexed tokenId, string count, address appliant, uint256 charge, string photo, string description, string serialNum, string birth, string gender, string breed);
    
    //****모르는것****//
    constructor(string memory name, string memory symbol) ERC721Full(name, symbol) public {}

    
    mapping (uint256 => Dog) public _DogsList; //dogs 배열
    mapping (uint256 => address) public dogToOwner; //강아지 토큰에 해당하는 owner 주소
    mapping (address => uint256) public MyDog; //나의 강아지 토큰
    
    uint256 totaldogs; //totalsupply_ 강아지 토큰 총 개수
    address FounDogs = "0xa52e40097628407d224beba260673fe831de5d16";
    
    struct Dog{
        uint256 tokenId;        //강아지 토큰
        uint256 count;          //강아지 지원자수
        address[] appliant;     //강아지 지원자
        uint256[] charge;       //강아지 지원자의 지원금액
        string photo;           //강아지 사진
        string description;     //강아지 설명
        string serialNum;       //강아지 핀번호
        string birth;           //강아지 생년월일
        string gender;          //강아지 성별
        string breed;           //강아지 품종       
        bool isadopted;         //강아지 분양 여부
    }

    // --------------------------------------------------
    // dog 업로드 (완료)
    // --------------------------------------------------

    function uploadDogs
    (string memory breed, string memory gender, string memory birth, string memory serialNum, string memory photo, string memory description) public {
        uint256 tokenId = totaldogs++;
        _mint(msg.sender, tokenId);
        address[] appliant;
        uint256[] charge;
        Dog memory newDog = Dog({
            tokenId : tokenId, // 토큰id
            count : 0, //강아지 지원자 수
            appliant : appliant, // 강아지 지원자
            charge : charge, // 강아지 지원자 지원금액
            photo : photo, // 사진
            description : description, //설명
            serialNum : serialNum, // 핀번호
            birth : birth, // 생년월일
            gender : gender, // 성별
            breed : breed, //품종
            isadopted : false //분양 여부
        });

        _DogsList[tokenId] = newDog;
        MyDog[msg.sender].push(tokenId);
        dogToOwner[tokenId] = msg.sender;

        emit DogsUploaded(tokenId, photo, description, serialNum, birth, gender, breed);
    }

    // --------------------------------------------------
    // dog 하나에 대한 토큰 정보
    // --------------------------------------------------
    function getDogs (uint256 tokenId) public view
    returns(uint256, address memoy, uint256 memory, string memory, string memory, string memory, string memory, string memory) {
        require(_Dogslist[tokenId].isadopted == false);
        return (
            _Dogslist[tokenId].tokenId,
            _Dogslist[tokenId].appliant,
            _Dogslist[tokenId].photo,
            _Dogslist[tokenId].description,
            _Dogslist[tokenId].serialNum,
            _Dogslist[tokenId].birth,
            _Dogslist[tokenId].gender,
            _Dogslist[tokenId].breed,
        );
    }

    // ----------------------------------------------------------------------------------------------------------------------------------
    //   dog에 지원하기
    // ----------------------------------------------------------------------------------------------------------------------------------

    function dogApply(uint256 tokenId, uint256 price) public payable{  
        //dog 토큰에 지원자 추가하기
        _DogsList[tokenId].count += 1;
        _DogsList[tokenId].appliant.push(msg.sender);
        _DogsList[tokenId].charge.push(price);
        //토큰 송금하기
        address payable payableTokenSeller = address(uint160(FounDogs));
        payableTokenSeller.transfer(msg.value); // FounDogs klay 송금
    }

    

    // ----------------------------------------------------------------------------------------------------------------------------------
    //   원주인의 지원자 목록보기
    // ----------------------------------------------------------------------------------------------------------------------------------
    function getAppliant() public view returns(address[] memory, uint256[] memory) {
        int tokenId = MyDog[msg.sender];
        return (
            _DogsList[tokenId].appliant,
            _DogsList[tokenId].charge
        );
    }

    // ----------------------------------------------------------------------------------------------------------------------------------
    //   원주인의 분양자 선택하기
    // ----------------------------------------------------------------------------------------------------------------------------------
    function selectAppliant(address Adopter) public {
        //token 소유권 이전
        int tokenId = MyDog[msg.sender];
        dogToOwner[tokenId] = Adopter;
        MyDog[Adopter] = tokenId;
    }
    
    // ----------------------------------------------------------------------------------------------------------------------------------
    // 강아지 분양 마감 시, 환불 함수 getappliant()실행해서 지원자 정보 얻기, 소유 받은사람 한테는 토큰 다시 주면 안됨
    // ----------------------------------------------------------------------------------------------------------------------------------
    function returnklay(address addressID) public payable{
        address payable payableTokenSeller = address(uint160(addressID));
        payableTokenSeller.transfer(msg.value);        
    }


    


}
