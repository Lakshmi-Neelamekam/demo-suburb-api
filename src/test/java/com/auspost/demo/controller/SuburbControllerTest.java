package com.auspost.demo.controller;

import com.auspost.demo.DemoApplication;
import com.auspost.demo.model.SuburbDetails;
import com.auspost.demo.service.SuburbService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest(classes = DemoApplication.class)
public class SuburbControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private MockMvc mockMvc;
    @MockBean
    private SuburbService service;
    ObjectMapper objectMapper;
    private static final Integer POST_CODE = 3000;
    private static final String NAME = "Melbourne";
    private SuburbDetails suburbDetails;
    private SuburbDetails newSuburbDetails;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        objectMapper = new ObjectMapper();
        suburbDetails = new SuburbDetails(NAME, POST_CODE, "VIC", "new");
    }

    @Test
    public void getSuburbByPostCode200WithValidAccessToken() throws Exception {
        when(service.getSuburbByPostCode(POST_CODE)).thenReturn(suburbDetails);
        String accessToken = fetchAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
        .queryParam("postCode", POST_CODE.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service, times(1)).getSuburbByPostCode(any());
    }

    @Test
    public void getSuburbByPostCode401WithoutAccessToken() throws Exception {
        when(service.getSuburbByPostCode(POST_CODE)).thenReturn(suburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("postCode", POST_CODE.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(service, times(0)).getSuburbByPostCode(any());
    }

    @Test
    public void getSuburbByPostCode404ForNoSuburb() throws Exception {
        when(service.getSuburbByPostCode(POST_CODE)).thenReturn(null);
        String accessToken = fetchAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("postCode", POST_CODE.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getSuburbByPostCode(any());
    }


    @Test
    public void getSuburbByPostCode401WithInvalidAccessToken() throws Exception {
        when(service.getSuburbByPostCode(POST_CODE)).thenReturn(suburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("postCode", POST_CODE.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer someInvalidToken")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(service, times(0)).getSuburbByPostCode(any());
    }

    @Test
    public void getSuburbByName200WithValidAccessToken() throws Exception {
        when(service.getSuburbByName(NAME)).thenReturn(suburbDetails);
        String accessToken = fetchAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("name", NAME)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service, times(1)).getSuburbByName(any());
    }

    @Test
    public void getSuburbByName401WithoutAccessToken() throws Exception {
        when(service.getSuburbByName(NAME)).thenReturn(suburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("name", NAME)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(service, times(0)).getSuburbByName(any());
    }

    @Test
    public void getSuburbByName401WithInvalidAccessToken() throws Exception {
        when(service.getSuburbByName(NAME)).thenReturn(suburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("name", NAME)
                .header(HttpHeaders.AUTHORIZATION, "Bearer someRandomToken")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(service, times(0)).getSuburbByName(any());
    }

    @Test
    public void getSuburbByName404ForNoSuburb() throws Exception {
        when(service.getSuburbByName(NAME)).thenReturn(null);
        String accessToken = fetchAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/suburbs")
                .queryParam("name", NAME)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getSuburbByName(any());
    }

    @Test
    public void addSuburb201WithValidAccessToken() throws Exception {
        newSuburbDetails = new SuburbDetails(NAME+"-saved", POST_CODE, "", "");
        String accessToken = fetchAccessToken();
        String payload = objectMapper.writeValueAsString(suburbDetails);
        when(service.addSuburb(suburbDetails)).thenReturn(newSuburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/suburbs")
                .content(payload)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(service, times(1)).addSuburb(any());
    }

    @Test
    public void addSuburb401WithoutAccessToken() throws Exception {
        newSuburbDetails = new SuburbDetails(NAME+"-saved", POST_CODE, "", "");
        String payload = objectMapper.writeValueAsString(suburbDetails);
        when(service.addSuburb(suburbDetails)).thenReturn(newSuburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/suburbs")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(service, times(0)).addSuburb(any());
    }

    @Test
    public void addSuburb401WithInvalidAccessToken() throws Exception {
        newSuburbDetails = new SuburbDetails(NAME+"-saved", POST_CODE, "", "");
        String payload = objectMapper.writeValueAsString(suburbDetails);
        when(service.addSuburb(suburbDetails)).thenReturn(newSuburbDetails);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/suburbs")
                .content(payload)
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(service, times(0)).addSuburb(any());
    }

    private String fetchAccessToken() throws Exception {
        String tokenResponse = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("username", "user")
                .param("password", "secret")
                .param("grant_type", "password")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("demoId:pwkaldnsm765".getBytes(Charset.forName(UTF_8))))
        ).andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(tokenResponse).get("access_token").toString();
    }
}
