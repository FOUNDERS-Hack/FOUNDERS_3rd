import App from "next/app";
import React from "react";
import { Provider } from "mobx-react";
import { rootStore } from "../stores";
import NavBottomFooter from "../components/Common/NavBottomFooter";
class CustomApp extends App {
  state = {
    store: rootStore
  };

  // Fetching serialized(JSON) store state
  static async getInitialProps(appContext: any) {
    const appProps = await App.getInitialProps(appContext);

    return {
      ...appProps
    };
  }

  render() {
    const { Component, pageProps, router } = this.props;
    return (
      <Provider {...this.state.store}>
        <Component {...pageProps} asPath={router.asPath} query={router.query} />
        {/* <NavBottomFooter></NavBottomFooter> */}
      </Provider>
    );
  }
}
export default CustomApp;
