pragma solidity ^0.5.6;

contract Election {
    // Model a Candidate
    struct Candidate {
        uint256 id;
        string name;
        uint256 voteCount;
    }

    // Store accounts that have voted
    mapping(address => bool) public votedOrNot;
    // Read/write candidates
    mapping(uint256 => Candidate) public candidates;
    // Store Candidates Count
    uint256 public candidatesCount;
    //mapping who voted to whom
    mapping(address => uint256) public whoVotedToWhom;
    //total vote Count
    uint256 public totalVoteCount;

    function candidateList() public {
        addCandidate("Candidate 1");
        addCandidate("Candidate 2");
        //can add more
    }

    function addCandidate(string memory _name) private {
        candidatesCount++;
        candidates[candidatesCount] = Candidate(candidatesCount, _name, 0);
    }

    function vote(uint256 _candidateId) public {
        // require that they haven't voted before
        require(!votedOrNot[msg.sender]);
        // require a valid candidate
        require(_candidateId > 0 && _candidateId <= candidatesCount);
        // record that voter has voted
        votedOrNot[msg.sender] = true; //true == voted
        whoVotedToWhom[msg.sender] = _candidateId;

        // update candidate vote Count
        candidates[_candidateId].voteCount++;
        totalVoteCount++;
    }

    //show results
    function getMyVoteResult() public view returns (uint256 seeMyVoteResult) {
        //voted?
        require(
            votedOrNot[msg.sender] == true,
            "No result : the user hasn't voted yet."
        );
        //if then, voted to whom?
        return whoVotedToWhom[msg.sender];
    }
    function getTotalVoteCount()
        public
        view
        returns (uint256 seetotalVoteCount)
    {
        //total votes
        return totalVoteCount;
    }
    function getVoteCountPerCandidate(uint256 _candidateId)
        public
        view
        returns (uint256 seeVoteCount)
    {
        //candidate id --> vote count
        return candidates[_candidateId].voteCount;
    }
}
