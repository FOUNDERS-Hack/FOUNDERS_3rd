import { createGlobalStyle } from "../../node_modules/styled-components";
import reset from "../../node_modules/styled-reset";

export default createGlobalStyle`
    ${reset};
    @import url('https://fonts.googleapis.com/css?family=Open+Sans:400,600,700');
    @import url('https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap');
    * {
        box-sizing:border-box;
    }
    body {
        background-color:${props => props.theme.bgColor};
        color:${props => props.theme.blackColor};
        font-size:14px;
        font-family:-apple-system, 'Noto Sans KR', sans-serif, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;   
        margin-top: 60px;
    }
    a {
        text-decoration:none;
        color: black;
    }
    input:focus{
        outline:none;
    }
    &::-webkit-scrollbar {
        display: none !important;
    }
`;
