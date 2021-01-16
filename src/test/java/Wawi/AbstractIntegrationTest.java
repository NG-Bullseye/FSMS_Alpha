package Wawi;

import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {Application.class, WebSecurityConfiguration.class}) // With necessary imports
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith( SpringRunner.class )
public class AbstractIntegrationTest {}
