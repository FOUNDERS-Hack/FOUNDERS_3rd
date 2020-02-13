pragma solidity >=0.5.0;

contract trackmate {

    
    struct Player{
        address payable name;   
        uint256 laptime;  
    }

    Player[] Players;       
    uint256 prizemoney=0;
    
    
    function setPlayer(uint256 _laptime, uint256 _money) payable public
    {
        Players.push(Player(msg.sender,_laptime));
        prizemoney+=_money;
        
        if(Players.length==2){
            if(Players[0].laptime>Players[1].laptime){
                Players[1].name.transfer(prizemoney);
            }else{
                Players[0].name.transfer(prizemoney);
            }
        }
    }

    
    

}