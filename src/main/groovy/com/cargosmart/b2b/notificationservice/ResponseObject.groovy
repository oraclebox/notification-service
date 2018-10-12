package com.cargosmart.b2b.notificationservice

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseObject {
    String jwtToken;
    String systemMessage;
    Object dataObject;
}
