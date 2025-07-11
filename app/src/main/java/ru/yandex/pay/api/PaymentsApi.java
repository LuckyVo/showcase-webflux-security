package ru.yandex.pay.api;

import ru.yandex.pay.invoker.ApiClient;

import ru.yandex.pay.dto.PaymentRequest;
import ru.yandex.pay.dto.PaymentResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.12.0")
public class PaymentsApi {
    private ApiClient apiClient;

    public PaymentsApi() {
        this(new ApiClient());
    }

    @Autowired
    public PaymentsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Process a payment by deducting amount from user&#39;s balance
     * 
     * <p><b>200</b> - Payment successful
     * <p><b>400</b> - Insufficient funds or invalid request
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param paymentRequest The paymentRequest parameter
     * @return PaymentResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec payPostRequestCreation(PaymentRequest paymentRequest) throws WebClientResponseException {
        Object postBody = paymentRequest;
        // verify the required parameter 'paymentRequest' is set
        if (paymentRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'paymentRequest' when calling payPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "bearerAuth" };

        ParameterizedTypeReference<PaymentResponse> localVarReturnType = new ParameterizedTypeReference<PaymentResponse>() {};
        return apiClient.invokeAPI("/pay", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Process a payment by deducting amount from user&#39;s balance
     * 
     * <p><b>200</b> - Payment successful
     * <p><b>400</b> - Insufficient funds or invalid request
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param paymentRequest The paymentRequest parameter
     * @return PaymentResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<PaymentResponse> payPost(PaymentRequest paymentRequest) throws WebClientResponseException {
        ParameterizedTypeReference<PaymentResponse> localVarReturnType = new ParameterizedTypeReference<PaymentResponse>() {};
        return payPostRequestCreation(paymentRequest).bodyToMono(localVarReturnType);
    }

    /**
     * Process a payment by deducting amount from user&#39;s balance
     * 
     * <p><b>200</b> - Payment successful
     * <p><b>400</b> - Insufficient funds or invalid request
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param paymentRequest The paymentRequest parameter
     * @return ResponseEntity&lt;PaymentResponse&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<PaymentResponse>> payPostWithHttpInfo(PaymentRequest paymentRequest) throws WebClientResponseException {
        ParameterizedTypeReference<PaymentResponse> localVarReturnType = new ParameterizedTypeReference<PaymentResponse>() {};
        return payPostRequestCreation(paymentRequest).toEntity(localVarReturnType);
    }

    /**
     * Process a payment by deducting amount from user&#39;s balance
     * 
     * <p><b>200</b> - Payment successful
     * <p><b>400</b> - Insufficient funds or invalid request
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param paymentRequest The paymentRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec payPostWithResponseSpec(PaymentRequest paymentRequest) throws WebClientResponseException {
        return payPostRequestCreation(paymentRequest);
    }
}
