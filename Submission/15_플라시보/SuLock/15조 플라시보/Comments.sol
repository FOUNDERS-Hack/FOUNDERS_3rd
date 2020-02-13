pragma solidity ^0.5.10;

contract Comments {
    struct Post {
        string area;
        string bname;
        string ph;
        string bod;
        string cod;
        string toc;
        bool show;
    }
    Post[] public posts;

    function getPostCount() public view returns(uint) {
        return posts.length;
    }

    function post(string memory _area, string memory _bname, string memory _ph, string memory _bod, string memory _cod, string memory _toc) public {
        posts.length++;
        posts[posts.length-1].area = _area;
        posts[posts.length-1].bname = _bname;
        posts[posts.length-1].ph = _ph;
        posts[posts.length-1].bod = _bod;
        posts[posts.length-1].cod = _cod;
        posts[posts.length-1].toc = _toc;
        posts[posts.length-1].show = true;
    }

    function hidePost(uint index) public {
        posts[index].show = false;
    }

    function getPost(uint index) public view returns(string memory, string memory, string memory, string memory, string memory, string memory) {
        return (posts[index].area, posts[index].bname, posts[index].ph, posts[index].bod, posts[index].cod, posts[index].toc);
    }

}