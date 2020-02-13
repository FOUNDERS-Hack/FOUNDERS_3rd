pragma solidity ^0.5.10;

contract SimpleStorage{

    mapping (string => uint) search;

    struct data {
        string name;
        string birth;
        string will;
    }
    data[] public datas;

    function getDataCount() public view returns(uint) {
        return datas.length;
    }

    function set(string memory _name, string memory _birth, string memory _will) public {
        datas.length++;
        datas[datas.length-1].name = _name;
        datas[datas.length-1].birth = _birth;
        datas[datas.length-1].will = _will;
        search[_name]=datas.length-1;

    }
    function getByName(string memory _name)public view returns (string memory, string memory, string memory ){
        return (datas[search[_name]].name, datas[search[_name]].birth, datas[search[_name]].will);

    }
    function get(uint index) public view returns(string memory, string memory, string memory ){
        return (datas[index].name, datas[index].birth, datas[index].will);



    }
}
