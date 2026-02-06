package com.hutech.bai8.model.momo;

import lombok.Data;

@Data
public class MoMoRequest {
    private String partnerCode;
    private String accessKey;
    private String requestId;
    private String amount;
    private String orderId;
    private String orderInfo;
    private String redirectUrl;
    private String ipnUrl;
    private String extraData = "";
    private String requestType = "captureWallet";
    private String signature;
    private String lang = "vi";
}
