package org.unixusergroup.tutorial_time;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< Updated upstream

import static org.junit.jupiter.api.Assertions.assertEquals;

=======
/**
 * Basic application test class for verifying that the Spring
 * application context loads correctly when using JPA-related
 * configuration.
 * <p>
 * This test ensures that the JPA test slice initializes without
 * errors, validating basic wiring of repositories and persistence
 * components.
 *
 * @author Ethan
 */
>>>>>>> Stashed changes
@DataJpaTest
class TutorialTimeApplicationTests {
	/**
     * Verifies that the Spring application context loads successfully.
     * <p>
     * This test fails if any configuration issues prevent the context
     * from initializing.
     */
	@Test
	void contextLoads() {
	}

    @Test
    void helloTest() {
        TutorialTimeApplication tutorialTimeApplication = new TutorialTimeApplication();
        assertEquals("Welcome to Tutorial Time. New Johnny Test\n",tutorialTimeApplication.hello("Johnny Test"));
    }


}
