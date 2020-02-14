import React from 'react'
import './header.scss';
// import 'semantic-ui-css/semantic.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import { Link } from "react-router-dom";

class Header extends React.Component{
    constructor(props){
        super(props)
        this.state = {
            curPage: 'main'
        }
    }

    pageMove = (event, str) => {
        this.setState({
            curPage : str
        })
    }
    render(){
        return (<>
            <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark" >
            <Navbar.Brand href="#home">DuckChain</Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse id="responsive-navbar-nav">
                <Nav className="mr-auto">
                <Link to={"/"} style={{color : "white" ,padding : "10px"}}>Home</Link>
                <Link to={"/profile"} style={{color : "white" ,padding : "10px"}}>Profile</Link>
                <Link to={"/SmartContract"} style={{color : "white" ,padding : "10px"}} >SmartContract</Link>
                </Nav>
                <Nav>
                <Nav.Link href="#deets">More deets</Nav.Link>
                <Nav.Link eventKey={2} href="#memes">
                    Dank memes
                </Nav.Link>
                </Nav>
            </Navbar.Collapse>
            </Navbar>
        </>
        )
    }
}
export default Header