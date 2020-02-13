import caver from "../klaytn/caver";

export const isValidAddress = address => caver.utils.isAddress(address);

export const isValidPrivateKey = privateKey => {
  const washedPrivateKey =
    privateKey.slice(0, 2) === "0x" ? privateKey.slice(2) : privateKey;

  return (
    String(washedPrivateKey)
      .split("")
      .filter(character => /^[a-f0-9A-F]$/i.test(character)).length === 64
  );
};

export const getWallet = () => {
  if (!caver.klay.accounts.wallet.length) return null;
  return caver.klay.accounts.wallet[0];
};

export const getAccount = () => {
  if (!caver.klay.accounts.wallet.length) return null;
  return caver.klay.accounts.wallet[0].add('0x4a5c621280be5e2081592cf0b8198a293118ca9082307f7b5a0d8d2bba61f8b0');
};