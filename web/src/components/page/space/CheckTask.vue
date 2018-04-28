<template>
    <div class="table">
       <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">个人中心</el-menu-item>
            <el-menu-item index="3">
            <img class="user-logo" src="http://img06.tooopen.com/images/20160921/tooopen_sy_179583447187.jpg">
            </el-menu-item>

        </el-menu>
        

        <el-row>
        <el-col :span="8" class="taskInfos"><div class="grid-content bg-purple">

            <el-form :model="taskData" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
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
        </div>
        </el-col>

        <el-col span="16">

            <lightbox :images="images">

            </lightbox>

        </el-col>
            
        
       

        </el-row>
        

        <el-row>
            <el-button type="primary" class="choiceBtn" @click="getTask()">接受任务</el-button>
            <el-button type="success" class="choiceBtn" @click="test()">开始标注</el-button>
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
            readonly: true,
            currentDate: new Date(),
            activeIndex: '1',
            imgList: [
                'http://p0.ifengimg.com/pmop/2017/0925/2D6C95104267D4F27325E7502509FF0BCB67304F_size1443_w479_h314.gif',
                'https://up.enterdesk.com/edpic_360_360/23/c5/a5/23c5a544259db587a535efaaba046187.jpg'
            ],
            task: {
                name: '任务1',
                sponsor: 'tony马'

            },
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

            },
            tableData: [
               'https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=1a3d82d42f2dd42a4b0409f9625230d0/314e251f95cad1c86a912b9a753e6709c93d5161.jpg',
               'https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=1a3d82d42f2dd42a4b0409f9625230d0/314e251f95cad1c86a912b9a753e6709c93d5161.jpg'
            ],

            images: [
                {
                    src: 'https://p.upyun.com/docs/cloud/demo.jpg',
                },
                {
                    src: 'https://p.upyun.com/docs/cloud/demo.jpg',
                },
                {
                    src: 'https://p.upyun.com/docs/cloud/demo.jpg'
                },
                {
                    src: 'https://p.upyun.com/docs/cloud/demo.jpg'
                },
                {
                    src: 'https://p.upyun.com/docs/cloud/demo.jpg'
                }
            ]
        }
    },
    methods: {
            getTask() {
            var userId = localStorage.getItem("userId")
            var taskId = this.taskData.taskId
            axios.post("http://localhost:8080/taskOrder/createTaskOrder", {"userId": userId, "taskId": taskId})
            .then(response => {
                console.log(response.data)
            })
            },
            test () {
                console.log(this.$route.query.beginDate)
            },
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
                if(key == 1){
                    this.$router.push({path: '/guide'})
                }else if(key == 2){
                    this.$router.push({path: '/personalSpace'})
                }
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
        /* position: absolute; */
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
