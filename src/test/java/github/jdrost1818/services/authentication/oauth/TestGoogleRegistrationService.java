package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.authentication.GoogleUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.GoogleUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestGoogleRegistrationService {

    @Autowired
    private GoogleRegistrationService googleRegistrationService;

    @Autowired
    private GoogleUserRepository googleUserRepository;

    private GoogleUser googleUser;

    private User user;

    private LinkedHashMap<String, Object> details;

    private OAuth2Authentication oAuth2Authentication;

    @Before
    public void setUp() {
        /*
            User Set Up
         */
        this.user = new User();
        this.user.setFirstName("FirstName");
        this.user.setLastName("LastName");
        this.user.setEmail("email@example.com");

        this.googleUser = this.googleUserRepository.save(new GoogleUser("1", this.user));

        /*
            Authentication Set Up
         */
        this.details = new LinkedHashMap<>();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null);
        authentication.setDetails(this.details);

        this.oAuth2Authentication = new OAuth2Authentication(null, authentication);
    }

    @After
    public void tearDown() {
        this.googleUserRepository.deleteAll();
    }

    @Test
    public void testNullAuthenticationArg() {
        assertNull(this.googleRegistrationService.getUser(null));
    }

    @Test
    public void testUserDoesNotExist() {
        this.details.put("id", "this ID does not exist - I guarantee it");
        assertNull(this.googleRegistrationService.getUser(this.oAuth2Authentication));
    }

    @Test
    public void testUserDoesExist() {
        this.details.put("id", this.googleUser.getId());
        assertTrue(reflectionEquals(this.googleUser, this.googleRegistrationService.getUser(this.oAuth2Authentication)));
    }

    @Test
    public void testSaveUserNullInput() {
        assertNull(this.googleRegistrationService.saveUser(null));
    }

    @Test
    public void testSaveUserNullDetails() {
        this.oAuth2Authentication.setDetails(null);
        assertNull(this.googleRegistrationService.saveUser(this.oAuth2Authentication));
    }

    @Test
    public void testSaveUserInvalidDetails() {
        this.oAuth2Authentication.setDetails(null);
        assertNull(this.googleRegistrationService.saveUser(this.oAuth2Authentication));
    }

    @Test
    public void testSaveUser() {
        Map<String, String> nameMap = new LinkedHashMap<>();
        nameMap.put("givenName", this.user.getFirstName());
        nameMap.put("familyName", this.user.getLastName());

        Map<String, String> emailMap = new LinkedHashMap<>();
        emailMap.put("value", UUID.randomUUID() + this.user.getEmail()); // emails need to be unique in the db

        this.details.put("id", UUID.randomUUID());
        this.details.put("emails", Collections.singletonList(emailMap));
        this.details.put("name", nameMap);

        // All the properties should be the same except for the id - so we'll ignore that for now
        assertTrue(reflectionEquals(this.googleUser, this.googleRegistrationService.saveUser(this.oAuth2Authentication), "id", "email"));
    }

    @Test(expected = JpaSystemException.class)
    public void testSaveEmailAlreadyUse() {
        Map<String, String> emailMap = new LinkedHashMap<>();
        emailMap.put("value", this.user.getEmail());

        this.details.put("id", UUID.randomUUID());
        this.details.put("emails", Collections.singletonList(emailMap));

        this.googleRegistrationService.saveUser(this.oAuth2Authentication);
    }

}
