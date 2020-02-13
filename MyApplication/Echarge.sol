pragma solidity ^0.6.1;

contract Echarge {
    // 충전을 원하는 사람(충전원하는 자동자 소유주)의 주소.
    address internal accept_owner;
    // 충전이 끝난후 충전값을 보낼사람의 주소.
    address internal donation_owner;

    // 충전 시작하고 끝나는시간.
    uint256 public charge_start;
    uint256 public charge_end;

}