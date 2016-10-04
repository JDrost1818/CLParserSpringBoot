package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.authentication.FacebookUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.FacebookUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.LinkedHashMap;
import java.util.UUID;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestFacebookRegistrationService {

    @Autowired
    private FacebookRegistrationService facebookRegistrationService;

    @Autowired
    private FacebookUserRepository facebookUserRepository;

    private FacebookUser facebookUser;

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

        this.facebookUser = this.facebookUserRepository.save(new FacebookUser("1", this.user));

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
        this.facebookUserRepository.deleteAll();
    }

    @Test
    public void testNullAuthenticationArg() {
        assertNull(this.facebookRegistrationService.getUser(null));
    }

    @Test
    public void testUserDoesNotExist() {
        this.details.put("id", "this ID does not exist - I guarantee it");
        assertNull(this.facebookRegistrationService.getUser(this.oAuth2Authentication));
    }

    @Test
    public void testUserDoesExist() {
        this.details.put("id", this.facebookUser.getId());
        assertReflectionEquals(this.facebookUser.getUser(), this.facebookRegistrationService.getUser(this.oAuth2Authentication));
    }

    @Test
    public void testSaveUserNullInput() {
        assertNull(this.facebookRegistrationService.saveUser(null));
    }

    @Test
    public void testSaveUserNullDetails() {
        this.oAuth2Authentication.setDetails(null);
        assertNull(this.facebookRegistrationService.saveUser(this.oAuth2Authentication));
    }

    @Test
    public void testSaveUserInvalidDetails() {
        this.oAuth2Authentication.setDetails(null);
        assertNull(this.facebookRegistrationService.saveUser(this.oAuth2Authentication));
    }

    @Test
    public void testSaveUser() {
        this.details.put("id", UUID.randomUUID());
        this.details.put("name", this.user.getFirstName() + " " + this.user.getLastName());

        // All the properties should be the same except for the id - so we'll ignore that for now
        assertReflectionEquals(this.user, this.facebookRegistrationService.saveUser(this.oAuth2Authentication), ReflectionComparatorMode.IGNORE_DEFAULTS);
    }

}
