package org.unixusergroup.tutorial_time.domain.module;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ModuleTest {
    String moduleCode = "testModule";
    long lecturer = 1000L;
    ArrayList<Long> students = new ArrayList<>();
    ArrayList<Long> taStudents = new ArrayList<>();
    Module module = new Module(moduleCode, students, taStudents, lecturer);

    /**
     * test the constructor with no parameters
     */
    @Test
    public void testEmptyConstructor() {
        Module module = new Module();
        assertEquals(0, module.getLecturer());
        assertNull(module.getModuleCode());
        assertNull(module.getStudents());
        assertNull(module.getTAStudents());
    }

    /**
     * test the constructor with just lecturer and module code
     */
    @Test
    public void testConstructorNoStudents() {
        Module module = new Module(moduleCode, lecturer);
        assertEquals(lecturer, module.getLecturer());
        assertEquals(moduleCode, module.getModuleCode());
        assertEquals(students, module.getStudents());
        assertEquals(taStudents, module.getTAStudents());
    }

    /**
     * test the full constructor with all parameters
     */
    @Test
    public void testFullConstructor() {
        Module module = new Module(moduleCode, students, taStudents, lecturer);
        assertEquals(lecturer, module.getLecturer());
        assertEquals(moduleCode, module.getModuleCode());
        assertEquals(students, module.getStudents());
        assertEquals(taStudents, module.getTAStudents());
    }

    @Test
    public void testConstructorInvalidModuleCode() {
        assertThrows(IllegalArgumentException.class, () -> new Module("", lecturer));
    }

    @Test
    public void testConstructorInvalidLecturer() {
        assertThrows(IllegalArgumentException.class, () -> new Module(moduleCode, 0));
    }

    @Test
    public void testEquals() {
        Module module2 = new Module(moduleCode, students, taStudents, lecturer);
        assertEquals(module, module2);
    }

    @Test
    public void testNotEquals() {
        Module module2 = new Module("fakeModule", students, taStudents, lecturer);
        assertNotEquals(module, module2);
    }

    @Test
    public void testAddStudent() {
        module.addStudent(10L);
        students.add(10L);
        assertEquals(module.getStudents(), students);
    }

    @Test
    public void testRemoveStudent() {
        module.addStudent(10L);
        module.removeStudent(10L);
        assertEquals(module.getStudents(), students);
    }

    @Test
    public void testInvalidAddStudent() {
        assertThrows(IllegalArgumentException.class, () -> module.addStudent(0));
    }

    @Test
    public void testAddTAStudent() {
        module.addTAStudent(10L);
        taStudents.add(10L);
        assertEquals(module.getTAStudents(), taStudents);
    }

    @Test
    public void testRemoveTAStudent() {
        module.addTAStudent(10L);
        module.removeTAStudent(10L);
        assertEquals(module.getStudents(), taStudents);
    }

    @Test
    public void testInvalidAddTAStudent() {
        assertThrows(IllegalArgumentException.class, () -> module.addTAStudent(0));
    }

}
