/** 
 * @author Ethan
 */
package org.unixusergroup.tutorial_time;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TutorialTimeApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void helloTest() {
        TutorialTimeApplication tutorialTimeApplication = new TutorialTimeApplication();
        assertEquals("Welcome to Tutorial Time. New Johnny Test\n",tutorialTimeApplication.hello("Johnny Test"));
    }


}
