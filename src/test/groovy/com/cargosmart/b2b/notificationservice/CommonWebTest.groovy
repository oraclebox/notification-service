package com.cargosmart.b2b.notificationservice

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import javax.annotation.Resource

@ActiveProfiles(profiles = "local")
@RunWith(SpringRunner.class)
@SpringBootTest
class CommonWebTest extends Assert {


    @Autowired
    ObjectMapper objectMapper;

    @Resource
    private WebApplicationContext webApplicationContext;

    //mvc 环境对象
    protected MockMvc mockMvc;

    @Before
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    MvcResult POST(String path, Object body) {
        return mockMvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))).andReturn();
    }

    MvcResult PUT(String path, Object body) {
        return mockMvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))).andReturn();
    }

    MvcResult GET(String path) {
        return mockMvc.perform(MockMvcRequestBuilders.get(path)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
    }

    MvcResult DELETE(String path) {
        return mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
    }

}
