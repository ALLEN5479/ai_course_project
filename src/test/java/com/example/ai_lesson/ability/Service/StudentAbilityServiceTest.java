package com.example.ai_lesson.ability.Service;

import com.example.ai_lesson.ability.Domain.StudentAbility;
import com.example.ai_lesson.ability.Domain.QuestionRecordDTO;
import com.example.ai_lesson.ability.Mapper.StudentAbilityMapper;
import com.example.ai_lesson.ability.Service.impl.StudentAbilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * StudentAbilityService的单元测试
 * 白盒测试：使用Mock隔离依赖，测试业务逻辑的各种分支
 */
@ExtendWith(MockitoExtension.class)
class StudentAbilityServiceTest {

    @Mock
    private StudentAbilityMapper studentAbilityMapper;

    @InjectMocks
    private StudentAbilityServiceImpl studentAbilityService;

    private StudentAbility testStudentAbility;
    private QuestionRecordDTO testQuestionRecord;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testStudentAbility = new StudentAbility(1L, "张三");
        testStudentAbility.setProgrammingScore(85.0);
        testStudentAbility.setAiScore(90.0);
        testStudentAbility.setMathScore(88.0);
        testStudentAbility.setCreativityScore(92.0);
        testStudentAbility.setCommunicationScore(87.0);

        testQuestionRecord = new QuestionRecordDTO();
        testQuestionRecord.setQuestionId(1L);
        testQuestionRecord.setStudentId(1L);
        testQuestionRecord.setDifficulty(1500.0);
        testQuestionRecord.setIsCorrect(true);
    }

    @Test
    @DisplayName("测试根据学生ID获取能力数据")
    void testGetByStudentId() {
        // 准备Mock数据
        List<StudentAbility> expectedAbilities = Arrays.asList(testStudentAbility);
        when(studentAbilityMapper.selectStudentAbilityByStudentId(1L)).thenReturn(expectedAbilities);

        // 执行测试
        List<StudentAbility> result = studentAbilityService.getByStudentId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
        assertEquals("张三", result.get(0).getStudentName());

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).selectStudentAbilityByStudentId(1L);
    }

    @Test
    @DisplayName("测试获取学生语法ELO分数 - 有答题记录")
    void testGetGrammarEloByStudentId_WithRecords() {
        // 准备Mock数据 - 模拟答题记录
        List<QuestionRecordDTO> records = Arrays.asList(
            createQuestionRecord(1L, 1500.0, true),   // 答对
            createQuestionRecord(2L, 1600.0, false),  // 答错
            createQuestionRecord(3L, 1400.0, true)    // 答对
        );
        
        when(studentAbilityMapper.selectGrammarRecordsByStudentId(1L)).thenReturn(records);

        // 执行测试
        Double result = studentAbilityService.getGrammarEloByStudentId(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result >= 1500.0); // ELO分数应该基于初始分1500计算
        assertTrue(result <= 2500.0); // 不超过最大值2500

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).selectGrammarRecordsByStudentId(1L);
    }

    @Test
    @DisplayName("测试获取学生语法ELO分数 - 无答题记录")
    void testGetGrammarEloByStudentId_NoRecords() {
        // 准备Mock数据 - 空记录列表
        when(studentAbilityMapper.selectGrammarRecordsByStudentId(1L)).thenReturn(Arrays.asList());

        // 执行测试
        Double result = studentAbilityService.getGrammarEloByStudentId(1L);

        // 验证结果 - 应该返回初始分1500
        assertEquals(1500.0, result, 0.01);

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).selectGrammarRecordsByStudentId(1L);
    }

    @Test
    @DisplayName("测试获取学生语法ELO分数 - 包含null记录")
    void testGetGrammarEloByStudentId_WithNullRecords() {
        // 准备Mock数据 - 包含null的记录
        List<QuestionRecordDTO> records = Arrays.asList(
            createQuestionRecord(1L, 1500.0, true),
            null, // null记录
            createQuestionRecord(2L, 1600.0, false)
        );
        
        when(studentAbilityMapper.selectGrammarRecordsByStudentId(1L)).thenReturn(records);

        // 执行测试 - 不应该抛出异常
        assertDoesNotThrow(() -> {
            Double result = studentAbilityService.getGrammarEloByStudentId(1L);
            assertNotNull(result);
        });

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).selectGrammarRecordsByStudentId(1L);
    }

    @Test
    @DisplayName("测试添加问题记录")
    void testAddQuestionRecord() {
        // 执行测试
        studentAbilityService.addQuestionRecord(testQuestionRecord);

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).insertQuestionRecord(testQuestionRecord);
    }

    @Test
    @DisplayName("测试获取学生能力数据")
    void testGetStudentAbility() {
        // 准备Mock数据
        when(studentAbilityMapper.selectStudentAbilityById(1L)).thenReturn(testStudentAbility);

        // 执行测试
        StudentAbility result = studentAbilityService.getStudentAbility(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        assertEquals("张三", result.getStudentName());

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).selectStudentAbilityById(1L);
    }

    @Test
    @DisplayName("测试获取所有学生能力数据")
    void testGetAllStudentsAbility() {
        // 准备Mock数据
        List<StudentAbility> expectedAbilities = Arrays.asList(
            testStudentAbility,
            new StudentAbility(2L, "李四")
        );
        when(studentAbilityMapper.selectAllStudentsAbility()).thenReturn(expectedAbilities);

        // 执行测试
        List<StudentAbility> result = studentAbilityService.getAllStudentsAbility();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).selectAllStudentsAbility();
    }

    @Test
    @DisplayName("测试更新学生能力数据")
    void testUpdateStudentAbility() {
        // 执行测试
        studentAbilityService.updateStudentAbility(testStudentAbility);

        // 验证更新时间被设置
        assertNotNull(testStudentAbility.getUpdateTime());

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).updateStudentAbility(testStudentAbility);
    }

    @Test
    @DisplayName("测试创建学生能力数据")
    void testCreateStudentAbility() {
        // 执行测试
        studentAbilityService.createStudentAbility(testStudentAbility);

        // 验证创建时间和更新时间被设置
        assertNotNull(testStudentAbility.getCreateTime());
        assertNotNull(testStudentAbility.getUpdateTime());

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).insertStudentAbility(testStudentAbility);
    }

    @Test
    @DisplayName("测试删除学生能力数据")
    void testDeleteStudentAbility() {
        // 执行测试
        studentAbilityService.deleteStudentAbility(1L);

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1)).deleteStudentAbilityByStudentId(1L);
    }

    @Test
    @DisplayName("测试批量更新学生能力数据")
    void testBatchUpdateStudentAbility() {
        // 准备测试数据
        List<StudentAbility> abilities = Arrays.asList(
            new StudentAbility(1L, "张三"),
            new StudentAbility(2L, "李四")
        );

        // 执行测试
        studentAbilityService.batchUpdateStudentAbility(abilities);

        // 验证每个能力对象的更新时间被设置
        for (StudentAbility ability : abilities) {
            assertNotNull(ability.getUpdateTime());
        }

        // 验证Mock方法被调用两次（每个能力对象一次）
        verify(studentAbilityMapper, times(2)).updateStudentAbility(any(StudentAbility.class));
    }

    @Test
    @DisplayName("测试根据学生ID和能力类型获取ELO分数")
    void testGetEloByStudentIdAndAbilityType() {
        // 准备Mock数据
        List<QuestionRecordDTO> records = Arrays.asList(
            createQuestionRecord(1L, 1500.0, true),
            createQuestionRecord(2L, 1600.0, false)
        );
        
        when(studentAbilityMapper.selectRecordsByStudentIdAndAbilityType(1L, "programming"))
            .thenReturn(records);

        // 执行测试
        Double result = studentAbilityService.getEloByStudentIdAndAbilityType(1L, "programming");

        // 验证结果
        assertNotNull(result);
        assertTrue(result >= 1500.0);
        assertTrue(result <= 2500.0);

        // 验证Mock方法被调用
        verify(studentAbilityMapper, times(1))
            .selectRecordsByStudentIdAndAbilityType(1L, "programming");
    }

    // 辅助方法：创建问题记录
    private QuestionRecordDTO createQuestionRecord(Long questionId, Double difficulty, Boolean isCorrect) {
        QuestionRecordDTO record = new QuestionRecordDTO();
        record.setQuestionId(questionId);
        record.setStudentId(1L);
        record.setDifficulty(difficulty);
        record.setIsCorrect(isCorrect);
        return record;
    }
} 