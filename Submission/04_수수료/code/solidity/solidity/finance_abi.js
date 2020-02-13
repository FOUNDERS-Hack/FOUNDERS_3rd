var abi = [{
        "anonymous": false,
        "inputs": [{
                "indexed": false,
                "internalType": "string",
                "name": "purpose",
                "type": "string"
            },

            {
                "indexed": false,
                "internalType": "string",
                "name": "subject",
                "type": "string"
            },

            {
                "indexed": false,
                "internalType": "uint256",
                "name": "price",
                "type": "uint256"
            },

            {
                "indexed": false,
                "internalType": "uint256",
                "name": "amount",
                "type": "uint256"
            },

            {
                "indexed": false,
                "internalType": "uint256",
                "name": "totalprice",
                "type": "uint256"
            }

        ],

        "name": "Newfinance",
        "type": "event"
    },

    {
        "inputs": [],
        "name": "Council",
        "outputs": [{
            "internalType": "address",
            "name": "",
            "type": "address"
        }],

        "stateMutability": "view",
        "type": "function"
    },

    {
        "inputs": [],
        "name": "Council_received_wei",
        "outputs": [{
            "internalType": "uint256",
            "name": "",
            "type": "uint256"
        }],

        "stateMutability": "view",
        "type": "function"
    },
    
    {
        "inputs": [{
            "internalType": "uint256",
            "name": "_number",
            "type": "uint256"
        }],
        "name": "getResult",
        "outputs": [{
                "internalType": "string",
                "name": "getpurpose",
                "type": "string"
            },
            {
                "internalType": "string",
                "name": "getsubject",
                "type": "string"
            },
            {
                "internalType": "uint256",
                "name": "getprice",
                "type": "uint256"
            },
            {
                "internalType": "uint256",
                "name": "getamount",
                "type": "uint256"
            },
            {
                "internalType": "uint256",
                "name": "gettotalprice",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "last_sender",
        "outputs": [{
            "internalType": "address",
            "name": "",
            "type": "address"
        }],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [{
                "internalType": "string",
                "name": "_purpose",
                "type": "string"
            },
            {
                "internalType": "string",
                "name": "_subject",
                "type": "string"
            },
            {
                "internalType": "uint256",
                "name": "_price",
                "type": "uint256"
            },
            {
                "internalType": "uint256",
                "name": "_amount",
                "type": "uint256"
            },
            {
                "internalType": "uint256",
                "name": "_totalprice",
                "type": "uint256"
            }
        ],
        "name": "putResult",
        "outputs": [],
        "stateMutability": "nonpayable",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "sending",
        "outputs": [],
        "stateMutability": "payable",
        "type": "function"
    },
    {
        "inputs": [{
            "internalType": "uint256",
            "name": "",
            "type": "uint256"
        }],
        "name": "useresults",
        "outputs": [{
                "internalType": "string",
                "name": "purpose",
                "type": "string"
            },
            {
                "internalType": "string",
                "name": "subject",
                "type": "string"
            },
            {
                "internalType": "uint256",
                "name": "price",
                "type": "uint256"
            },
            {
                "internalType": "uint256",
                "name": "amount",
                "type": "uint256"
            },
            {
                "internalType": "uint256",
                "name": "totalprice",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    }
]