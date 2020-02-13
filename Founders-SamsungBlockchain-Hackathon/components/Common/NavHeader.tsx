import React from "react";
import Link from "next/link";
import {
  Grid,
  Menu,
} from "semantic-ui-react";
import { HEADER } from "../../constants/texts";

export class NavHeader extends React.Component {

  // handleItemClick = (
  //   _event: React.MouseEvent<HTMLAnchorElement>,
  //   { name }: { [key: string]: string }
  // ) => {
  //   this.activeItem = name;
  //   this.menuStyle.dropdownMenuStyle = { display: "none" };
  // }

  // handleToggleDropdownMenu = () => {
  //   if (this.menuStyle.dropdownMenuStyle.display === "none") {
  //     this.menuStyle.dropdownMenuStyle = { display: "flex" };
  //   } else {
  //     this.menuStyle.dropdownMenuStyle = { display: "none" };
  //   }
  // };
  render() {
    // let link: any;

    return (
      <div className="AdminPage">
        <Grid padded className="tablet computer only">
          <Menu borderless inverted fluid fixed="top">
            <Link href="/">
              <Menu.Item header as="a">
                관리자 페이지
              </Menu.Item>
            </Link>
            {HEADER.mainNavigation.map((item, i: number) => (
            <Link key={i} href={item.href} as={item.asPath}>
                <Menu.Item name={item.name} />
              </Link>
            ))}
          </Menu>
        </Grid>
      </div>
    );
  }
}



// import React from "react";
// // import Link from "next/link";
// import {
//   Button,
//   Grid,
//   Icon,
//   Menu,
// } from "semantic-ui-react";
// // import { HEADER } from "../../constants/texts";

// export class NavHeader extends React.Component {

//   // handleItemClick = (
//   //   _event: React.MouseEvent<HTMLAnchorElement>,
//   //   { name }: { [key: string]: string }
//   // ) => {
//   //   this.activeItem = name;
//   //   this.menuStyle.dropdownMenuStyle = { display: "none" };
//   // }

//   // handleToggleDropdownMenu = () => {
//   //   if (this.menuStyle.dropdownMenuStyle.display === "none") {
//   //     this.menuStyle.dropdownMenuStyle = { display: "flex" };
//   //   } else {
//   //     this.menuStyle.dropdownMenuStyle = { display: "none" };
//   //   }
//   // };

//   render() {
//     // let link: any;

//     return (
//       <div className="AdminPage">
//         <Grid padded className="tablet computer only">
//           <Menu borderless inverted fluid fixed="top">
//             <Menu.Item header as="a">
//               관리자 페이지
//             </Menu.Item>
//           </Menu>
//         </Grid>
//         <Grid padded className="mobile only">
//           <Menu borderless inverted fluid fixed="top">
//             <Menu.Item header as="a">
//               관리자 페이지
//             </Menu.Item>
//             <Menu.Menu position="right">
//               <Menu.Item>
//                 <Button
//                   basic
//                   inverted
//                   icon
//                   toggle
//                   // onClick={this.handleToggleDropdownMenu}
//                 >
//                   <Icon name="content" />
//                 </Button>
//               </Menu.Item>
//             </Menu.Menu>
//             <Menu
//               borderless
//               fluid
//               inverted
//               vertical
//               // style={{...this.menuStyle.dropdownMenuStyle}}
//             >
//               {/* {panes.map((pane: IPaneType, i: number) => (
//                 <Menu.Item
//                   key={i}
//                   name={pane.menuItem}
//                   onClick={this.handleItemClick}
//                   active={this.activeItem === pane.menuItem}
//                 >
//                   {pane.menuItem}
//                 </Menu.Item>
//               ))} */}
//             </Menu>
//           </Menu>
//         </Grid>
//         <Grid padded>
//           <Grid.Column
//             tablet={3}
//             computer={3}
//             only="tablet computer"
//             id="sidebar"
//           >
//             <Menu vertical borderless fluid text>
//               {/* {panes.map((pane: IPaneType, i: number) => (
//                   <Menu.Item
//                     key={i}
//                     name={pane.menuItem}
//                     onClick={this.handleItemClick}
//                     active={this.activeItem === pane.menuItem}
//                   >
//                     {pane.menuItem}
//                   </Menu.Item> */}
//                 ))}
//             </Menu>
//           </Grid.Column>
//           <Grid.Column
//             mobile={16}
//             tablet={13}
//             computer={13}
//             floated="right"
//             id="content"
//           >
//             {/* {panes
//             .find((pane: IPaneType) => pane.menuItem === this.activeItem)!
//             .render()} */}
//           </Grid.Column>
//         </Grid>
//       </div>
//     );
//   }
// }
