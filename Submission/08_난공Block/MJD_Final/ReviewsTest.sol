pragma solidity >=0.4.21 <=0.6.1;


contract ReviewsTest {
    struct Vote {
        uint voteCount; // 전체 투표 수
        uint approve;   // 찬성표
        bool isVoting;  //투표 중 체크
    }

    struct Review {
        uint reviewId;  // 리뷰 고유 번호
        address userId;  // 리뷰 등록자
        string storeName; //가게 이름
        string comment; // 리뷰 내용
        string date;    // 날짜
        bool isTrust;     //신뢰도 있는 리뷰 (찬성 70% 이상)
        Vote vote;  //투표 정보
    }

    // 전체 리뷰 리스트
    Review[] reviews;

    // userId의 가게 리뷰 작성에 대한 권한, authRvList[address userId] => storeId[]
    mapping(address => string[]) private authRvList;
    // userId의 리뷰 투표에 대한 권한, authVoteList[address userId] => reviewId[]
    mapping(address => uint[]) private authVoteList;
    // 가게 별 리뷰 고유 번호 array 연결, storeReviews[string_storeName] => 리뷰 번호 Array
    mapping(string => uint[]) private storeReviews;
    // 자신이 쓴 리뷰 고유 번호 array 연결, myReviews[address] => 리뷰 번호 Array
    mapping(address => uint[]) private myReviews;

    // 가게 리뷰, 투표 권한 부여
    function giveAuth(address _userId, string memory _storeName) public{
        authRvList[_userId].push(_storeName);
        for(uint i = 0; i < storeReviews[_storeName].length; i++) {
            authVoteList[_userId].push(storeReviews[_storeName][i]);
        }
    }

    // 지정된 가게의 리뷰 개수
    function getStoreReviewCount(string memory _storeName) public view returns(uint) {
        return storeReviews[_storeName].length;
    }

    // 가게에 등록된 리뷰 인덱스로 조회
    function getStoreReview(string memory _storeName, uint _index) public view returns(uint, address, string memory
    , string memory, string memory, bool, uint, uint, bool) {
        uint findReviewNumber = storeReviews[_storeName][_index];
        Review memory storeReview;
        for(uint i = 0; i < reviews.length; i++) {
            if(reviews[i].reviewId == findReviewNumber) {
                storeReview = reviews[i];
                break;
            }
        }
        return (storeReview.reviewId, storeReview.userId, storeReview.storeName, storeReview.comment,
        storeReview.date, storeReview.isTrust, storeReview.vote.voteCount, storeReview.vote.approve, storeReview.vote.isVoting);
    }

    // 내 리뷰 개수
    function getMyReviewCount(address _userId) public view returns (uint) {
        return myReviews[_userId].length;
    }

    //내 리뷰 index 접근
    function getMyReview(address _userId, uint _index) public view returns (uint, address, string memory
    , string memory, string memory, bool, uint, uint, bool) {
        uint findReviewNumber = myReviews[_userId][_index];
        Review memory myReview;
        for(uint i = 0; i < reviews.length; i++) {
            if(reviews[i].reviewId == findReviewNumber) {
                myReview = reviews[i];
                break;
            }
        }
        return (myReview.reviewId, myReview.userId, myReview.storeName, myReview.comment,
        myReview.date, myReview.isTrust, myReview.vote.voteCount, myReview.vote.approve, myReview.vote.isVoting);
    }

    //새 리뷰 작성
    function postReview(address _userId, string memory _storeName, string memory _comment, string memory _date) public returns (bool) {
        uint findIndex;
        for(uint i = 0; i<authRvList[_userId].length; i++) {
            if((keccak256(abi.encode(authRvList[_userId][i]))) == keccak256(abi.encode(_storeName))) {
                findIndex = i;
                break;
            }
        }
        // 거래내역이 없어 리뷰 권한이 없거나 이미 작성함. -> 리뷰 권한이 없습니다.
        if(findIndex == authRvList[_userId].length) {
            return false;
        }
        //리뷰넘버, address, 가게이름, comment, 날짜, NoTrust, (총, 찬성, 투표 중)
        reviews.push(Review(reviews.length, _userId, _storeName, _comment, _date, false, Vote(0, 0, true)));
        storeReviews[_storeName].push(reviews.length);
        myReviews[_userId].push(reviews.length);
        // 리뷰 작성 후 권한 삭제
        delete authRvList[_userId][findIndex];
        return true;
    }

    // 투표하기
    function postVote(address _userId, uint _reviewId, bool _approve) public returns (bool) {
        uint findIndex;
        for(uint i = 0; i<authVoteList[_userId].length; i++) {
            if(authVoteList[_userId][i] == _reviewId) {
                findIndex = i;
                break;
            }
        }
        // 거래 내역이 없어 투표 권한 없음. -> 투표 권한이 없습니다.
        if(findIndex == authVoteList[_userId].length) {
            return false;
        }
        // 찬성
        if(_approve) {
            reviews[_reviewId].vote.voteCount += 1;
            reviews[_reviewId].vote.approve += 1;
        }
        // 반대
        else {
            reviews[_reviewId].vote.voteCount += 1;
        }

        // 투표 권한 삭제
        delete authVoteList[_userId][findIndex];

        // 투표 수 체크 후 종료 여부 결정
        if(reviews[_reviewId].vote.voteCount == 20) {
            reviews[_reviewId].vote.isVoting = false;
            // 70% 넘게 찬성
            if((reviews[_reviewId].vote.approve/reviews[_reviewId].vote.voteCount*100)>=70) {
                reviews[_reviewId].isTrust = true;
            }
        }

        return true;
    }
}

