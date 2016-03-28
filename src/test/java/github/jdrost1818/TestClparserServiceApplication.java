package github.jdrost1818;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestClparserServiceApplication {

    @Test
    public void contextLoads() {
        ClparserServiceApplication.main(new String[]{});
    }
}
