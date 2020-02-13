const BOX_BORDER = "1.5px solid #eaeaea";
const BORDER_RADIUS = "20px";

export default {
  maxWidth: "935px",
  maxCardWidth: "540px",
  minPageWidth: "320px",
  bgColor: "#FAFAFA",
  mainColor: "#fbae17",
  borderGrey: "#eaeaea",
  lightGrey: "#e1e1e1",
  middleGrey: "cccccc",
  brownGrey: "#999999",
  brownishGrey: "#707070",
  darkBrown: "#4f473e",
  lightGreen: "#03a87c",
  darkGreen: "#41734E",
  headerColor: "#17202E",
  black: "#2c2c2c",
  white: "#ffffff",
  boxBorder: "1px solid #eaeaea",
  borderRadius: "4px",
  whiteBox: `
    border:${BOX_BORDER};
    border-radius:${BORDER_RADIUS};
    background-color:white;
  `,
  fixedCenter: `
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  `,
  absoluteCenter: `
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  `,
  cardBox: `
    background-color: #fff;
    border: 1px solid #e1e1e1; 
  `,
  imageReplacement: `
    width: $width;
    height: $height;
    overflow: hidden;
    &::after {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      width: $width;
      height: $height;
      background: center / contain no-repeat url($url) $bgColor;
    }
  `,
  clearBoth: `
    &:after {
      content: "";
      display: block;
      clear: both;
    }
  `,
  textEllipsis: `
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  `,
  breakWord: `
    overflow: hidden;
    white-space: normal;
    word-wrap: break-word;
  `
};
