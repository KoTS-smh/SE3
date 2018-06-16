<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-setting"></i> 系统介绍</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="task-state" style="margin-left:50px">
            <el-row :gutter="10">
                <el-col :sm="24" :md="6" v-for="card in cardsData" :key="card.id">
                    <div class="grid-content bg-purple">
                    <lee-status-card :count="card.count" :name="card.name"></lee-status-card>
                    </div>
                </el-col>
            </el-row>
        </div>

    </div>
</template>

<script>
import status_card from '../../common/status_card.vue';
    export default {
        components: {
            'lee-status-card': status_card
        },
        data(){
            return {
                appoint_num: 0,
                ongoing_num: 0,

                cardsData: [
                    {
                        count: '996',
                        name: '预约中'
                    },
                    {
                        count: '170871',
                        name: '进行中'
                    },
                    {
                        count: '38470',
                        name: '已完成'
                    },
                    {
                        count: '12350',
                        name: '信用积分'
                    }
                ]
            }
        },

        mounted() {
            var username = localStorage.getItem('username')
            if(username != null && username.length > 0) {
                
            }else {
                this.$message('请先登陆');
                this.$router.push('login');
            }
        },
        beforeMount() {
            
        }
    }
    
</script>

<style scoped>
    .ms-doc{
        width:100%;
        max-width: 980px;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
    }
    .ms-doc h3{
        padding: 9px 10px 10px;
        margin: 0;
        font-size: 14px;
        line-height: 17px;
        background-color: #f5f5f5;
        border: 1px solid #d8d8d8;
        border-bottom: 0;
        border-radius: 3px 3px 0 0;
    }
    .ms-doc article{
        padding: 45px;
        word-wrap: break-word;
        background-color: #fff;
        border: 1px solid #ddd;
        border-bottom-right-radius: 3px;
        border-bottom-left-radius: 3px;
    }
    .ms-doc article h1{
        font-size:32px;
        padding-bottom: 10px;
        margin-bottom: 15px;
        border-bottom: 1px solid #ddd;
    }
    .ms-doc article h2 {
        margin: 24px 0 16px;
        font-weight: 600;
        line-height: 1.25;
        padding-bottom: 7px;
        font-size: 24px;
        border-bottom: 1px solid #eee;
    }
    .ms-doc article p{
        margin-bottom: 15px;
        line-height: 1.5;
    }
    .ms-doc article .el-checkbox{
        margin-bottom: 5px;
    }

    .bg-purple-dark {
        background: #99a9bf;
    }
    .bg-purple {
        background: #d3dce6;
    }
    .grid-content {
        border-radius: 4px;
        min-height: 36px;
    }
</style>