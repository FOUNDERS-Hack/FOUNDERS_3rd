const EcoToken = artifacts.require('./EcoToken.sol')
const fs = require('fs')

module.exports = function (deployer) {

    var decimals = "10000000000000000000";
    var total = "99999999999999999999999";
    var _address = "0xc4b83caa6a8c07168cec216bae6813f1a165ee2f";

    deployer.deploy(EcoToken, total, _address)
        .then(() => {
            if (EcoToken._json) {
                fs.writeFile(
                    'deployedABI2',
                    JSON.stringify(EcoToken._json.abi),
                    (err) => {
                        if (err) throw err
                        console.log("파일에 ABI 입력 성공");
                    })
            }

            fs.writeFile(
                'deployedAddress2',
                EcoToken.address,
                (err) => {
                    if (err) throw err
                    console.log("파일에 주소 입력 성공");
                })
        });
}