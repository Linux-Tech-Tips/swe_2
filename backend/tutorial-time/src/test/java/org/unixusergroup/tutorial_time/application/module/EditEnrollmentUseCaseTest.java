/**
 * @author Cedar, Lilian
 */

package org.unixusergroup.tutorial_time.application.module;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.module.dto.EditEnrollmentRequest;
import org.unixusergroup.tutorial_time.application.module.dto.EditRequestType;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;

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
        students = new ArrayList<>();
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
        EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(module.getModuleCode(), 5L, EditRequestType.STUDENT_ADDITION);
        students.add(5L);
        editEnrollmentUseCase.execute(editEnrollmentRequest);
        assertEquals(students, module.getStudents());
    }

    @Test
    public void testRemoveStudent() {
        EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(module.getModuleCode(), 1L, EditRequestType.STUDENT_REMOVAL);
        editEnrollmentUseCase.execute(editEnrollmentRequest);
        students.remove(1L);
        assertEquals(students, module.getStudents());
    }
    
    @Test
    public void testRemoveNonExistentStudent_throwsException() {
        EditEnrollmentRequest request = new EditEnrollmentRequest(module.getModuleCode(), 999L, EditRequestType.STUDENT_REMOVAL);
        // The underlying Module.removeStudent() throws IllegalArgumentException, 
        // which EditEnrollmentUseCase wraps in a RuntimeException.
        assertThrows(RuntimeException.class, () -> editEnrollmentUseCase.execute(request));
    }

    @Test
    public void testAddExistingStudent_noChange() {
        // Student 1L is already enrolled from setup
        EditEnrollmentRequest request = new EditEnrollmentRequest(module.getModuleCode(), 1L, EditRequestType.STUDENT_ADDITION);
        editEnrollmentUseCase.execute(request);
        // The list should still contain only the original students (1L)
        assertEquals(1, module.getStudents().size()); 
        assertEquals(students, module.getStudents());
    }
}
