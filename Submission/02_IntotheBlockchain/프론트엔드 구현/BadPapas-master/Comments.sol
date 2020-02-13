pragma solidity >=0.4.22 <0.7.0;

contract Reports {

    string name;
    string date;
    string sex;
    string company;
    string feature;
    string privateKey;


    function report(string memory _name, string memory  _date, string memory  _sex, string memory  _company, string memory  _feature) public {
        name = _name;
        date = _date;
        sex = _sex;
        company = _company;
        feature = _feature;
    }


    function getReports() public view returns(string memory ,string memory ,string memory ,string memory ,string memory) {
            return(name,date,sex,company,feature);
        }


}


