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
                    path:'/picktable',
                    component: resolve => require(['../components/page/space/PickTable.vue'],resolve)
                },
                {
                    path:'/releaseTable',
                    component:resolve => require(['../components/page/space/ReleaseTable.vue'],resolve)
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
                    path: '/getTask',
                    component: resolve => require(['../components/page/space/GetTask.vue'], resolve)
                },
                // {
                //     path: '/publishedTasks',
                //     component: resolve => require(['../components/page/space/PublishedTasks.vue'], resolve)
                // },
                {
                    path: '/createTask',
                    component: resolve => require(['../components/page/space/CreateTask.vue'], resolve)
                },
                // {
                //     path: '/checkTask',
                //     component: resolve => require(['../components/page/space/CheckTask.vue'], resolve)
                // },
                // {
                //     path: '/analysis',
                //     component: resolve => require(['../components/page/space/Analysis.vue'], resolve)
                // }
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
            path: '/allAnnotation',
            component:resolve => require(['src/components/page/annotation/allAnnotation.vue'],resolve)
        },
        {
            path:'/guide',
            component:resolve => require(['../components/page/guide/Guide.vue'],resolve)
        },
        // {
        //     path: '/mainPage',
        //     component: resolve => require(['../components/page/mainPage/MainPage.vue'], resolve)
        // }
    ]
})
