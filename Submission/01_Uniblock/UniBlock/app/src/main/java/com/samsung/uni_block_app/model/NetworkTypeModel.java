package com.samsung.uni_block_app.model;

import com.samsung.android.sdk.blockchain.network.NetworkType;

public class NetworkTypeModel extends SetupModel {
    private NetworkType networkType;

    public NetworkTypeModel(int networkId, String networkName, int networkImageId, NetworkType networkType) {
        super(networkId, networkName, networkImageId);
        this.networkType = networkType;
    }

    /*Type casting needed since cannot pass NetworkType object from Activity to Activity
    since it an interface and not a concrete class.
    Look up Network Selection Activity: launchActivity : dataBundle.putSerializable*/

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }
}
