<template>
    <div class="table">
        <el-header style="text-align: right; font-size: 15px">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">个人中心</el-menu-item>
           <!--<img class="user-logo" src="http://img06.tooopen.com/images/20160921/tooopen_sy_179583447187.jpg">  -->
            <span style="color: dodgerblue;position:absolute;top: 18px;right: 10px">{{myName}}</span>
        </el-menu>
        </el-header>

        <el-row>
        <el-col :span="8" class="taskInfos" style="border-right: gainsboro solid 1px"><div class="grid-content bg-purple">

            <el-form :model="taskData" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="任务编号" class="formItems">
                    <el-input v-model="taskData.taskId" readonly></el-input>
                </el-form-item>
                <el-form-item label="任务名称" class="formItems">
                    <el-input v-model="taskData.taskname" readonly></el-input>
                </el-form-item>
                <el-form-item label="发起人编号" class="formItems">
                    <el-input v-model="taskData.sponsorId" readonly></el-input>
                </el-form-item>
                <el-form-item label="发起时间" class="formItems">
                    <el-input v-model="taskData.beginDate" readonly></el-input>
                </el-form-item>
                <el-form-item label="截止时间" class="formItems">
                    <el-input v-model="taskData.endDate" readonly></el-input>
                </el-form-item>
                <el-form-item label="标注类型" class="formItems">
                    <el-input v-model="taskData.tagType" readonly></el-input>
                </el-form-item>
                <el-form-item label="任务级别" class="formItems">
                    <el-input v-model="taskData.taskLevel" readonly></el-input>
                </el-form-item>
                <el-form-item label="奖励积分" class="formItems">
                    <el-input v-model="taskData.totalPoint" readonly></el-input>
                </el-form-item>
                <!--<el-form-item label="任务进度" class="formItems">-->
                    <!--<el-progress type="circle" :percentage="0" v-model="taskData.degree"></el-progress>-->
                <!--</el-form-item>-->


            </el-form>
        </div>
            <el-row>
                <el-button type="primary" class="choiceBtn" @click="acceptTask()">接受任务</el-button>
                <el-button type="success" class="choiceBtn" @click="startAnno()">开始标注</el-button>
            </el-row>

        </el-col>

        <el-col :span="16">

            <lightbox :images="images">

            </lightbox>

        </el-col>

        </el-row>

    </div>


</template>

<script>
import upload from '../../common/upload.vue'
import lightbox from 'vlightbox'
import axios from 'axios'
export default {
    name: 'app',
    components: {
            upload
    },
    data() {
        return {
            myName:'',
            readonly: true,
            currentDate: new Date(),
            activeIndex: '1',
            value5: '',
            taskData: {
                taskname: '任务1',
                sponsorId: 'tony王',
                beginDate: '2018/4/6 10:27',
                endDate: '2018/5/6 10:27',
                tagType: '',
                taskLevel: 2,
                totalPoint: 20,
                taskId: '1',
                degree: 10
            },
            rules: {

            },
            images: []
        }
    },
    methods: {
            getTask() {
            var userId = localStorage.getItem("userId");
            var taskId = this.taskData.taskId;
            axios.post("http://localhost:8080/task/taskInfo", {"taskId": taskId})
            .then((response) => {
                console.log(response.data.data);
                var indata = response.data.data;
                this.taskData.taskId = indata.taskId;
                this.taskData.taskname = indata.taskname;
                this.taskData.taskLevel = indata.taskLevel;
                console.log(indata.annotationType);
                if(indata.annotationType === 'option1'){
                    this.taskData.tagType = "标框标注"
                }else if(indata.annotationType === 'option2'){
                    this.taskData.tagType = "分类标注"
                }else if(indata.annotationType === 'option3'){
                    this.taskData.tagType = "区域标注"
                }else if(indata.annotationType === "option4" ){
                    this.taskData.tagType = "整体标注"
                }else {
                    this.taskData.tagType = "未定义"
                }
                this.taskData.sponsorId = indata.postUserId;
                this.taskData.beginDate = new Date(indata.beginDate).toDateString();
                this.taskData.endDate = new Date(indata.endDate).toDateString();
                this.taskData.totalPoint = indata.totalPoints;
                for(let i=0;i<indata.imgUrlList.length;i++){
                    this.images.push({src:indata.imgUrlList[i]})
                }
                console.log(this.images)
            }).catch(() =>{
            })
            },
            startAnno(){

            },
            acceptTask(){

            },
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
                if(key === 1){
                    this.$router.push({path: '/guide'})
                }else if(key === 2){
                    this.$router.push({path: '/personalSpace'})
                }
            }

        },
    mounted() {
        this.myName = localStorage.getItem("username");
        this.getTask();
    }


}
</script>


<style>
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
      clear: both;
  }

  .formItems {
      width: 300px;
      vertical-align: middle;
      padding: 0px 0px 0px 50px;
  }

    .block,
    .viewimg {
        margin: 15px;
    }
    .btn {
        padding: 10px 10px;
        border: 0px solid #ccc;
        border-radius: 3px;
    }
    .viewimg {
        max-width: 200px;
        border: 1px dashed #ccc;
        border-radius: 4px;
        padding: 5px;
        background-color: #ccc;
    }

    .title{
        text-align: center;
    }

    .choiceBtn {
        margin: 0px 0px 0px 80px;
    }

    .demo-ruleForm {
        margin: 20px 0px 0px 0px;
    }

    .user-logo {
        left:0;
        top:15px;
        width:40px;
        height:40px;
        border-radius: 50%;
        margin-left: 1000px;
        margin-top: 5px;

    }

    .icon {
        float: right;
    }

</style>
