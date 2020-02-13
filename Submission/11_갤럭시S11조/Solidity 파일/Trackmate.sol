pragma solidity 0.6.1;

contract trackmate {

    // Struct 생성
    struct Player{
        address Name;        // 플레이어명
        uint BetMoney;   // 배팅금액
        uint Record;   // 기록
    }
    
    
    uint Betted;
    Player[] public Players;       // 플레이어 배열 선언
    
    // 배팅을 하고 리스트에 데이터 저장
    function setPlayer(address _Name, uint _BetMoney, uint _Record) public
    {
        Players.push(Player(_Name, _BetMoney, _Record));
    }
    function betting(uint _BetMoney)
    /*
    // 필요한 값을 배열에서 겟 (확인용)
    function getPlayer(uint _number) public view returns(address, uint256)
    {
        return(Players[_number].Name, Players[_number].Record);
    }
    */
    // 기록이 가장 짧은 플레이어에게 보상
    function reward(address _Name, uint _Record) public {
        if (Players[].length >= 1){
            Players.Name.transfer(BetMoney)
        }
    }
}
