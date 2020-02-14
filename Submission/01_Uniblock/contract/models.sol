pragma solidity >=0.5.1 <0.7.0;

library Models {
    struct Company {
        string coName;
        string coInfo;
    }
    
    struct Planet {
        string planetName;
        string planetInfo;
    }
    
    struct User {
        address addr;
        bytes32 hashroot;
    }
}