package com.cargosmart.b2b.notificationservice

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

// DAO
@Repository
interface NotificationGroupRepository extends MongoRepository<NotificationGroup, String> {


}
