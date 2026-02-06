package com.hutech.bai8.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hutech.bai8.config.MoMoConfig;
import com.hutech.bai8.model.momo.MoMoRequest;
import com.hutech.bai8.model.momo.MoMoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
// import java.net.URLEncoder; // Không cần URL encode cho signature MoMo
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoMoService {
    private final MoMoConfig momoConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createPaymentRequest(String orderId, Long amount, String orderInfo) throws Exception {
        String requestId = UUID.randomUUID().toString();
        
        // Ensure orderInfo is clean - remove special characters that might cause issues
        orderInfo = cleanOrderInfo(orderInfo);
        
        MoMoRequest request = new MoMoRequest();
        request.setPartnerCode(momoConfig.getPartnerCode());
        request.setAccessKey(momoConfig.getAccessKey());
        request.setRequestId(requestId);
        request.setAmount(String.valueOf(amount));
        request.setOrderId(orderId);
        request.setOrderInfo(orderInfo);
        request.setRedirectUrl(momoConfig.getRedirectUrl());
        request.setIpnUrl(momoConfig.getIpnUrl());
        request.setRequestType("captureWallet");

        // Tạo signature
        String signature = buildSignature(request);
        request.setSignature(signature);

        // Log để debug
        log.info("MoMo Request - PartnerCode: {}, OrderId: {}, Amount: {}, OrderInfo: {}", 
                request.getPartnerCode(), request.getOrderId(), request.getAmount(), request.getOrderInfo());
        log.info("MoMo Request - RedirectUrl: {}, IpnUrl: {}", request.getRedirectUrl(), request.getIpnUrl());
        log.info("MoMo Request - Signature: {}", signature);

        // Gửi request đến MoMo API
        String response = sendRequest(momoConfig.getApiUrl(), request);
        log.info("MoMo Response: {}", response);
        
        MoMoResponse momoResponse = objectMapper.readValue(response, MoMoResponse.class);

        // Kiểm tra cả errorCode (v1) và resultCode (v2)
        String code = (momoResponse.getResultCode() != null) ? momoResponse.getResultCode() : momoResponse.getErrorCode();
        String message = (momoResponse.getResultMessage() != null) ? momoResponse.getResultMessage() : momoResponse.getMessage();
        
        if ("0".equals(code)) {
            return momoResponse.getPayUrl();
        } else {
            log.error("MoMo payment request failed - Code: {}, Message: {}", code, message);
            throw new Exception("Không thể tạo yêu cầu thanh toán MoMo: " + message);
        }
    }

    private String cleanOrderInfo(String orderInfo) {
        // Remove characters that might cause encoding issues
        if (orderInfo == null) return "Thanh toan don hang";
        // Keep only ASCII letters, digits, spaces, and common punctuation
        return orderInfo.replaceAll("[^\\x00-\\x7F]", "").trim();
    }

    private String buildSignature(MoMoRequest request) throws Exception {
        // MoMo yêu cầu signature được tính từ giá trị GỐC (KHÔNG URL encode)
        String accessKey = request.getAccessKey();
        String amount = request.getAmount();
        String extraData = request.getExtraData() != null ? request.getExtraData() : "";
        String ipnUrl = request.getIpnUrl();
        String orderId = request.getOrderId();
        String orderInfo = request.getOrderInfo();
        String partnerCode = request.getPartnerCode();
        String redirectUrl = request.getRedirectUrl();
        String requestId = request.getRequestId();
        String requestType = request.getRequestType();

        // Theo MoMo API v2 format - KHÔNG URL encoding
        String rawData = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        log.debug("Raw signature data: {}", rawData);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(momoConfig.getSecretKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String signature = bytesToHex(sha256_HMAC.doFinal(rawData.getBytes(StandardCharsets.UTF_8)));
        log.debug("Generated signature: {}", signature);
        return signature;
    }

    // Method urlEncode đã bị xóa vì MoMo yêu cầu dữ liệu gốc (không URL encode) để tạo signature

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() ==1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String sendRequest(String urlString, MoMoRequest request) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        String jsonRequest = objectMapper.writeValueAsString(request);
        log.debug("Sending JSON: {}", jsonRequest);
        
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
            wr.flush();
        }

        int responseCode = conn.getResponseCode();
        log.info("MoMo API Response Code: {}", responseCode);

        StringBuilder response = new StringBuilder();
        
        // Đọc response từ error stream nếu có lỗi
        if (responseCode >= 400) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } else {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        }

        return response.toString();
    }

    /**
     * Verify IPN/Return signature theo MoMo API v2
     * Format: accessKey=$accessKey&amount=$amount&extraData=$extraData&message=$message
     *         &orderId=$orderId&orderInfo=$orderInfo&orderType=$orderType&partnerCode=$partnerCode
     *         &payType=$payType&requestId=$requestId&responseTime=$responseTime
     *         &resultCode=$resultCode&transId=$transId
     */
    public boolean verifyIpnSignature(String signature, String amount, String extraData, String message,
                                      String orderId, String orderInfo, String partnerCode,
                                      String payType, String requestId, String orderType,
                                      String responseTime, String transId, String resultCode) throws Exception {
        
        // Format đúng theo MoMo API v2 - theo thứ tự alphabet
        String rawData = "accessKey=" + momoConfig.getAccessKey() +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&message=" + message +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&orderType=" + orderType +
                "&partnerCode=" + partnerCode +
                "&payType=" + payType +
                "&requestId=" + requestId +
                "&responseTime=" + responseTime +
                "&resultCode=" + resultCode +
                "&transId=" + transId;

        log.debug("IPN Raw signature data: {}", rawData);
        
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(momoConfig.getSecretKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String calculatedSignature = bytesToHex(sha256_HMAC.doFinal(rawData.getBytes(StandardCharsets.UTF_8)));
        log.debug("IPN Signature - Received: {}, Calculated: {}", signature, calculatedSignature);
        return calculatedSignature.equals(signature);
    }
}
