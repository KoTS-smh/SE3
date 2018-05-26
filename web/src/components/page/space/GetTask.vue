<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 表格</el-breadcrumb-item>
                <el-breadcrumb-item>接取任务</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-row>
        <el-col :span="8" class="taskInfos"><div class="grid-content bg-purple">

            <el-form :model="taskData" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
                <el-form-item label="任务编号" class="formItems">
                    <el-input v-model="taskData.taskId" readonly></el-input>
                </el-form-item>
                <el-form-item label="任务名称" class="formItems">
                    <el-input v-model="taskData.taskname" readonly></el-input>
                </el-form-item>
                <el-form-item label="发起人" class="formItems">
                    <el-input v-model="taskData.sponsorName" readonly></el-input>
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
                <el-form-item label="任务进度" class="formItems">
                    <el-progress type="circle" :percentage="0"></el-progress>
                </el-form-item>


            </el-form>
        </div></el-col>
        <el-col :span="16"><div class="grid-content bg-purple-light" >
            <el-carousel height="400px">
            <el-carousel-item v-for="item in imgList" :key="item" >
                <img :src="item" alt="">
            </el-carousel-item>
            </el-carousel>
        </div></el-col>
        </el-row>

        <el-row>
            <el-button type="primary" @click="getTask()">接受任务</el-button>
            <el-button type="success">开始标注</el-button>
        </el-row>

    </div>


</template>

<script>
import upload from '../../common/upload.vue'
import axios from 'axios'
export default {
    name: 'app',
    components: {
            upload
    },
    data() {

        return {
            readonly: true,
            currentDate: new Date(),
            imgList: [
                'http://p0.ifengimg.com/pmop/2017/0925/2D6C95104267D4F27325E7502509FF0BCB67304F_size1443_w479_h314.gif',
                'https://up.enterdesk.com/edpic_360_360/23/c5/a5/23c5a544259db587a535efaaba046187.jpg'
            ],
            value5: '',
            taskData: {
                taskname: '任务1',
                sponsorName: 'tony王',
                beginDate: '2018/4/6 10:27',
                endDate: '2018/5/6 10:27',
                tagType: '标框标注',
                taskLevel: 2,
                totalPoint: 20,
                taskId: '2'
            },
            rules: {

            }
        }
    },
    methods: {
        getTask() {
            var acceptUserId = localStorage.getItem("acceptUserId");
            var taskId = this.taskData.taskId;
            axios.post("http://localhost:8080/taskOrder/createTaskOrder", {"acceptUserId": acceptUserId, "taskId": taskId})
            .then(response => {
                console.log(response.data)
            })
        }
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

  .forPics {
         /* line-height: 20px; */
     }

  .taskInfos {
      /* line-height: 40px; */
  }

  .formItems {
      width: 300px;
      vertical-align: middle;
  }

    .block,
    .viewimg {
        margin: 15px;
    }
    .btn {
        padding: 10px 10px;
        border: 0 solid #ccc;
        border-radius: 3px;
    }
    .viewimg {
        max-width: 200px;
        border: 1px dashed #ccc;
        border-radius: 4px;
        padding: 5px;
        background-color: #ccc;
    }
</style>
