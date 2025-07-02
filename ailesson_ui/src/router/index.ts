import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import QuizVew from "@/views/teacher/QuizVew.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/student',
      name: 'student',
      redirect: '/student/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'student-dashboard',
          component: () => import('../views/student/StudentDashboard.vue'),
        },
        {
          path: 'courses',
          name: 'student-courses',
          component: () => import('../views/student/StudentCourses.vue'),
        },
        {
          path: 'courses/:id',
          name: 'student-course-detail',
          component: () => import('../views/student/StudentCourseDetail.vue'),
        },
        {
          path: 'knowledge-graph',
          name: 'student-knowledge-graph',
          component: () => import('../views/student/KnowledgeGraph.vue'),
        },
        {
          path: 'learning',
          name: 'student-learning',
          component: () => import('../views/student/StudentLearningLayout.vue'),
        },
        //旧版的课程总览页面 {
        //   path: 'learning/:courseId',
        //   name: 'student-learning-with-course',
        //   component: () => import('../views/student/LearningView.vue'),
        // },
        {
          path: 'missions',
          name: 'student-missions',
          component: () => import('../views/student/MissionView.vue'),
        },
        // {
        //   path: 'ability-map',
        //   name: 'student-ability-map',
        //   component: () => import('../views/student/AbilityMap.vue'),
        // },
        {
          path: 'profile',
          name: 'student-profile',
          component: () => import('../views/student/StudentProfile.vue'),
        },
        {
          path: 'study-resources',
          name: 'student-study-resources',
          component: () => import('../views/student/StudyResources.vue'),
        },
        {
          path: 'study-resources/:nodeId',
          name: 'student-study-resources-with-node',
          component: () => import('../views/student/StudyResources.vue'),
        },
        // {
        //   path: 'self-study',
        //   name: 'student-self-study',
        //   component: () => import('../views/student/SelfStudy.vue'),
        // }
        {
          path: 'personal',
          name: 'student-personal',
          component: () => import('../views/student/StudentPersonal.vue'),
        },
      ]
    },
    {
      path: '/teacher',
      name: 'teacher',
      redirect: '/teacher/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'teacher-dashboard',
          component: () => import('../views/teacher/TeacherDashboard.vue'),
        },
        {
          path: 'courses',
          name: 'teacher-courses',
          component: () => import('../views/teacher/CourseBrowser.vue'),
        },
        {
          path: 'courses/:id',
          name: 'teacher-course-detail',
          component: () => import('../views/teacher/CourseDetail.vue'),
        },
        {
          path: 'students',
          name: 'teacher-students',
          component: () => import('../views/teacher/StudentList.vue'),
        },
        {
          path: 'ability-map/:studentId',
          name: 'teacher-ability-map',
          component: () => import('../views/student/ability/index.vue'),
        },
        {
          path: 'knowledge-graph',
          name: 'teacher-knowledge-graph',
          component: () => import('../views/teacher/KnowledgeGraph.vue'),
        },
        // {
        //   path: 'test',
        //   name: 'test-page',
        //   component: () => import('../views/teacher/TestPage.vue'),
        // },
        // {
        //   path: 'class-mission-student',
        //   name: 'class_mission_student',
        //   component: () => import('../views/teacher/ClassMissionStudent.vue'),
        // },
        {
          path: 'profile',
          name: 'teacher-profile',
          component: () => import('../views/teacher/TeacherProfile.vue'),
        },
        {
          path: 'student-management',
          name: 'teacher-student-management',
          component: () => import('../views/teacher/StudentManagement.vue'),
        },
        {
          path: 'task-library',
          name: 'teacher-task-library',
          component: () => import('../views/teacher/TaskLibraryManager.vue'),
        },
        {
          path: 'class-task-manager',
          name: 'teacher-class-task-manager',
          component: () => import('../views/teacher/TaskManager.vue'),
        },
        {
          path: 'task-grading',
          name: 'task-grading',
          component: () => import('../views/teacher/TaskGrading.vue'),
        },
        {
          path: 'course-submission-list',
          name: 'course-submission-list',
          component: () => import('../views/teacher/CourseSubmissionList.vue'),
        },
      ]
    },
    {
      path: '/student/ability',
      name: 'StudentAbility',
      component: () => import('@/views/student/ability/index.vue')
    },
    {
      path: '/quiz',
      name: 'quiz',
      component: QuizVew,
    },
  ],
})

export default router
