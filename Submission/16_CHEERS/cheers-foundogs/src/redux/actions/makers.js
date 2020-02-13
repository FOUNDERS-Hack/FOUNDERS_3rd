import MakersContract from "../../klaytn/MakersContract";
import { getWallet } from "../../utils/crypto";
import ui from "../../utils/ui";
import { feedParser } from "../../utils/misc";
import { SET_FEED } from "./actionTypes";
// import { makersFeed } from "data";
import Makers from "../../pages/Makers";
import { toast } from "react-toastify";

// Action creators

const setFeed = feed => ({
  type: SET_FEED,
  payload: { feed }
});

// const MakersState = makersState => ({
//   type: MAKERS_STATE,
//   payload: { makersState }
// });

const updateFeed = tokenId => (dispatch, getState) => {
  console.log("updateFeed");

  MakersContract.methods
    .getDogs1(tokenId)
    .call()
    .then(newMakers => {
      const {
        makers: { feed }
      } = getState();
      const newFeed = [feedParser(newMakers), ...feed];
      dispatch(setFeed(newFeed));
    });
};

// TODO: Makers 리스트
export const getFeed = () => dispatch => {
  MakersContract.methods
    .getTotalDogsCount()
    .call()
    .then(totalMakersCount => {
      console.log("getFeed");
      if (!totalMakersCount) {
        console.log("없음");
        return [];
      }
      const feed = [];
      for (let i = totalMakersCount; i > 0; i--) {
        console.log(i);
        const product = MakersContract.methods.getDogs1(i).call();
        console.log("product", product);
        /**
         *  struct Makers{
              address[] buyer;
              bytes photo;
              string title;
              string description;
              int targetKlay;
              string D_day;
              uint256 status;
              uint256 timestamp;
              uint256 count;
              int price;
            }
         */
        feed.push(product);
      }
      return Promise.all(feed);
    })
    .then(feed => {
      dispatch(setFeed(feedParser(feed)));
    });
};

// --------------------------------------------------
//  MyMakers 확인
// --------------------------------------------------
export const _showMyMakers = addressId => dispatch => {
  MakersContract.methods
    .showMyMakers(addressId)
    .call()
    .then(totalMyMakers => {
      if (!totalMyMakers.length) {
        console.log("없음");
        return [];
      }
      const feed = [];
      for (let i = totalMyMakers.length - 1; i > 0; i--) {
        const product = MakersContract.methods
          .getMakers(totalMyMakers[i])
          .call();
        feed.push(product);
      }
      return Promise.all(feed);
    })
    .then(feed => dispatch(setFeed(feedParser(Makers))));
};

// ----------------------------------------------------------------
//              Makers 삭제
// ----------------------------------------------------------------

export const removeMakers = tokenId => dispatch => {
  MakersContract.methods
    .removeMakers(tokenId)
    .send({
      from: getWallet().address,
      gas: "200000000"
    })
    .once("transactionHash", txHash => {
      console.log("txHash:", txHash);
      ui.showToast({
        status: "pending",
        message: `Sending a transaction... (uploadPhoto)`,
        txHash
      });
    })
    .once("receipt", receipt => {
      ui.showToast({
        status: receipt.status ? "success" : "fail",
        message: `Received receipt! It means your transaction is
        in klaytn block (#${receipt.blockNumber}) (uploadPhoto)`,
        link: receipt.transactionHash
      });
      const tokenId = receipt.events.MakersUploaded.returnValues[0];
      console.log("tokenId: ", tokenId);
      dispatch(updateFeed(tokenId));
    })
    .once("error", error => {
      ui.showToast({
        status: "error",
        message: error.toString()
      });
    });
};

// ----------------------------------------------------------------
//              Makers 업로드
// ----------------------------------------------------------------

export const uploadItem = (
  breed,
  gender,
  birth,
  serialNum,
  photo,
  description
) => dispatch => {
  console.log(
    `
    breed   : ${breed} 
    gender      : ${gender}
    birth: ${birth}
    serialNum : ${serialNum}
    photo      : ${photo}
    description      : ${description}
    `
  );

  MakersContract.methods
    .uploadDogs(breed, gender, birth, serialNum, photo, description)
    .send({
      from: getWallet().address,
      gas: "200000000"
    })
    .once("transactionHash", txHash => {
      console.log("txHash:", txHash);
      // ui.showToast({
      //   status: "pending",
      //   message: `Sending a transaction... (uploadMakers)`,
      //   txHash
      // });
      toast.success("Sending a transaction...");
    })
    .once("receipt", receipt => {
      // ui.showToast({
      //   status: receipt.status ? "success" : "fail",
      //   message: `Received receipt! It means your transaction is
      //     in klaytn block (#${receipt.blockNumber}) (uploadMakers)`,
      //   link: receipt.transactionHash
      // });
      toast.success("Received receipt!");
      const tokenId = receipt.events.DogsUploaded.returnValues[0];
      console.log("-----------------");
      console.log("tokenId: ", tokenId);
      console.log("————————");

      sessionStorage.setItem("txList", receipt.transactionHash);

      dispatch(updateFeed(tokenId));
    })
    .once("error", error => {
      console.log(error);
      ui.showToast({
        status: "error",
        message: error.toString()
      });
    });
};
