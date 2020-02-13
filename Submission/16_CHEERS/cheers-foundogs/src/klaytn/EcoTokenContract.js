import caver from "klaytn/caver";

// const fs = require("fs");

/**
 * 1. Create contract instance
 * ex:) new caver.klay.Contract(DEPLOYED_ABI, DEPLOYED_ADDRESS)
 * You can call contract method through this instance.
 * Now you can access the instance by `this.countContract` variable.
 */

// const DEPLOYED_ABI = fs.readFileSync("deployedABI", "utf8");

// const DEPLOYED_ADDRESS = JSON.stringify(
//   fs.readFileSync("deployedAddress", "utf8").replace(/\n|\r/g, "")
// );

const DEPLOYED_ABI = JSON.parse(
  '[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function","signature":"0x06fdde03"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function","signature":"0x313ce567"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function","signature":"0x95d89b41"},{"inputs":[{"name":"total","type":"uint256"},{"name":"_address","type":"address"}],"payable":false,"stateMutability":"nonpayable","type":"constructor","signature":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"name":"tokenOwner","type":"address"},{"indexed":true,"name":"spender","type":"address"},{"indexed":false,"name":"tokens","type":"uint256"}],"name":"Approval","type":"event","signature":"0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925"},{"anonymous":false,"inputs":[{"indexed":true,"name":"from","type":"address"},{"indexed":true,"name":"to","type":"address"},{"indexed":false,"name":"tokens","type":"uint256"}],"name":"Transfer","type":"event","signature":"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function","signature":"0x18160ddd"},{"constant":true,"inputs":[{"name":"tokenOwner","type":"address"}],"name":"balanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function","signature":"0x70a08231"},{"constant":false,"inputs":[{"name":"receiver","type":"address"},{"name":"numTokens","type":"uint256"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":true,"stateMutability":"payable","type":"function","signature":"0xa9059cbb"},{"constant":true,"inputs":[{"name":"owner","type":"address"},{"name":"delegate","type":"address"}],"name":"allowance","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function","signature":"0xdd62ed3e"}]'
);

const DEPLOYED_ADDRESS = "0x04121200d8b80051Cacf94873b7B44286C3Dfc4D";

const EcoTokenContract = new caver.klay.Contract(
  DEPLOYED_ABI,
  DEPLOYED_ADDRESS
);

console.log("EcoTokenContract: ", EcoTokenContract);

export default EcoTokenContract;
