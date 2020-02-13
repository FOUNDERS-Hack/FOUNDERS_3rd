import React from "react";
import styled from "styled-components";
import MakersHeader from "components/MakersHeader";
import { getWallet } from "utils/crypto";
import { connect } from "react-redux";
import ui from "utils/ui";
import MakersContract from "klaytn/MakersContract";
import cav from "klaytn/caver";
import EcoTokenContract from "klaytn/EcoTokenContract";
import * as makersActions from "redux/actions/makers";
import dealService from "../api/deal";
import { toast } from "react-toastify";

const Container = styled.main`
  width: 100%;
  min-height: 100%;
  min-width: ${props => props.theme.maxCardWidth};
  max-width: ${props => props.theme.maxCardWidth};
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Button = styled.button`
  margin-bottom: 20px;
  margin-top: 20px;
  width: 100px;
`;

// TODO: Mysql 호출을 통해서 txAddress 값을 불러와야함.
// --------------------------------------------------
//  마이페이지 - 구매목록 호출
// --------------------------------------------------

// TODO: 여기 41번 라인에서 txArray가 529번 에서 DB에서 뽑은 TxArray를 넣어줘야함

const _showTracking = data => {
  // [1] sesstion storag에 있는 JWT로 조회

  console.log("_showTracking 호출");
  console.log(data);
  console.log("-----------------------");
  console.log(data[0]);
  console.log(data[0].Deals[0].id);
  console.log("data.length: ", data.length);
  for (let i = 0; i < data.length; i++) {
    cav.klay.getTransactionReceipt(data[i].Deals[i].hash).then(result => {
      var resultList = [];
      resultList[0] = result.type.toString(); // tx타입
      resultList[1] = result.blockNumber.toString(); // 블록번호
      resultList[2] = result.value.toString(); // value 값 (가격)
      resultList[3] = data[i].Deals[i].hash; // tx주소값

      console.log(resultList);
      return resultList;
    });
  }
};

// --------------------------------------------------
//  친환경 제품 구매
// --------------------------------------------------

const _purchaseGoods = price => {
  console.log("_purchaseGoods 호출");
  console.log("price : ", price);

  // TODO: param1 값을 상품 주인의 값에 하드 코딩.

  getCount(getWallet().address).then(cnt => {
    MakersContract.methods
      .purchaseToken("0xc4b83caa6a8c07168cec216bae6813f1a165ee2f", price)
      .send({
        from: getWallet().address,
        gas: "200000000",
        value: cav.utils.toPeb(price.toString(), "KLAY"),
        cnt: cnt + 1
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
        console.log("영수증 완료");
        ui.showToast({
          status: receipt.status ? "success" : "fail",
          message: `Received receipt! It means your transaction is in klaytn block (#${receipt.blockNumber}) (uploadPhoto)`,
          link: receipt.transactionHash
        });
      })
      .once("error", error => {
        ui.showToast({
          status: "error",
          message: error.toString()
        });
      });
    console.log("---------------------------");
    console.log("rewardToken 호출");
    console.log("---------------------------");

    EcoTokenContract.methods
      .transfer(getWallet().address, 5)
      .send({
        from: getWallet().address,
        gas: "200000000",
        nonce: cnt + 1
      })
      .once("receipt", receipt => {
        console.log("영수증 완료2");
        ui.showToast({
          status: receipt.status ? "success" : "fail",
          message: `Received receipt! It means your transaction is in klaytn block (#${receipt.blockNumber}) (uploadPhoto)`,
          link: receipt.transactionHash
        });
      })
      .once("error", error => {
        console.log(error);
        ui.showToast({
          status: "error",
          message: error.toString()
        });
      });
  });
  // TODO: 구매 완료 후  구매완료 alert 창을 띄우고 나서 그 창을 확인 했을 때 지급 하는 식으로 해야할듯.
};

// --------------------------------------------------
// Makers 목표 금액 확인
// --------------------------------------------------

const _showTargetKlay = tokenId => {
  console.log("_showTargetKlay 함수 호출");

  MakersContract.methods
    .showTargetKlay(tokenId)
    .call()
    .then(targetKlay => {
      if (!targetKlay) {
        return 0;
      }
      console.log("-----------------");
      console.log("targetKlay : ", targetKlay);
      console.log("-----------------");
    });
};

// --------------------------------------------------
// 메이커스 강제 종료
// --------------------------------------------------
//

const _prohibitMakers = tokenId => {
  MakersContract.methods
    .showMakersState(tokenId)
    .call()
    .then(result => {
      if (result === 0) {
        console.log("이미 종료된 Makers 입니다.");
        return 0;
      } else {
        MakersContract.methods
          .forcedClosure(tokenId)
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
          })
          .once("error", error => {
            ui.showToast({
              status: "error",
              message: error.toString()
            });
          });
      }
    });
};

// --------------------------------------------------
//  MyMakers 확인 (master)
// --------------------------------------------------

const _checkMyMakers = addressId => {
  console.log("checkMyMakers 호출");

  MakersContract.methods
    .showMyMakers_cutsomer(addressId)
    .call()
    .then(Makers => {
      if (Makers.length === 0) {
        console.log("해당되는 Makers가 없습니다.");
        return 0;
      }
      console.log("-----------------------------");
      for (let i = Makers.length; i > 0; i--) {
        console.log(MakersContract.methods.getMakers(i));
      }
      console.log("-----------------------------");
    });
};

// --------------------------------------------------
// MyMakers 확인 (운영진)
// --------------------------------------------------

// const _check_master = addressId => {
//   console.log("_check_master 호출 됨");

//   MakersContract.methods
//     .showMyMakers(addressId)
//     .call()
//     .then(Makers => {
//       if (Makers.length === 0) {
//         console.log("해당되는 Makers가 없습니다.");
//         return 0;
//       }
//       console.log("-----------------------------");
//       for (let i = Makers.length; i > 0; i--) {
//         console.log(MakersContract.methods.getMakers(i));
//       }
//       console.log("-----------------------------");
//     });
// };

// --------------------------------------------------
// Makers 상태 확인함수.
// --------------------------------------------------

const _showState = tokenId => {
  console.log("showState 함수 호출");

  MakersContract.methods
    .checkMakersStatus(tokenId)
    .call()
    .then(state => {
      console.log("state: ", state);
    });
};

// --------------------------------------------------
//  Makers 현재 모금액 확인
// --------------------------------------------------

const _checkDonate = tokenId => {
  console.log("checkNodate 호출");

  MakersContract.methods
    .parentStateMakers(tokenId)
    .call()
    .then(donate => {
      if (!donate) {
        return 0;
      }
      console.log("---------< donate >---------------");
      console.log(donate);
      console.log("-----------------------------");
    });
};

// --------------------------------------------------
//  Makers 임의 종료 (투자한 Klay 환불 처리.)
// --------------------------------------------------

async function getCount(address) {
  const cnt = await cav.klay.getTransactionCount(address);
  return cnt;
}

const _removeMakers = tokenId => {
  console.log("refund 함수 호출");
  console.log("showMakersState 함수 호출");

  MakersContract.methods
    .showMakersState(tokenId)
    .call()
    .then(result => {
      if (result === 0) {
        MakersContract.methods
          .showMakersPrice(tokenId)
          .call()
          .then(price => {
            if (!price) {
              return 0;
            } // getTransactionReceipt _
            MakersContract.methods
              .showInvestor(tokenId)
              .call()
              .then(buyer => {
                if (!buyer) {
                  console.log("투자자가 없어서 환불 처리는 없습니다.");
                  return 0;
                } else {
                  console.log("buyer수 :  ", buyer.length);
                  console.log("returnKlay 함수 호출");
                  getCount(getWallet().address).then(cnt => {
                    for (let i = 0; i < buyer.length; i++) {
                      MakersContract.methods
                        .returnklay(buyer[i])
                        .send({
                          from: getWallet().address,
                          gas: "20000000",
                          value: cav.utils.toPeb(price.toString(), "KLAY"),
                          nonce: cnt + i
                        })
                        .once("transactionHash", txHash => {
                          // TODO : param1 : txHash
                          // TODO : 여기
                          dealService.registerDeal(txHash);

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
                        })
                        .once("error", error => {
                          console.log(error);

                          ui.showToast({
                            status: "error",
                            message: error.toString()
                          });
                        });
                    }
                  });
                }
              });
          });
      } else {
        console.log("종료된 Makers가 아니라 환불 처리가 불가능합니다.");
      }
    });
};

// --------------------------------------------------
//  Makers 공동구매
// --------------------------------------------------

const _investMakers = tokenId => {
  console.log("invest", tokenId);

  MakersContract.methods
    .showMakersState(tokenId)
    .call()
    .then(result => {
      if (result === 0 || result === 2) {
        console.log("종료된 메이커스 입니다.");
        return 0;
      } else {
        MakersContract.methods
          .prohibitOverlap(tokenId)
          .call()
          .then(result2 => {
            if (result2 === false) {
              console.log("이미 참여한 Makers 입니다.");
              return 0;
            } else {
              MakersContract.methods
                .showMakersPrice(tokenId)
                .call()
                .then(price => {
                  if (!price) {
                    return 0;
                  }

                  getCount(getWallet().address).then(cnt => {
                    MakersContract.methods
                      .investMakers(tokenId)
                      .send({
                        from: getWallet().address,
                        gas: "200000000",
                        value: cav.utils.toPeb(price.toString(), "KLAY"),
                        nonce: cnt + 1
                      })
                      .once("transactionHash", txHash => {
                        console.log("txHash:", txHash);

                        // TODO : param1 : txHash
                        // TODO : 여기!
                        dealService.registerDeal(txHash);

                        ui.showToast({
                          status: "pending",
                          message: `Sending a transaction... (uploadPhoto)`,
                          txHash
                        });
                      })
                      .once("receipt", receipt => {
                        ui.showToast({
                          status: receipt.status ? "success" : "fail",
                          message: `Received receipt! It means your transaction is in klaytn block (#${receipt.blockNumber}) (uploadPhoto)`,
                          link: receipt.transactionHash
                        });
                      })
                      .once("error", error => {
                        console.log("_Invest Error");
                        console.log(error);
                        ui.showToast({
                          status: "error",
                          message: error.toString()
                        });
                      });
                  });
                });

              console.log("------------------------------------");
              console.log("reward Eco power");
              console.log("------------------------------------");

              EcoTokenContract.methods
                .transfer(getWallet().address, 3)
                .send({
                  from: getWallet().address,
                  gas: "200000000"
                })
                .once("receipt", receipt => {
                  console.log("Eco power 영수증");
                  ui.showToast({
                    status: receipt.status ? "success" : "fail",
                    message: `Received receipt! It means your transaction isin klaytn block (#${receipt.blockNumber}) (uploadPhoto)`,
                    link: receipt.transactionHash
                  });
                })
                .once("error", error => {
                  console.log(error);
                  ui.showToast({
                    status: "error",
                    message: error.toString()
                  });
                });
            }
          });
      }
    });
};

const _check_master = addressId => {
  console.log("_check_master 호출 됨");
  //const price = 10;
  const FounDogs = "0xa52e40097628407d224beba260673fe831de5d16";
  MakersContract.methods
    .dogApply(1, 1, FounDogs)
    .call()
    .send({
      from: getWallet().address,
      gas: "200000000",
      value: cav.utils.toPeb(price.toString(), "KLAY")
    })
    .once("transactionHash", txHash => {
      console.log("txHash:", txHash);
      ui.showToast({
        status: "pending",
        message: `Sending a transaction... (apply)`,
        txHash
      }).once("receipt", receipt => {
        // ui.showToast({
        //   status: receipt.status ? "success" : "fail",
        //   message: `Received receipt! It means your transaction is
        //     in klaytn block (#${receipt.blockNumber}) (uploadMakers)`,
        //   link: receipt.transactionHash
        // });
        toast.success("Received receipt!");
        console.log("-----------------");
        //console.log("tokenId: ", tokenId);
        console.log("————————");

        sessionStorage.setItem("txList", receipt.transactionHash);
      });
    });
};

const price = 1;
const TokenId = 1;
// const txAddress =
//   "0xa1df269a769a164f11f334f3402257735b3a8766f9d593a56a8c47d0a4e3d67c";
const txArray = [
  {
    data: "0x111baf1bf63462563663047899f9bbfef2d00dc1329615fec269a7d1afd97444"
  },
  {
    data: "0x111baf1bf63462563663047899f9bbfef2d00dc1329615fec269a7d1afd97444"
  },
  { data: "0x111baf1bf63462563663047899f9bbfef2d00dc1329615fec269a7d1afd97444" }
];

const invest = e => {
  const invest_value = e.target.value;
  console.log(invest_value);
  _investMakers(invest_value);
};

const remove = e => {
  const remove_value = e.target.value;
  console.log(remove_value);
  _removeMakers(remove_value);
};

// TokenID로 하드코딩 되어 있어서, 해당 Makers 의 TokenID 값을 불러와서 이 함수 불러와주면 됨.
const checkDonate = e => {
  const checkDonate_value = e.target.value;
  console.log(checkDonate_value);
  _checkDonate(checkDonate_value);
};

const showTargetKlay = e => {
  const showTarget_value = e.target.value;
  console.log(showTarget_value);
  _showTargetKlay(showTarget_value);
};

const showState = e => {
  const state_value = e.target.value;
  console.log(state_value);
  _showState(state_value);
};

// 메이커스 강제 종료
const prohibitMakers = e => {
  const prohibit_value = e.target.value;
  console.log(prohibit_value);
  _prohibitMakers(prohibit_value);
};

// ------------------------------------------------------------------------------------
//                        Eco Toekn Logic (ERC20)
// ------------------------------------------------------------------------------------

// ----------------------
// 보유한 Eco Token 확인
// ----------------------

const _showMyToken = addressId => {
  console.log("_showMyToken 호출");

  EcoTokenContract.methods
    .balanceOf(addressId)
    .call()
    .then(result => {
      console.log("총 보유 Eco Token : ", result);
    });
};

// ----------------------
// Eco Token 보상
// ----------------------

const _rewardToken = addressId => {
  console.log("_rewardToken 호출");

  EcoTokenContract.methods
    .transfer(addressId, 10)
    .send({
      from: getWallet().address,
      gas: "200000000"
    })
    .once("receipt", receipt => {
      ui.showToast({
        status: receipt.status ? "success" : "fail",
        message: `Received receipt! It means your transaction is
  in klaytn block (#${receipt.blockNumber}) (uploadPhoto)`,
        link: receipt.transactionHash
      });
    })
    .once("error", error => {
      console.log(error);
      ui.showToast({
        status: "error",
        message: error.toString()
      });
    });
};

const test = props => {
  const check_customer = e => {
    // props._showMyMakers(props.userAddress);
    const checkMyMakers = e.target.value;
    console.log(checkMyMakers);
    _checkMyMakers(checkMyMakers);
  };

  const check_master = e => {
    const Makers = e.target.value;
    console.log(Makers);
    _check_master(Makers);
  };

  const showMyToken = e => {
    const showMyToken_value = e.target.value;
    console.log(showMyToken_value);
    _showMyToken(showMyToken_value);
  };

  const showTracking = e => {
    const track_value = e.target.value;
    console.log("track_value : ", track_value);
    dealService.getDealList().then(result => {
      _showTracking(result);
    });
  };

  const rewardToken = e => {
    const reward_value = e.target.value;
    console.log("reward_value : ", reward_value);
    _rewardToken(reward_value);
  };

  const purchaseGoods = e => {
    const purchaseGoods_value = e.target.value;
    console.log("purchaseGoods : ", purchaseGoods_value);
    _purchaseGoods(purchaseGoods_value);
  };

  // TODO: 529번 라인에서 txArray 에 DB 저장된 값이 들어가야함.

  const FounDogs = "0xa52e40097628407d224beba260673fe831de5d16";

  return (
    <Container>
      <MakersHeader />
      <Button onClick={() => _check_master(1, 1, FounDogs)}>Dog apply</Button>
      <Button onClick={remove} value={TokenId}>
        refund
      </Button>
      <Button onClick={invest} value={TokenId}>
        investMakers
      </Button>
      <Button onClick={check_customer} value={props.userAddress}>
        checkMakers (customer)
      </Button>
      <Button onClick={check_master} value={props.userAddress}>
        checkMakers (master)
      </Button>
      <Button onClick={checkDonate} value={TokenId}>
        checkDonate
      </Button>
      <Button onClick={showTargetKlay} value={TokenId}>
        showTargetKlay
      </Button>
      <Button onClick={showState} value={TokenId}>
        showState
      </Button>
      <Button onClick={prohibitMakers} value={TokenId}>
        prohibitMakers
      </Button>
      <Button onClick={showTracking} value={txArray}>
        showTracking
      </Button>
      <Button onClick={showMyToken} value={props.userAddress}>
        ERC20_showMyToken
      </Button>
      <Button onClick={rewardToken} value={props.userAddress}>
        ERC20_rewardToken
      </Button>
      <Button onClick={purchaseGoods} value={price}>
        Buy_Goods
      </Button>
      <Button
        onClick={() =>
          ui.showToast({
            status: "pending",
            message: `Received receipt! It means your transaction is
          in klaytn block (#322231) (uploadPhoto)`,
            link: "linklinklinklink",
            txHash: "dkjfkds"
          })
        }
      >
        Toast test
      </Button>
      <Button
        onClick={() =>
          toast.success(`
          거래에 성공하였습니다 (#322231) ${"/n"} txHash: ${props.userAddress}
          `)
        }
      >
        toastify test
      </Button>
    </Container>
  );
};

const mapStateToProps = state => ({
  isLoggedIn: state.auth.isLoggedIn,
  userAddress: state.auth.address
});

const mapDispatchToProps = dispatch => ({
  _showMyMakers: addressId => dispatch(makersActions._showMyMakers(addressId))
});

export default connect(mapStateToProps, mapDispatchToProps)(test);
