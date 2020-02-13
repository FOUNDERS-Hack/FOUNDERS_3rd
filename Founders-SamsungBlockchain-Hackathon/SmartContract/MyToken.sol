pragma solidity ^0.5.0;

import '@openzeppelin/contracts/token/ERC721/ERC721Full.sol';

contract MyContract is ERC721Full {
    constructor() ERC721Full("Vote Star Token", "VST") public {
        
    }

}