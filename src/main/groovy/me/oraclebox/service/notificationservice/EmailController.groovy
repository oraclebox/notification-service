package me.oraclebox.service.notificationservice

import groovy.util.logging.Slf4j
import org.simplejavamail.email.Email
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.email.EmailPopulatingBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@RequestMapping("/service/notification/")
class EmailController {

    @Autowired
    EmailProperty emailProperty;
    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/send/email", method = RequestMethod.POST)
    ResponseEntity<ResponseObject> sendEmail(@RequestBody RequestObject requestObject) {
        ResponseObject ro = new ResponseObject(systemMessage: "Emails have been sent successfully.");
        Assert.notNull(requestObject.emails, "Missing field emails.");
        Assert.isTrue(requestObject.emails.size() > 0, "At least one email in list emails.");

        List<Email> emailList = [];
        requestObject.emails.each {
            Assert.notNull(it, "Missing content body.");
            if (it.group == null) {
                Assert.notNull(it.to, "Missing to address list.");
                Assert.isTrue(it.to.size() > 0, "At least one To address.");
            }
            Assert.notNull(it.subject, "Missing email subject.");
            Assert.notNull(it.content, "Missing email content.");
            Assert.notNull(it.content.template, "Missing email content.template.");

            EmailPopulatingBuilder emailBuilder = EmailBuilder.startingBlank()
                    .from(it.from == null ? emailProperty.noreply : it.from)
                    .withSubject(it.subject);

            // Use notification group if provide group name
            if (it.group != null) {
                NotificationGroup notificationGroup = emailService.getNotificationGroupByName(it.group);
                Assert.notNull(notificationGroup, "Cannot find notiication group by group=${it.group}.");
                Assert.isTrue(notificationGroup.recipients.size() == 0, "Missing recipients in notification group.");
                notificationGroup.recipients.each {
                    if(!it.isCC)
                        emailBuilder.to(it.name, it.email);
                    else
                        emailBuilder.cc(it.name, it.email);
                }
            } else {
                emailBuilder.toMultiple(it.to);
                if (it.cc.size() > 0) {
                    emailBuilder.ccMultiple(it.cc.toListString());
                }
                if (it.bcc.size() > 0) {
                    emailBuilder.bccMultiple(it.bcc.toListString());
                }
            }

            // Prepare email content
            if (it.content.plainText) {
                if (it.content.bindingValues.size() > 0) {
                    emailBuilder.withPlainText(EmailService.bindToTemplate(it.content.template, it.content.bindingValues));
                } else {
                    emailBuilder.withPlainText(it.content.template);
                }
            }
            // Build email object
            emailList.add(emailBuilder.buildEmail());
        }

        emailService.send(emailList, requestObject.async);
        return new ResponseEntity<ResponseObject>(ro, HttpStatus.OK);
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    ResponseEntity<ResponseObject> getNotificationGroup() {
        ResponseObject responseObject = new ResponseObject(systemMessage: "List of notification group.");
        responseObject.dataObject = emailService.getNotificationGroup();
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET)
    ResponseEntity<ResponseObject> getNotificationGroupById(@PathVariable String id) {
        ResponseObject responseObject = new ResponseObject(systemMessage: "Notification group");
        responseObject.dataObject = emailService.getNotificationGroupById(id);
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    ResponseEntity<ResponseObject> createNotificationGroup(@RequestBody NotificationGroup notificationGroup) {
        ResponseObject responseObject = new ResponseObject(systemMessage: "Group has been created.");
        Assert.notNull(notificationGroup, "Missing content body.");
        Assert.notNull(notificationGroup.name, "Missing notification group name.");
        if (notificationGroup.organization == null) {
            notificationGroup.organization = emailProperty.defaultOrganization;
        }
        responseObject.dataObject = emailService.createNotificationGroup(notificationGroup);
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = "/group", method = RequestMethod.PUT)
    ResponseEntity<ResponseObject> updateNotificationGroup(@RequestBody NotificationGroup notificationGroup) {
        ResponseObject responseObject = new ResponseObject(systemMessage: "Group has been updated.");
        Assert.notNull(notificationGroup, "Missing content body.");
        Assert.notNull(notificationGroup.id, "Missing notification group id.");
        Assert.notNull(notificationGroup.name, "Missing notification group name.");
        Assert.notNull(notificationGroup.organization, "Missing notification group organization.");
        responseObject.dataObject = emailService.updateNotificationGroup(notificationGroup);
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseObject> deleteNotificationGroup(@PathVariable String id) {
        ResponseObject responseObject = new ResponseObject(systemMessage: "Group has been deleted.");
        Assert.notNull(id, "Missing notification group id.");
        emailService.deleteNotificationGroup(id);
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }


}
