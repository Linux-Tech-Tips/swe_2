/**
 * @author Ethan, Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerJPARepository;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerPersistenceAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class LecturerDatabaseRepositoryTest {
    @Autowired
    LecturerJPARepository lecturerJPARepository;
    LecturerPersistenceAdapter lecturerPersistenceAdapter;

    Lecturer testLecturer;

    @BeforeEach
    void setup() {
        lecturerPersistenceAdapter = new LecturerPersistenceAdapter(lecturerJPARepository);
        Lecturer testLecturer = new Lecturer("j.test@nuigalway.ie", 10002);
        Lecturer testLecturer1 = new Lecturer("L.tests@nuigalway.ie", 12342);
        lecturerPersistenceAdapter.saveLecturer(testLecturer);
        lecturerPersistenceAdapter.saveLecturer(testLecturer1);
    }

    @Test
    void retrieveLecturerById() {
        Lecturer testLecturer = new Lecturer("j.test@nuigalway.ie", 10002);
        assertEquals(testLecturer, lecturerPersistenceAdapter.getLecturerById(10002));
    }

    @Test
    void deleteLecturerTest() {
        lecturerPersistenceAdapter.deleteLecturer(testLecturer);
        assertNull(lecturerPersistenceAdapter.getLecturerById(10002));
    }

}
