const HDWalletProvider = require("truffle-hdwallet-provider-klaytn");
const NETWORK_ID = "1001";
const GASLIMIT = "20000000";
const URL = "https://api.baobab.klaytn.net:8651";
const PRIVATE_KEY =
  "0x4a5c621280be5e2081592cf0b8198a293118ca9082307f7b5a0d8d2bba61f8b0";

module.exports = {
  networks: {
    baobab: {
      provider: () => new HDWalletProvider(PRIVATE_KEY, URL),
      network_id: NETWORK_ID,
      gas: GASLIMIT,
      gasPrice: null
    },
    ganache: {
      host: "localhost",
      port: 8545,
      network_id: "*" // Match any network id
    }
  },

  compilers: {
    solc: {
      version: "0.5.6"
    }
  }
};
