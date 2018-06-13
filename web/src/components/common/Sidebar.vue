<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index">
                        <template slot="title"><i :class="item.icon"></i>{{ item.title }}</template>
                        <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="subItem.index">{{ subItem.title }}
                        </el-menu-item>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index">
                        <i :class="item.icon"></i>{{ item.title }}
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                items: [
                    {
                        icon: 'el-icon-setting',
                        index: 'readme',
                        title: '系统介绍'
                    },
                    {
                        icon: 'el-icon-menu',
                        index: '2',
                        title: '任务',
                        subs: [
                            {
                                index: 'basetable',
                                title: '接取任务列表'
                            },
                            {
                                index: 'publishedTasks',
                                title: '发布任务列表'
                            },
                            {
                                index: 'createTask',
                                title: '新建任务'
                            },
                            {
                                index: 'recommendTask',
                                title: '推荐任务'
                            }
                        ]
                    },
                    {
                        icon: 'el-icon-star-on',
                        index:'personalSpace',
                        title: '个人中心',
                        subs:[
                            {
                                icon: 'el-icon-star-on',
                                index:'personalSpace',
                                title: '个人信息',
                            },
                            {
                                icon: 'el-icon-date',
                                index: 'dataCheck',
                                title: '数据统计',

                            },
                            {
                                icon: 'el-icon-news',
                                index: 'messageCenter',
                                title: '消息中心'
                            },
                            {
                                index: 'recharge',
                                title: '充值'
                            }
                        ]
                    },
                    // {
                    //     icon:'el-icon-star-on',
                    //     index:'taskRate',
                    //     title:'任务完成进度界面'
                    // }
                ]
            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom:0;
        background: #2E363F;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
