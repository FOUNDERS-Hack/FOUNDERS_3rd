import React from 'react'
import '../App.css'
import { Link } from "react-router-dom";

class HeaderS extends React.Component{
    render(){
      const style = {
        position : "fixed",
        // display : "flex",
        width : "100%",
        backgroundColor : "#666666",
        zIndex: "1",
        height: "auto",
        color : "white",
        padding: "10px",
        margin : "1px"
      }
        return (
            <>      <div style = {style}> ASD
            {/* <div className="menuIcon">
              <div className="dashTop"></div>
              <div className="dashBottom"></div>
              <div className="circle"></div>
            </div>
    
            <span className="title">
              {this.props.title}
            </span>
    
            <input
              type="text"
              className="searchInput"
              placeholder="Search ..." />
    
            <div className="fa fa-search searchIcon"></div> */}
          </div>
        )
            </>
        )
    }
}

export default HeaderS