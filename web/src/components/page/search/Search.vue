<template>
    <div style="height:100%">
        <el-container style="height:100%">
            <el-header>
                <div class="top_container">
                    <el-menu default-active="/search" class="el-menu-demo" mode="horizontal" @select="handleSelect" :router="canRoute">
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
            <el-main style="height:100%">
                <!--
        搜索表单
        -->     <div style="text-align: center">
                    <img style="margin-right:20px;" src="https://ws1.sinaimg.cn/large/0073JsqJly1fr1uiup7upj302e00raa0.jpg">
                    <el-select v-model="tag" placeholder="请选择" style="width: 100px">
                        <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>

                    <el-input v-model="input" placeholder="请输入内容" style="width:200px; "></el-input>
                    <el-button type="primary" icon="el-icon-search" @click="searchTask">搜索</el-button>
                </div>
                <div style="margin-left: 220px;margin-right: 220px; ">
                    <!-- <el-menu :default-active="activeIndex" class="taskTypeMenu" mode="horizontal" @select="handleTypeSelect">
                        <el-menu-item index="0" key="0" class="type_item" @click="indexToOne()">所有任务</el-menu-item>
                        <el-menu-item index="1" key="1" class="type_item">标框标注</el-menu-item>
                        <el-menu-item index="2" key="2" class="type_item">分类标注</el-menu-item>
                        <el-menu-item index="3" key="3" class="type_item">区域标注</el-menu-item>
                        <el-menu-item index="4" key="4" class="type_item">整体标注</el-menu-item>
                    </el-menu> -->
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <el-tab-pane label="全部任务" name="0"></el-tab-pane>
                        <el-tab-pane label="标框标注" name="1"></el-tab-pane>
                        <el-tab-pane label="分类标注" name="2"></el-tab-pane>
                        <el-tab-pane label="区域标注" name="3"></el-tab-pane>
                        <el-tab-pane label="整体标注" name="4"></el-tab-pane>
                    </el-tabs>
                </div>

                <div style="margin-left: 220px; margin-top: 20px">
                    <el-radio-group v-model="sort_type" size="medium" class="sort_types">
                        <el-radio-button label="综合排序"></el-radio-button>
                        <el-radio-button label="最新发布"></el-radio-button>
                        <el-radio-button label="最多点击"></el-radio-button>
                        <el-radio-button label="最多参与"></el-radio-button>
                    </el-radio-group>
                </div>

                <div class="line"></div>

                <!--搜索结果-->
                
                <el-row>
                <el-col :span="4">&nbsp;</el-col>
                <el-col :span="16">
                    <myContent v-bind:searchInfo="this.input" ref="mycontent"></myContent>
                </el-col>
                <el-col :span="4">&nbsp;</el-col>
                </el-row>
                <!--分页-->
                
            </el-main>
        </el-container>
    </div>

</template>

<script>
    import PictureCard from '../../common/picture_card.vue'
    import myContent from 'components/homepage/ContentInSearch.vue'
    import Icon from 'vue-awesome/components/Icon'
    import 'vue-awesome/icons'
    export default {
        components: {
            Icon,
            myContent,
            'picture-card': PictureCard
        },
        data() {
            return {
                activeIndex: '0',
                form: {
                    name: '',
                    delivery: false,
                    resource1: 'r1-1',
                    resource2: 'r2-1'
                },
                currentDate: new Date(),
                sort_type: '',
                
                canRoute: true,
                input: '',
                activeName: 'second',
                options: [{
                    value: 0,
                    label: '动物'
                }, {
                    value: '1',
                    label: '人物'
                }, {
                    value: '2',
                    label: '自然'
                }, {
                    value: '3',
                    label: '日常用品'
                }, {
                    value: '4',
                    label: '建筑'
                },{
                    value: '5',
                    label: '科技'
                },{
                    value: '6',
                    label: '食物'
                },{
                    value: '7',
                    label: '交通'
                },{
                    value: '8',
                    label: '家具'
                },{
                    value: '9',
                    label: '工业产品'
                }
                ],
                tag: '请选择'
            };
        },
        computed: {
            showCardList() {
                return this.card_list.slice((this.currentPage - 1) * this.perPage, this.currentPage * this.perPage);
            },
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            onSubmit() {
                console.log('submit!');
            },
            handleTypeSelect() {
                console.log(key, keyPath);
            },
            handleSizeChange(val) {
                this.perPage = val
            },
            handleCurrentChange(val) {
                this.currentPage = val
            },
            
            searchTask() {
                console.log(this.tag);
                this.$refs.mycontent.searchForTasks(this.input, this.activeName, this.tag);
            },
            handleClick(tab, event) {
                console.log(tab, event);
            },
            getKeyword() {
                let keyword = this.$route.query.keyword;
                this.input = keyword;
                console.log(keyword);
                if(keyword != null && keyword != '' && keyword != undefined){
                    this.$refs.mycontent.searchForTasks(keyword, 0, "all");
                }
            }

        },
        mounted() {
            this.getKeyword();
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

    .type_item{
        font-size: 1.3rem;
    }

    .picture{
        display: inline;
    }

</style>
