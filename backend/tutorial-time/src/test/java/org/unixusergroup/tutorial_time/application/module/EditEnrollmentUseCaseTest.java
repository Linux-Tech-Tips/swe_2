/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.module.dto.EditEnrollmentRequest;
import org.unixusergroup.tutorial_time.application.module.dto.EditRequestType;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.domain.module.Module;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class EditEnrollmentUseCaseTest {

    private final InMemoryModuleRepository testModuleRepository = new InMemoryModuleRepository();
    private EditEnrollmentUseCase editEnrollmentUseCase = new EditEnrollmentUseCase(testModuleRepository);
    private Module module;
    private ArrayList<Long> students = new ArrayList<>();
    private ArrayList<Long> tas = new ArrayList<>();

    //TODO: no negative testing here
    @BeforeEach
    public void setup() {
        students.add(1L);
        tas.add(3L);
        module = new Module("testModule", students, tas, 1000L);
        testModuleRepository.saveModule(module);
    }

    @Test
    public void testAddTA() {
        EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(module.getModuleCode(), 4L, EditRequestType.TA_ADDITION);
        tas.add(4L);
        editEnrollmentUseCase.execute(editEnrollmentRequest);
        assertEquals(tas, module.getTAStudents());
    }

    @Test
    public void testRemoveTA() {
        EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(module.getModuleCode(), 3L, EditRequestType.TA_REMOVAL);
        editEnrollmentUseCase.execute(editEnrollmentRequest);
        tas.remove(3L);
        assertEquals(tas, module.getTAStudents());
    }

    @Test
    public void testIllegalModuleCode(){
        EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest("fakeModule", 4L, EditRequestType.TA_ADDITION);
        assertThrows(RuntimeException.class, () -> editEnrollmentUseCase.execute(editEnrollmentRequest));
    }

    @Test
    public void testAddStudent() {
        //TODO
    }

    @Test
    public void testRemoveStudent() {
        //TODO
    }
}
