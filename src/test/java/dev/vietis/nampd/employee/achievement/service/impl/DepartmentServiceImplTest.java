package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.DepartmentMapper;
import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDepartmentSuccess() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentName("IT");

        Department department = new Department();
        department.setDepartmentName("IT");

        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(false);
        when(departmentMapper.toDepartment(departmentDTO)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);

        departmentServiceImpl.createDepartment(departmentDTO);

        verify(departmentRepository, times(1)).save(department);
    }

    //department exists
    @Test
    void createDepartmentFail() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentName("IT");

        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.createDepartment(departmentDTO);
        });

        assertEquals("Department already exists", exception.getMessage());
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void getAllDepartmentsSuccess() {
        Department department1 = new Department();
        Department department2 = new Department();
        Department department3 = new Department();

        DepartmentDTO departmentDTO1 = new DepartmentDTO();
        DepartmentDTO departmentDTO2 = new DepartmentDTO();
        DepartmentDTO departmentDTO3 = new DepartmentDTO();

        when(departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(List.of(department1, department2, department3));
        when(departmentMapper.toDepartmentDto(department1)).thenReturn(departmentDTO1);
        when(departmentMapper.toDepartmentDto(department2)).thenReturn(departmentDTO2);
        when(departmentMapper.toDepartmentDto(department3)).thenReturn(departmentDTO3);

        List<DepartmentDTO> departments = departmentServiceImpl.getAllDepartments();

        assertEquals(3, departments.size());
        assertEquals(departmentDTO1, departments.get(0));
        assertEquals(departmentDTO2, departments.get(1));
        assertEquals(departmentDTO3, departments.get(1));
    }

    @Test
    void getDepartmentByIdSuccess() {
        Long departmentId = 1L;
        Department department = new Department();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.toDepartmentDto(department)).thenReturn(departmentDTO);

        DepartmentDTO resultDepartmentDTO = departmentServiceImpl.getDepartmentById(departmentId);

        assertEquals(departmentDTO, resultDepartmentDTO);
    }

    //not found department
    @Test
    void getDepartmentByIdFail() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.getDepartmentById(departmentId);
        });

        assertEquals("Department not found", exception.getMessage());
    }

    @Test
    void updateDepartmentSuccess() {
        Long departmentId = 1L;
        DepartmentDTO updatedDepartmentDTO = new DepartmentDTO();
        updatedDepartmentDTO.setDepartmentName("Sales");

        Department existingDepartment = new Department();
        existingDepartment.setDepartmentName("IT");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(existingDepartment)).thenReturn(existingDepartment);
        when(departmentMapper.toDepartmentDto(existingDepartment)).thenReturn(updatedDepartmentDTO);

        departmentServiceImpl.updateDepartment(departmentId, updatedDepartmentDTO);

        verify(departmentRepository).save(existingDepartment);
        assertEquals("Sales", existingDepartment.getDepartmentName());
    }

    //not found department
    @Test
    void updateDepartmentFail() {
        Long departmentId = 1L;
        DepartmentDTO updatedDepartmentDto = new DepartmentDTO();
        updatedDepartmentDto.setDepartmentName("Updated IT");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.updateDepartment(departmentId, updatedDepartmentDto);
        });

        assertEquals("Department not found", exception.getMessage());
    }

    @Test
    void deleteDepartmentSuccess() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(new Department()));

        departmentServiceImpl.deleteDepartment(departmentId);

        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    @Test
    void deleteDepartmentFail() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            departmentServiceImpl.deleteDepartment(departmentId);
        });

        assertEquals("Department not found with id: " + departmentId, exception.getMessage());
        verify(departmentRepository, never()).deleteById(any());
    }
}