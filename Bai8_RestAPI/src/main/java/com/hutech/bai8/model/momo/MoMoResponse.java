package com.hutech.bai8.model.momo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Bỏ qua các field không biết từ MoMo
public class MoMoResponse {
    @JsonProperty("partnerCode")
    private String partnerCode;
    
    @JsonProperty("accessKey")
    private String accessKey;
    
    @JsonProperty("requestId")
    private String requestId;
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("errorCode")
    private String errorCode;
    
    @JsonProperty("resultMessage")
    private String resultMessage;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("responseTime")
    private String responseTime;
    
    @JsonProperty("signature")
    private String signature;
    
    @JsonProperty("amount")
    private String amount;
    
    @JsonProperty("payUrl")
    private String payUrl;
    
    @JsonProperty("transId")
    private String transId;
    
    @JsonProperty("resultCode")
    private String resultCode;
    
    // Các field mới từ MoMo API v2
    @JsonProperty("deeplink")
    private String deeplink;
    
    @JsonProperty("qrCodeUrl")
    private String qrCodeUrl;
    
    @JsonProperty("applink")
    private String applink;
    
    @JsonProperty("deeplinkMiniApp")
    private String deeplinkMiniApp;
}
