import { useStaticRendering } from "mobx-react";
import { GlobalStore } from "./globalStore";

const isServer = typeof window === "undefined";
useStaticRendering(isServer);

export class Store {
  globalStore: GlobalStore;

  constructor() {
    this.globalStore = new GlobalStore();
  }
}

export const rootStore = new Store();
