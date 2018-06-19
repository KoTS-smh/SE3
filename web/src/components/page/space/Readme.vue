<template>
    <div>
        <el-row>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-document"></i> 系统信息</el-breadcrumb-item>
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
        </el-row>

        <el-row>
            <div class="chart" style="width:440px;hight:280px;margin-left:10px;margin-top:50px">
                <ve-radar :data="chartData"></ve-radar>
            </div>
        </el-row>

        <el-row style="margin-left:950px">
            <el-button @click="gotoHomePage">返回主页</el-button>
        </el-row>
    </div>
</template>

<script>
import status_card from '../../common/status_card.vue'
import axios from 'axios'
import VCharts from 'v-charts'
    export default {
        components: {
            'lee-status-card': status_card,
        },
        data(){
            return {
                appoint_num: 0,
                ongoing_num: 0,

                cardsData: [
                    {
                        count: 0,
                        name: '预约中'
                    },
                    {
                        count: 0,
                        name: '进行中'
                    },
                    {
                        count: 0,
                        name: '已完成'
                    },
                    {
                        count: 0,
                        name: '信用积分'
                    }
                ],
                chartData: {
                    columns: ['ID', '分类标注', '整体标注', '区域标注', '标框标注'],
                    metrics: ['分类标注', '整体标注', '区域标注', '标框标注'],
                    rows:[
                       {'ID': '基准值', '分类标注': 100, '整体标注': 100, '区域标注': 100, '标框标注': 100 }
                    ]
                }
            }
        },

        methods: {
            placeData() {
                var userId = localStorage.getItem('userId');
                console.log(userId);
                axios.post('http://localhost:8080/getPersonalData', {"userId": userId, "password": ''})
                .then(response => {
                    console.log(response);
                    var data = JSON.parse(response.data.data);
                    var taskNumData = data[0];
                    var radarPicData = data[1];
                    this.cardsData[0].count = taskNumData.appointNum;
                    this.cardsData[1].count = taskNumData.ongoingNum;
                    this.cardsData[2].count = taskNumData.finishedNum;
                    this.cardsData[3].count = taskNumData.point;

                    var chartRow = {'ID': '你的标注评分','分类标注': radarPicData.classifyPoint, '整体标注': radarPicData.wholePoint, '区域标注': radarPicData.regionPoint, '标框标注': radarPicData.framePoint};
                    // this.chartData.rows[0] = chartRow;
                    console.log(chartRow);
                    this.chartData.rows.push(chartRow);
                })
            },
            gotoHomePage() {
                this.$router.push('homepage');
            }
        },

        mounted() {
            var username = localStorage.getItem('username');
            if(username != null && username.length > 0) {

            }else {
                this.$message('请先登陆');
                this.$router.push('login');
            }

            this.placeData();


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
