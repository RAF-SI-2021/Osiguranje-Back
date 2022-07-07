package crudApp.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crudApp.dto.PasswordDto;
import crudApp.dto.UserDto;
import crudApp.model.Permissions;
import crudApp.model.User;
import crudApp.repositories.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class IntegrationTests{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    @Test
    public void test1() throws Exception{
        ResultActions resultActions = mockMvc.perform(get("/api/users/all")).andExpect(status().isOk());

        String jsonstr = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(jsonstr);
        List userList = objectMapper.readValue(jsonstr, List.class);
        assertThat(userList.size() == 2);
        assertThat(userList.get(0) instanceof UserDto);
    }

    @Test
    public void test2() throws Exception {
        String userEmail = "test@test.com";
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPosition("ADMIN");
        user.setEmail(userEmail);
        user.setActive(true);
        user.setPhoneNumber("+13456");
        user.setJMBG("12345");
        user.setPassword("pwd");
        user.setPermissions(new Permissions(false,true,true,true,false,true,false));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Optional<User> userOpt = repository.findUserByEmail(userEmail);
        assertThat(userOpt.isPresent());
        User realUser = userOpt.get();
        assertThat(realUser.getActive());
        assertThat(realUser.getPhoneNumber().equals("+13456"));
        System.out.println(realUser);

        userOpt = repository.findUserByEmail("yuuy@gmail.com");
        assertThat(userOpt.isEmpty());
    }

    @Test
    public void test3() throws Exception {

        String userEmail = "test3@test.com";
        User user = new User();
            user.setFirstName("Test");
        user.setLastName("Test");
        user.setPosition("ADMIN");
        user.setEmail(userEmail);
        user.setActive(true);
        user.setPhoneNumber("+13456");
        user.setJMBG("12346");
        user.setPassword("pwd");
        user.setPermissions(new Permissions(false,true,true,true,false,false,true));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        System.out.println(user.getJMBG());
        user.setPhoneNumber("+09876");
        user.setFirstName("Kontrola");

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Optional<User> userOptional = repository.findUserByEmail(userEmail);

        assertThat(userOptional.isPresent());

        user = userOptional.get();
        assertThat(user.getFirstName().equals("Kontrola"));
        assertThat(user.getPhoneNumber().equals("+09876"));

    }

    @Test
    public void test4() throws Exception {
        String userEmail = "test4@test.com";
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPosition("TESTERA");
        user.setEmail(userEmail);
        user.setActive(true);
        user.setPhoneNumber("+13456");
        user.setJMBG("123466");
        user.setPassword("pwd");
        user.setPermissions(new Permissions(false,true,true,true,false,false,true));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());


        user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPosition("TESTERA");
        user.setEmail("test41@");
        user.setActive(true);
        user.setPhoneNumber("+13456");
        user.setJMBG("123467");
        user.setPassword("pwd");
        user.setPermissions(new Permissions(false,true,true,true,true,false,false));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPosition("TESTERA");
        user.setEmail("test47@");
        user.setActive(true);
        user.setPhoneNumber("+13456");
        user.setJMBG("123468");
        user.setPassword("pwd");
        user.setPermissions(new Permissions(false,true,true,true,false,true,false));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/users/search/position?position=TESTERA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());


        String jsonstr = resultActions.andReturn().getResponse().getContentAsString();
        List userList = objectMapper.readValue(jsonstr, List.class);
        assertThat(userList.size() == 3);
        assertThat(userList.get(0) instanceof UserDto);
    }

    @Test
    public void test5() throws Exception {

        ResultActions resultActions0 = mockMvc.perform(get("/api/users/all")).andExpect(status().isOk());

        String jsonstrr = resultActions0.andReturn().getResponse().getContentAsString();
        System.out.println(jsonstrr);

        ResultActions resultActions = mockMvc.perform(get("/api/users/1")).andExpect(status().isOk());

        String jsonstr = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(jsonstr);
        UserDto user = objectMapper.readValue(jsonstr, UserDto.class);
        assertThat(user.getActive());
        assertThat(user.getEmail().equals("car@gmail.com"));
    }

    @Test
    public void test6() throws Exception {
        String userEmail = "test77@test.com";
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPosition("ADMIN");
        user.setEmail(userEmail);
        user.setActive(true);
        user.setPhoneNumber("+13456");
        user.setJMBG("126545");
        user.setPassword("pwd");
        user.setPermissions(new Permissions(false,true,true,true,false,false,true));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Optional<User> userOpt = repository.findUserByEmail(userEmail);
        assertThat(userOpt.isPresent());


        PasswordDto passwordDto = new PasswordDto(userOpt.get().getId(),"pwd","juju");
        mockMvc.perform(put("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isNoContent());

        userOpt = repository.findUserByEmail(userEmail);

        assertThat(userOpt.isPresent());
        user = userOpt.get();
        assertThat(user.getPassword().equals("juju"));
    }

}
