const MAKERS = artifacts.require('./MakersToken.sol')
const fs = require('fs')

module.exports = function (deployer) {
    var name = "ECO GROUND MAKERS Token";
    var symbol = "EG";

    deployer.deploy(MAKERS, name, symbol)
        .then(() => {
            if (MAKERS._json) {
                fs.writeFile(
                    'deployedABI',
                    JSON.stringify(MAKERS._json.abi),
                    (err) => {
                        if (err) throw err
                        console.log("파일에 ABI 입력 성공");
                    })
            }

            fs.writeFile(
                'deployedAddress',
                MAKERS.address,
                (err) => {
                    if (err) throw err
                    console.log("파일에 주소 입력 성공");
                })
        });
}