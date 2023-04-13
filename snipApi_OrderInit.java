@GetMapping("/api/v1/3ds2/test/init-pre-order")
    @ResponseBody
    public ResponseEntity preOrder3dTest() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("audience", "https://api.payments.auspost.com.au");
        RestTemplate template = new RestTemplate();
        // call token
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers2.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeBase64String("0oaxb9i8P9vQdXTsn3l5:0aBsGU3x1bc-UIF_vDBA2JzjpCPHjoCP7oI6jisp".getBytes()));
        HttpEntity httpEntity2 = new HttpEntity(map, headers2);
        ResponseEntity entityAuth = template.exchange(
                "https://welcome.api2.sandbox.auspost.com.au/oauth/token",
                HttpMethod.POST,
                httpEntity2,
                Map.class
        );
        Map authResult = (Map) entityAuth.getBody();

        // call pre order
        InitiatePaymentRequest initiatePaymentRequest = InitiatePaymentRequest.builder()
                .merchantCode("5AR0055")
                .ip(request.getRemoteAddr())
                .amount(100)
                .orderReference("chungSleep")
                .orderType("THREED_SECURE")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authResult.get("access_token"));
        HttpEntity<InitiatePaymentRequest> httpEntity = new HttpEntity<>(initiatePaymentRequest, headers);

        return template.exchange(
                "https://payments-stest.npe.auspost.zone/v2/payments/orders/initiate",
                HttpMethod.POST,
                httpEntity,
                Map.class
        );
    }
