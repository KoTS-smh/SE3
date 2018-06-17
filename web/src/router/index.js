import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/guide'
        },
        {
            path: '/readme',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            children:[
                {
                    path: '/',
                    component: resolve => require(['../components/page/space/Readme.vue'], resolve)
                },
                {
                    path: '/basetable',
                    component: resolve => require(['../components/page/space/BaseTable.vue'], resolve)
                },
                {
                    path:'/adTable',
                    component:resolve => require(['../components/page/space/administratorTable.vue'],resolve)
                },
                {
                    path: '/createTask',
                    component: resolve => require(['../components/page/space/CreateTask.vue'], resolve)
                },
                {
                    path: '/publishedTasks',
                    component: resolve => require(['../components/page/space/PublishedTasks.vue'], resolve)
                },
                {
                    path: '/personalSpace',
                    component: resolve => require(['../components/page/space/PersonalSpace.vue'], resolve)
                },
                {
                    path:'/taskRate',
                    component:resolve => require(['../components/page/space/TaskRate.vue'],resolve)
                },
                {
                    path: '/dataCheck',
                    component: resolve => require(['../components/page/space/DataCheck.vue'], resolve)
                },
                {
                    path: '/recommendTask',
                    component: resolve => require(['../components/page/space/RecommendTask.vue'], resolve)
                },
                {
                    path: '/messageCenter',
                    component: resolve => require(['../components/page/space/MessageCenter.vue'], resolve)
                },
                {
                    path: '/recharge',
                    component: resolve => require(['../components/page/space/RechargePage.vue'], resolve)
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/login/Login.vue'], resolve)
        },
        {
            path:'/register',
            component:resolve => require(['../components/page/register/Register.vue'],resolve)
        },
        {
            path: '/annotation/all',
            component:resolve => require(['src/components/page/annotation/allAnnotation.vue'],resolve)
        },
        {
            path:'/annotation/rect',
            component:resolve => require(['src/components/page/annotation/rectAnnotation.vue'],resolve)
        },
        {
            path:'/annotation/region',
            component:resolve => require(['src/components/page/annotation/regionAnnotation.vue'],resolve)
        },
        {
            path:'/annotation/classified',
            component:resolve => require(['src/components/page/annotation/classifiedAnnotation.vue'],resolve)
        },
        {
            path:'/guide',
            component:resolve => require(['../components/page/guide/Guide.vue'],resolve)
        },
        {
            path: '/checkTask',
            component: resolve => require(['../components/page/space/CheckTask.vue'], resolve)
        },
        {
            path: '/homepage',
            component: resolve => require(['../components/homepage/Bhomepage.vue'], resolve)
        },
        {
            path: '/search',
            component: resolve => require(['../components/page/search/Search.vue'], resolve)
        },
    ]
})
