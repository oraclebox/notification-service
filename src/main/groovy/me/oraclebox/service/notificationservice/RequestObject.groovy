package me.oraclebox.service.notificationservice

import groovy.transform.ToString
import groovy.transform.builder.Builder

@ToString
@Builder
class RequestObject {
    List<Email> emails = [];
    boolean async;
}

@ToString
@Builder
class Email{
    String from;
    String group;
    List<String> to = [];
    List<String> cc = [];
    List<String> bcc = [];
    String subject;
    Content content;
}

@ToString
@Builder
class Content{
    String template;
    Map<String, String> bindingValues = [:];
    boolean plainText = true;
}
