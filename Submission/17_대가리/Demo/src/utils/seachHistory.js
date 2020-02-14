var request = require('request')
const searchHistory = (address,callback) => {
    const url = `http://106.10.58.158:3000/v1/addresses/${address}/transactions`

    request({url,json:true},(error,body)=>{
        if(error){
            callback('error',undefined)
        }else{
            console.log(body)
            callback(undefined,body)
        }
    })
}

export default searchHistory