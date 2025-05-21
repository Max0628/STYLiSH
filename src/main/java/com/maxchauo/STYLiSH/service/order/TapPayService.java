package com.maxchauo.STYLiSH.service.order;

import com.maxchauo.STYLiSH.dto.product.dto.order.TapPayResponseDto;
import com.maxchauo.STYLiSH.dto.product.form.order.TapPayRequestForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TapPayService {
  private final RestTemplate restTemplate;

  @Value("${tappay.partner-key}")
  private String partnerKey;

  @Value("${tappay.merchant-id}")
  private String merchantId;

  @Value("${tappay.api-url}")
  private String apiUrl;

  public TapPayService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public TapPayResponseDto executePayment(String prime, int amount, String details, String email) {
    // establish request body
    TapPayRequestForm request = new TapPayRequestForm();
    request.setPrime(prime);
    request.setPartnerKey(partnerKey);
    request.setMerchantId(merchantId);
    request.setAmount(amount);
    request.setDetails(details);

    TapPayRequestForm.Cardholder cardholder = new TapPayRequestForm.Cardholder();
    cardholder.setEmail(email);
    cardholder.setPhoneNumber("");
    cardholder.setName("");
    cardholder.setZipCode("");
    cardholder.setAddress("");
    request.setCardholder(cardholder);

    // establish headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-api-key", partnerKey);

    HttpEntity<TapPayRequestForm> httpEntity = new HttpEntity<>(request, headers);

    // send request
    ResponseEntity<TapPayResponseDto> response =
        restTemplate.exchange(
            apiUrl + "/tpc/payment/pay-by-prime",
            HttpMethod.POST,
            httpEntity,
            TapPayResponseDto.class);

    // check response status
    TapPayResponseDto responseBody = response.getBody();
    return responseBody;
  }
}
