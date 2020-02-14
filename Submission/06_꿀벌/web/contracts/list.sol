pragma solidity >=0.5.0 <0.6.2;

contract history {
    struct Post {
        string lock;
    }

    Post[] public posts;

    function getPostCount() public view returns(uint) {
        return posts.length;
    }

    function postData(string memory _lock) public {
        posts.push(Post(_lock));
    }

    function getData(uint index) public view returns(string memory) {
        return (posts[index].lock);
    }
}