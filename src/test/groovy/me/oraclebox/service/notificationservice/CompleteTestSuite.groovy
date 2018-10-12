package me.oraclebox.service.notificationservice

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(profiles = "testing")
@RunWith(Suite.class)
@Suite.SuiteClasses([
        EmailControllerTest.class
])
class CompleteTestSuite {

}

