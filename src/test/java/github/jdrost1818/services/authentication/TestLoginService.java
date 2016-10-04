package github.jdrost1818.services.authentication;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.authentication.SessionUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.FacebookUserRepository;
import github.jdrost1818.repository.authentication.GoogleUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.LinkedHashMap;
import java.util.UUID;

import static junit.framework.TestCase.*;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestLoginService {

    @Value("${google.client.clientId}")
    private String googleClientId;

    @Value("${facebook.client.clientId}")
    private String facebookClientId;

    @Autowired
    private LoginService loginService;

    @Autowired
    private GoogleUserRepository googleUserRepository;

    @Autowired
    private FacebookUserRepository facebookUserRepository;

    @Autowired
    private SessionUser sessionUser;

    private LinkedHashMap<String, Object> details;

    private OAuth2Authentication googleAuthentication;

    private OAuth2Authentication facebookAuthentication;

    @Before
    public void setup() {
        /*
            Authentication Set Up
         */
        this.details = new LinkedHashMap<>();
        this.details.put("id", UUID.randomUUID().toString());

        OAuth2Request googleRequest = new OAuth2Request(null, this.googleClientId, null, true, null, null, null, null, null);
        OAuth2Request facebookRequest = new OAuth2Request(null, this.facebookClientId, null, true, null, null, null, null, null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null);
        authentication.setDetails(this.details);

        this.googleAuthentication = new OAuth2Authentication(googleRequest, authentication);
        this.facebookAuthentication = new OAuth2Authentication(facebookRequest, authentication);
    }

    @After
    public void cleanup() {
        this.googleUserRepository.deleteAll();
        this.facebookUserRepository.deleteAll();
    }

    @Test
    public void testLoadNullRequest() {
        assertNull(this.loginService.loadUser(null));
    }

    @Test
    public void testSaveNullRequest() {
        assertNull(this.loginService.saveUser(null));
    }

    @Test
    public void testUserGetsSavedToGoogleRepo() {
        assertFalse(this.googleUserRepository.findAll().iterator().hasNext());

        User user = this.loginService.saveUser(this.googleAuthentication);

        assertNotNull(user);
        assertTrue(this.googleUserRepository.findAll().iterator().hasNext());
    }

    @Test
    public void testUserGetsSavedToFacebookRepo() {
        assertFalse(this.facebookUserRepository.findAll().iterator().hasNext());

        User user = this.loginService.saveUser(this.facebookAuthentication);

        assertNotNull(user);
        assertTrue(this.facebookUserRepository.findAll().iterator().hasNext());
    }

    @Test
    public void testUserGetsLoadedFromGoogleRepo() {
        User userSaved = this.loginService.saveUser(this.googleAuthentication);

        reflectionEquals(userSaved, this.loginService.loadUser(this.googleAuthentication));
    }

    @Test
    public void testUserGetsLoadedFromFacebookRepo() {
        User userSaved = this.loginService.saveUser(this.facebookAuthentication);

        reflectionEquals(userSaved, this.loginService.loadUser(this.facebookAuthentication));
    }

    @Test
    public void testGetUserUserIsLoggedIn() {
        User loggedInUser = new User();

        this.sessionUser.setCurrentUser(loggedInUser);
        assertEquals(loggedInUser, this.loginService.getUserSaveIfNotPresent(null));
    }

    @Test
    public void testUserNotLoggedInNullInput() {
        assertNull(this.loginService.getUserSaveIfNotPresent(null));
    }

    @Test
    public void testUserNotLoggedInOrSaved() {
        User loggedInUser = this.loginService.getUserSaveIfNotPresent(this.googleAuthentication);

        assertNotNull(loggedInUser);
        assertTrue(this.googleUserRepository.findAll().iterator().hasNext());
    }

}
