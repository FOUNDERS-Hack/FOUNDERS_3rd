pragma solidity ^0.5.12;

import "https://github.com/OpenZeppelin/openzeppelin-solidity/contracts/token/ERC721/ERC721Full.sol";

import "https://github.com/OpenZeppelin/openzeppelin-solidity/contracts/math/SafeMath.sol";

contract PuppyToken is ERC721Full {
    using SafeMath for uint256;

    struct Puppy {
        string name;
        string tribe;
        string gender;
        uint256 age;
        address owner;
    }

    Puppy[] public puppies;
    address public owner;

    event Adopt(
        address owner,
        uint256 puppyId
    );

    constructor() ERC721Full("Puppies", "PFT") public {
        owner = msg.sender;
    }

    function () external payable { }

    function createPuppy(string memory _name, string memory _tribe, string memory _gender, uint256 _age, address _owner) public returns (uint256) {
        require(_owner != address(0));
        Puppy memory newPuppy = Puppy({
            name: _name,
            tribe: _tribe,
            gender: _gender,
            age: _age,
            owner: _owner
        });
        uint256 newPuppyId = puppies.push(newPuppy).sub(1);
        super._mint(_owner, newPuppyId);
        emit Adopt(_owner, newPuppyId);
        return newPuppyId;
    }

    function donatePuppy() external payable returns (uint256) {
        require(msg.value == 0.002 ether, "donate");
        return createPuppy('', '', '', 0, msg.sender);
    }

    function breedPuppy() external payable {
        require(msg.value == 0.005 ether, "breed");
    }
    
    function buyClothes() external payable {
        require(msg.value == 0.01 ether, "buyClothes");
    }
    
    function buySnack() external payable {
        require(msg.value == 0.03 ether, "buySnack");
    }

    function getPuppyDetails(uint256 puppyId) external view returns (uint256, string memory, string memory, string memory, uint256) {
        Puppy storage puppy = puppies[puppyId];
        return (puppyId, puppy.name, puppy.tribe, puppy.gender, puppy.age);
    }

    function ownedPuppies() external view returns (uint256[] memory) {
        uint256 puppyCount = balanceOf(msg.sender);
        if(puppyCount == 0) {
            return new uint256[](0);
        } else {
            uint256[] memory result = new uint256[](puppyCount);
            uint256 totalPuppies = puppies.length;
            uint256 resultIndex = 0;
            uint256 puppyId = 0;
            while (puppyId < totalPuppies) {
                if(ownerOf(puppyId) == msg.sender) {
                    result[resultIndex] = puppyId;
                    resultIndex = resultIndex.add(1);
                }
                puppyId = puppyId.add(1);
            }
            return result;
        }
    }
}