import React from "react";

class Footer extends React.Component {
    
    render(){
        var style = {

            textAlign: "center",
            padding: "20px",
            position: "fixed",
            left: "0",
            bottom: "0",
            height: "10%",
            width: "10%",
        }
        return (
            <>
               <div style = {style}>
                    <img src="https://scontent-ssn1-1.xx.fbcdn.net/v/t1.0-9/86480850_1380587095445432_693367056271147008_n.jpg?_nc_cat=110&_nc_ohc=_ivoCL7YBuQAX8cOCk5&_nc_ht=scontent-ssn1-1.xx&oh=b3054e05ce7e385f0649f944541028fa&oe=5EBCD5AC"></img>
                </div> 
            </>
        )
    }
}

export default Footer