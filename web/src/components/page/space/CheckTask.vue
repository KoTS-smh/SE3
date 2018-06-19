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
        <el-col :span="6" class="taskInfos" style="border-right: gainsboro solid 1px">
            <div style="width: 90%;background-color: gainsboro;height: 40px;margin-top: 10px;margin-left: 5%;text-align:center">
                <p style="color: #20a0ff; font-size: 1.8rem">基础信息</p>
            </div>
            <div class="grid-content bg-purple">
            <el-form :model="taskData" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="任务名称" class="formItems">
                    <el-input v-model="taskData.taskname" readonly></el-input>
                </el-form-item>
                <el-form-item label="开始时间" class="formItems">
                    <el-input v-model="taskData.beginDate" readonly></el-input>
                </el-form-item>
                <el-form-item label="截止时间" class="formItems">
                    <el-input v-model="taskData.endDate" readonly></el-input>
                </el-form-item>
                <el-form-item label="标注类型" class="formItems">
                    <el-input v-model="taskData.tagType" readonly></el-input>
                </el-form-item>
                <el-form-item label="完成积分" class="formItems">
                    <el-input v-model="taskData.totalPoint" readonly></el-input>
                </el-form-item>
                <el-form-item label="完成报酬" class="formItems">
                    <el-input v-model="taskData.reward + ' ¥'" readonly></el-input>
                </el-form-item>
                <el-form-item v-if="taskData.upRate != null" label="奖励增幅" class="formItems">
                    <el-input v-model="taskData.upRate" readonly></el-input>
                </el-form-item>
            </el-form>
        </div>
        </el-col>
            <el-col :span="8" style="border-right: gainsboro solid 1px">
                <div style="width: 90%;background-color: gainsboro;height: 40px;margin-top: 10px;margin-left: 5%;text-align:center">
                    <p style="color: #20a0ff; font-size: 1.8rem">任务状态: {{taskData.state}}</p>
                </div>
                <p style="margin-top: 6%;margin-left: 10%">目标标注人数： {{taskData.maxParticipator}}</p>
                <p style="margin-top: 6%;margin-left: 10%">预约中人数： {{taskData.appointNum}}</p>
                <p style="margin-top: 6%;margin-left: 10%">标注中人数： {{taskData.annoNum}}</p>
                <p style="margin-top: 5%;margin-left: 10%;font-size: small;color: #73ccff">
                    注：当任务开始时，系统会挑选最合适的人参与任务。
                    <br><br>
                    没被选中也没关系，任务进行中也有机会参与标注。
                    <br><br>
                    注意系统消息提示，不要错过了标注哦。
                </p>
                <div v-show="showAccept" style="margin-top: 8%">
                    <el-row>
                        <el-button type="primary" class="choiceBtn" @click="acceptTask()">接受任务</el-button>
                        <el-button type="success" class="choiceBtn" @click="startAnno()">开始标注</el-button>
                        <el-button type="info" class="choiceBtn" @click="leave()">返回主页</el-button>
                    </el-row>
                </div>
                <div v-show="showInfo" style="margin-top: 8%">
                    <el-row>
                        <el-button type="primary" class="choiceBtn" @click="toTaskInfo()">查看任务进度</el-button>
                    </el-row>
                </div>
            </el-col>
        <el-col :span="10">
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
                taskId: '',
                degree: 10,
                reward: '',
                upRate: '',
                state: '',
                maxParticipator: 0,
                appointNum: 0,
                annoNum: 0
            },
            rules: {
            },
            images: [],
            targetNum: 20
        }
    },
    methods: {
            leave(){
              this.$router.go(-1)
            },
            getTask() {
            var acceptUserId = localStorage.getItem("userId");
            var taskId = this.taskData.taskId;
            axios.post("http://localhost:8080/task/taskInfo", {taskId: taskId})
            .then((response) => {
                console.log(response);
                if(response.data.code === 0) {
                    var indata = response.data.data;
                    task = indata;
                    this.taskData.taskId = indata.taskId;
                    this.taskData.taskname = indata.taskname;
                    this.taskData.taskLevel = indata.taskLevel;
                    this.taskData.maxParticipator = indata.maxParticipator;
                    this.taskData.annoNum = indata.annoNum;
                    this.taskData.appointNum = indata.appointNum;

                    if(indata.state === 'appoint') {
                        this.taskData.state = '预约中'
                    }else if(indata.state === 'ongoing'){
                        this.taskData.state = '进行中'
                    }else if(indata.state === 'finished') {
                        this.taskData.state = '已结束'
                    }

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
                    this.taskData.beginDate = (new Date(indata.beginDate)).Format("yyyy-MM-dd");
                    this.taskData.endDate = (new Date(indata.endDate)).Format("yyyy-MM-dd");
                    this.taskData.totalPoint = indata.totalPoints;
                    this.taskData.reward = indata.reward;
                    this.taskData.upRate = indata.upRate;
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
                if(this.taskData.state=='appoint'){
                   this.$message.info("任务未开始！")
                }else {
                    let taskOrderId;
                    axios.post("http://localhost:8080/taskOrder/getAll", {
                        acceptUserId: localStorage.getItem("userId")
                    }).then((response) => {
                        if (response.data.code === 0) {
                            for (let i = 0; i < response.data.data.length; i++) {
                                if (response.data.data[i].taskId === this.taskData.taskId) {
                                    taskOrderId = response.data.data[i].taskOrderId;
                                    break;
                                }
                            }
                            if (taskOrderId == null) {
                                this.$message.info("不是可标注用户！")
                            } else {
                                if (this.taskData.tagType == '标框标注') {
                                    this.$router.push({
                                        path: "/annotation/rect", query: {
                                            taskOrderId: taskOrderId
                                        }
                                    })
                                }
                                else if (this.taskData.tagType == "分类标注") {
                                    this.$router.push({
                                        path: "/annotation/classified", query: {
                                            taskOrderId: taskOrderId
                                        }
                                    })
                                }
                                else if (this.taskData.tagType == "区域标注") {
                                    this.$router.push({
                                        path: "/annotation/region", query: {
                                            taskOrderId: taskOrderId
                                        }
                                    })
                                }
                                else if (this.taskData.tagType == "整体标注") {
                                    this.$router.push({
                                        path: "/annotation/all", query: {
                                            taskOrderId: taskOrderId
                                        }
                                    })
                                }
                            }
                        } else {
                            this.$message.info("不是可标注用户！")
                        }
                    }).catch(() => {
                        this.$message.error("网络异常！")
                    });
                }
            },
            toTaskInfo(){
                this.$router.push("/publishedTasks");
            },
            acceptTask(){
                let isAccepted = false;
                for(let i=0;i<task.acceptUserIds.length;i++){
                    if(task.acceptUserIds[i] == localStorage.getItem("userId")){
                        isAccepted = true;
                        break;
                    }
                }
                axios.get("http://localhost:8080/taskOrder/getAppoint", {
                    params:{
                        taskId: this.taskData.taskId,
                    }
                }).then((response) => {
                    if (response.data.code === 0) {
                        for(let i=0;i<response.data.data.length;i++){
                            if(response.data.data[i]==localStorage.getItem("userId")){
                                isAccepted=true;
                                break;
                            }
                        }
                    }
                });
                if(!isAccepted) {
                    axios.get("http://localhost:8080/taskOrder/appoint", {
                        params:{
                            taskId: this.taskData.taskId,
                            userId: localStorage.getItem("userId")
                        }
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
        var username = localStorage.getItem('username');
        if(username != null && username.length > 0) {
            //donothing
        }else {
            this.$message('请先登陆');
            this.$router.push('login');
        }
        this.myName = localStorage.getItem("username");
        this.taskData.taskId = this.$route.query.taskId;
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
