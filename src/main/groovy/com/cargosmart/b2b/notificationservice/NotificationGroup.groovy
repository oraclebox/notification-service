package com.cargosmart.b2b.notificationservice

import groovy.transform.builder.Builder
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Builder
@Document(collection = "notification_group")
class NotificationGroup {
    @Id
    String id;
    @Indexed(unique=true)
    String name;
    // User list in domain name
    List<Recipient> recipients = [];
    // Organization this group belong to
    String organization;
    Date createTs;
    Date updateTs;
}

class Recipient{
    String domain;
    String name;
    String email;
    boolean isCC;
}