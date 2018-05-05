<template>
    <div class="table">
        <el-header style="text-align: right; font-size: 15px">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">个人中心</el-menu-item>
           <!--<img class="user-logo" src = "http://img06.tooopen.com/images/20160921/tooopen_sy_179583447187.jpg">  -->
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
            </el-form>
        </div>
            <div v-show="showAccept">
            <el-row >
                <el-button type="primary" class="choiceBtn" @click="acceptTask()">接受任务</el-button>
                <el-button type="success" class="choiceBtn" @click="startAnno()">开始标注</el-button>
            </el-row>
            </div>
            <div v-show="showInfo">
            <el-row>
                <el-button type="primary" class="choiceBtn" @click="toTaskInfo()">查看任务进度</el-button>
            </el-row>
            </div>
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
let task;
export default {
    name: 'app',
    components: {
            upload
    },
    data() {
        return {
            showAccept:true,
            showInfo:false,
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
                taskId: '3',
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
                if(response.data.code === 0) {
                    var indata = response.data.data;
                    task = indata;
                    this.taskData.taskId = indata.taskId;
                    this.taskData.taskname = indata.taskname;
                    this.taskData.taskLevel = indata.taskLevel;
                    if (indata.annotationType === 'option1') {
                        this.taskData.tagType = '标框标注'
                    } else if (indata.annotationType === 'option2') {
                        this.taskData.tagType = "分类标注"
                    } else if (indata.annotationType === 'option3') {
                        this.taskData.tagType = "区域标注"
                    } else if (indata.annotationType === "option4") {
                        this.taskData.tagType = "整体标注"
                    } else {
                        this.taskData.tagType = "未定义"
                    }
                    this.taskData.sponsorId = indata.postUserId;
                    this.taskData.beginDate = (new Date(indata.beginDate)).Format("yyyy-MM-dd hh:mm:ss");
                    this.taskData.endDate = (new Date(indata.endDate)).Format("yyyy-MM-dd hh:mm:ss");
                    this.taskData.totalPoint = indata.totalPoints;
                    for (let i = 0; i < indata.imgUrlList.length; i++) {
                        this.images.push({src: indata.imgUrlList[i]})
                    }
                    if (indata.postUserId == localStorage.getItem("userId")) {
                        this.showInfo = true;
                        this.showAccept = false;
                    }
                }else{
                    //todo 转到‘任务不见了’页面
                }
            }).catch(() =>{
                //todo 同上
            })
            },
            startAnno(){
                let taskOrderId;
                let submited;
                axios.post("http://localhost:8080/taskOrder/getAll",{
                    userId:localStorage.getItem("userId")
                }).then((response)=>{
                   if(response.data.code === 0){
                       for(let i=0;i<response.data.data.length;i++){
                           if(response.data.data[i].taskId === this.taskData.taskId){
                               taskOrderId = response.data.data[i].taskOrderId;
                               submited = response.data.data[i].submited;
                               break;
                           }
                       }
                       if(taskOrderId == null){
                           this.$message.info("任务还未接受！")
                       }else {
                           if(this.taskData.tagType == '标框标注'){
                               if(!submited) {
                                   this.$router.push({
                                       path: "/annotation/rect", query: {
                                           taskOrderId: taskOrderId
                                       }
                                   })
                               }else {
                                   this.$router.push({
                                       path: "/annotation/rate/rect", query: {
                                           taskOrderId: taskOrderId,
                                           userId:localStorage.getItem("userId")
                                       }
                                   })
                               }
                           }
                           else if(this.taskData.tagType == "分类标注"){
                               if(!submited) {
                                   this.$router.push({
                                       path: "/annotation/classified", query: {
                                           taskOrderId: taskOrderId
                                       }
                                   })
                               }else {
                                   this.$router.push({
                                       path: "/annotation/rate/classified", query: {
                                           taskOrderId: taskOrderId,
                                           userId:localStorage.getItem("userId")
                                       }
                                   })
                               }
                           }
                           else if(this.taskData.tagType == "区域标注"){
                               if(!submited) {
                                   this.$router.push({
                                       path: "/annotation/region", query: {
                                           taskOrderId: taskOrderId
                                       }
                                   })
                               }else {
                                   this.$router.push({
                                       path: "/annotation/rate/region", query: {
                                           taskOrderId: taskOrderId,
                                           userId:localStorage.getItem("userId")
                                       }
                                   })
                               }
                           }
                           else if(this.taskData.tagType == "整体标注"){
                               if(!submited) {
                                   this.$router.push({
                                       path: "/annotation/all", query: {
                                           taskOrderId: taskOrderId
                                       }
                                   })
                               }else {
                                   this.$router.push({
                                       path: "/annotation/rate/all", query: {
                                           taskOrderId: taskOrderId,
                                           userId:localStorage.getItem("userId")
                                       }
                                   })
                               }
                           }
                       }
                   }else{
                       this.$message.info("任务还未接受！")
                   }
                }).catch(()=>{
                    this.$message.error("网络异常！")
                });
            },
            toTaskInfo(){
                //todo
            },
            acceptTask(){
                let isAccepted = false;
                for(let i=0;i<task.acceptUserIds.length;i++){
                    if(task.acceptUserIds[i] == localStorage.getItem("userId")){
                        isAccepted = true;
                        break;
                    }
                }
                if(!isAccepted) {
                    axios.post("http://localhost:8080/taskOrder/createTaskOrder", {
                        taskId: this.taskData.taskId,
                        userId: localStorage.getItem("userId")
                    }).then((response) => {
                        if (response.data.code === 0) {
                            this.$message.success('接受成功！');
                            task.acceptUserIds.push(localStorage.getItem("userId"))
                        } else {
                            this.$message.error(response.data.msg)
                        }
                    }).catch(() => {
                        this.$message.error('网络异常！')
                    })
                }else {
                    this.$message.warning("已经接受过任务了！")
                }
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
    Date.prototype.Format = function(fmt)
    {
        let o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(let k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length===1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
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
