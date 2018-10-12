package me.oraclebox.service.notificationservice

import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import org.simplejavamail.email.Email
import org.simplejavamail.mailer.Mailer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Slf4j
@Service
class EmailService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    NotificationGroupRepository notificationGroupRepository;
    @Autowired
    Mailer mailer;

    static String bindToTemplate(String templateName, Map<String, String> binding) {
        return new SimpleTemplateEngine().createTemplate(new File(templateName).text).make(binding).toString();
    }

    void send(List<Email> emailList, boolean async) {
        emailList.each {
            email ->
                try {
                    mailer.sendMail(email, async);
                } catch (Exception e) {
                    log.error("Failed to send email ${email.subject} to ${email.recipients}", e);
                    throw e;
                }
        }
    }

    List<NotificationGroup> getNotificationGroup() {
        return mongoTemplate.findAll(NotificationGroup.class);
    }

    NotificationGroup getNotificationGroupById(String id) {
        return mongoTemplate.findById(id, NotificationGroup.class);
    }

    NotificationGroup getNotificationGroupByName(String name) {
        return mongoTemplate.find(new Query(Criteria.where("name")
                .is(name))
                , NotificationGroup.class).find { true };
    }

    NotificationGroup createNotificationGroup(NotificationGroup notificationGroup) {
        notificationGroup.createTs = new Date();
        return updateNotificationGroup(notificationGroup);
    }

    NotificationGroup updateNotificationGroup(NotificationGroup notificationGroup) {
        notificationGroup.updateTs = new Date();
        return notificationGroupRepository.save(notificationGroup);
    }

    void deleteNotificationGroup(String id) {
        mongoTemplate.remove(new Query(Criteria.where("id")
                .is(id))
                , NotificationGroup.class);
    }
}
