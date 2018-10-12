package me.oraclebox.service.notificationservice

import com.fasterxml.jackson.core.type.TypeReference
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MvcResult

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class EmailControllerTest extends CommonWebTest {

    static String NOTIFICATION_ID;
    @Autowired
    EmailService emailService;

    @Test
    void _01_sendEmail() {
        RequestObject requestObject = new RequestObject();
        Email email = new Email();
        email.to.add("roger.chan@example.com");
        email.subject = "Unit testing Notification service";
        email.content = new Content(template: "This is content body.");
        requestObject.emails.add(email);


        MvcResult result = POST('/service/notification/send/email', requestObject);
        assertEquals(200, result.response.status);
        ResponseObject responseObject = objectMapper.readValue(result.response.contentAsString, ResponseObject.class);
        assertNotNull(requestObject);
    }

    @Test
    void _02_createNotificationGroup() {
        NotificationGroup notificationGroup = NotificationGroup.builder()
                .name("junit_notification").build();
        MvcResult result = POST('/service/notification/group', notificationGroup);
        assertEquals(200, result.response.status);
        ResponseObject responseObject = objectMapper.readValue(result.response.contentAsString, ResponseObject.class);
        notificationGroup = objectMapper.readValue(objectMapper.writeValueAsString(responseObject.dataObject), NotificationGroup.class);
        NOTIFICATION_ID = notificationGroup.id;
        assertNotNull(notificationGroup.id);
        assertEquals('junit_notification', notificationGroup.name);
        assertNotNull(notificationGroup.organization);
        assertNotNull(notificationGroup.updateTs);
        assertNotNull(notificationGroup.createTs);
    }

    @Test
    void _03_getNotificationGroup() {
        MvcResult result = GET('/service/notification/group');
        assertEquals(200, result.response.status);
        ResponseObject responseObject = objectMapper.readValue(result.response.contentAsString, ResponseObject.class);
        List<NotificationGroup> groupList = objectMapper.readValue(objectMapper.writeValueAsString(responseObject.dataObject), new TypeReference<List<NotificationGroup>>(){});
        assertEquals(1, groupList.size());
        assertEquals('junit_notification', groupList.first().name);
        assertEquals(NOTIFICATION_ID, groupList.first().id);
    }

    @Test
    void _04_getNotificationGroupById() {
        MvcResult result = GET('/service/notification/group/'+NOTIFICATION_ID);
        assertEquals(200, result.response.status);
        ResponseObject responseObject = objectMapper.readValue(result.response.contentAsString, ResponseObject.class);
        NotificationGroup notificationGroup = objectMapper.readValue(objectMapper.writeValueAsString(responseObject.dataObject), NotificationGroup.class);
        assertEquals('junit_notification', notificationGroup.name);
        assertEquals(NOTIFICATION_ID, notificationGroup.id);
    }

    @Test
    void _05_getNotificationGroupByName() {
        NotificationGroup notificationGroup = emailService.getNotificationGroupByName('junit_notification');
        assertNotNull(notificationGroup);
        assertNotNull(notificationGroup.name);
        assertEquals(NOTIFICATION_ID, notificationGroup.id);
    }

    @Test
    void _06_putNotificationGroup() {
        //GET
        MvcResult result = GET('/service/notification/group/'+NOTIFICATION_ID);
        assertEquals(200, result.response.status);
        ResponseObject responseObject = objectMapper.readValue(result.response.contentAsString, ResponseObject.class);
        NotificationGroup notificationGroup = objectMapper.readValue(objectMapper.writeValueAsString(responseObject.dataObject), NotificationGroup.class);
        notificationGroup.recipients.addAll([new Recipient(domain: "chanro3", name: 'Roger Chan', email:"roger.chan@example.com"),
                                             new Recipient(domain: "ptlam", name: 'Peter Lam',email:"peter.lam@example.com")]);
        result = PUT('/service/notification/group', notificationGroup);
        responseObject = objectMapper.readValue(result.response.contentAsString, ResponseObject.class);
        notificationGroup = objectMapper.readValue(objectMapper.writeValueAsString(responseObject.dataObject), NotificationGroup.class);
        assertTrue(notificationGroup.recipients.size() == 2);
    }

    @Test
    void _07_deleteNotificationGroup() {
        //GET
        MvcResult result = DELETE('/service/notification/group/'+NOTIFICATION_ID);
        assertEquals(200, result.response.status);
    }

}