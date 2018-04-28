<template>
    <div>
        <el-container>
            <el-header>
                <div class="top_container">
                    <el-menu default-active="/search" class="el-menu-demo" mode="horizontal" @select="handleSelect" router="true">
                        <el-row>
                            <el-col :span="2">
                                <el-menu-item index="/guide" style="text-align: center">首页</el-menu-item>
                            </el-col>
                            <el-col :span="2">
                                <el-menu-item index="/homepage" style="text-align: center">主站</el-menu-item>
                            </el-col>
                            <el-col :span="2">
                                <el-menu-item index="/search" style="text-align: center">搜索</el-menu-item>
                            </el-col>
                            <el-col :span="2" :offset="11">
                                <el-menu-item index="/guide" style="text-align: center">登陆</el-menu-item>
                            </el-col>
                            <el-col :span="2">
                                <el-menu-item index="/homepage" style="text-align: center">注册</el-menu-item>
                            </el-col>
                            <el-col :span="2">
                                <el-dropdown style="text-align: center;margin-top: 7.5px">
                                    <el-button type="primary">
                                        发布任务<i class="el-icon-arrow-down el-icon--right"></i>
                                    </el-button>
                                    <el-dropdown-menu slot="dropdown">
                                        <el-dropdown-item>发布界面</el-dropdown-item>
                                        <el-dropdown-item>任务管理</el-dropdown-item>
                                    </el-dropdown-menu>
                                </el-dropdown>
                            </el-col>
                        </el-row>
                        <div class="line"></div>
                    </el-menu>
                </div>
            </el-header>
            <el-main>
                <!--
        搜索表单
        -->
                <el-form ref="form" :model="form" size="mini">
                    <el-form-item placeholder="输入搜索关键字">
                        <el-row :gutter="20">
                            <el-col :span="2" :offset="7" style="margin-top: 20px"><img src="~assets/logo.png" class="image"></el-col>
                            <el-col :span="6" style="margin-top: 40px"><el-input v-model="form.name"></el-input></el-col>
                            <el-col :span="2" style="margin-top: 40px"><el-button type="primary" @click="onSubmit" icon="el-icon-search">站内搜索</el-button></el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item style="text-align: center">
                        <el-radio-group v-model="form.resource1">
                            <el-radio-button label="r1-1" v-model="radio1">综合排序</el-radio-button>
                            <el-radio-button label="r1-2" v-model="radio1">最新发布</el-radio-button>
                            <el-radio-button label="r1-3" v-model="radio1">最多收藏</el-radio-button>
                            <el-radio-button label="r1-4" v-model="radio1">最多参与</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item style="text-align: center">
                        <el-radio-group v-model="form.resource2">
                            <el-radio-button v-model="radio2" label="r2-1">标框标注</el-radio-button>
                            <el-radio-button v-model="radio2" label="r2-2">分类标注</el-radio-button>
                            <el-radio-button v-model="radio2" label="r2-3">区域标注</el-radio-button>
                            <el-radio-button v-model="radio2" label="r2-4">整体标注</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item style="text-align: center">
                        <el-switch v-model="form.delivery" active-text="同时搜索已结束的任务"></el-switch>
                    </el-form-item>
                </el-form>

                <div class="line"></div>

                <!--搜索结果-->
                <el-row>
                    <el-col :span="4" v-for="(o, index) in 2" :key="o" :offset="index > 0 ? 2 : 0">
                        <el-card :body-style="{ padding: '0px',height:'240px'}" :shadow="hover">
                            <img src="~assets/logo.png" class="image" width="180px" height="180px">
                            <div class="line"></div>
                            <div style="padding: 14px;">
                                <span>演示任务</span>
                                <div class="bottom clearfix">
                                    <time class="time">{{ currentDate }}</time>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <!--分页-->
                <div class="block" style="text-align: center">
                    <el-pagination
                        layout="prev, pager, next"
                        :total="100">
                    </el-pagination>
                </div>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    export default {
        data() {
            return {
                activeIndex: '1',
                activeIndex2: '1',
                form: {
                    name: '',
                    delivery: false,
                    resource1: 'r1-1',
                    resource2: 'r2-1'
                },
                currentDate: new Date()
            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            onSubmit() {
                console.log('submit!');
            }
        }
    }
</script>

<style scoped>
    el-form-item {
        text-align: center;
        margin-top: 10px;
    }

    .time {
        font-size: 13px;
        color: #999;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .button {
        padding: 0;
        float: right;
    }

    .image {
        width: 100%;
        display: block;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }

</style>
