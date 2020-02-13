import styled from "styled-components";

const Button = styled.button`
  width: 100%;
  height: 50px;
  border: 0;
  border-radius: ${props => props.theme.borderRadius};
  color: white;
  font-weight: 600;
  background-color: ${props => props.theme.lightGreen};
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 7px 0px;
  font-size: 14px;
  cursor: pointer;
`;

export default Button;
