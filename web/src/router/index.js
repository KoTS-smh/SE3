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
                    path: '/vuetable',
                    component: resolve => require(['../components/page/space/VueTable.vue'], resolve)     // vue-datasource组件
                },
                {
                    path: '/baseform',
                    component: resolve => require(['../components/page/space/BaseForm.vue'], resolve)
                },
                {
                    path: '/vueeditor',
                    component: resolve => require(['../components/page/space/VueEditor.vue'], resolve)    // Vue-Quill-Editor组件
                },
                {
                    path: '/markdown',
                    component: resolve => require(['../components/page/space/Markdown.vue'], resolve)     // Vue-Quill-Editor组件
                },
                {
                    path: '/upload',
                    component: resolve => require(['../components/page/space/Upload.vue'], resolve)       // Vue-Core-Image-Upload组件
                },
                {
                    path: '/basecharts',
                    component: resolve => require(['../components/page/space/BaseCharts.vue'], resolve)   // vue-schart组件
                },
                {
                    path: '/drag',
                    component: resolve => require(['../components/page/space/DragList.vue'], resolve)    // 拖拽列表组件
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
            path:'/annotation/rate/classified',
            component:resolve => require(['src/components/page/rate/classifiedAnnotationRate.vue'],resolve)
        },
        {
            path: '/annotation/rate/all',
            component:resolve => require(['src/components/page/rate/allAnnotationRate.vue'],resolve)
        },
        {
            path:'/annotation/rate/rect',
            component:resolve => require(['src/components/page/rate/rectAnnotationRate.vue'],resolve)
        },
        {
            path:'/annotation/rate/region',
            component:resolve => require(['src/components/page/rate/regionAnnotationRate.vue'],resolve)
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
