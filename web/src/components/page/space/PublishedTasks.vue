<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 任务</el-breadcrumb-item>
                <el-breadcrumb-item>发布任务列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div class="handle-box">
            <el-select v-model="select_cate" placeholder="筛选任务" class="handle-select mr10" @change="handleChange()">
                <el-option key="1" label="进行中" value="进行中"></el-option>
                <el-option key="2" label="已结束" value="已结束"></el-option>
                <el-option key="3" label="草稿" value="草稿"></el-option>
            </el-select>

            <el-button type="primary" @click="toCreate">新建任务</el-button>
        </div>

        <el-table :data="tableData" style="width: 100%" ref="multipleTable" >
            <!--<el-table-column prop="taskId" label="任务Id" width="70"></el-table-column>-->
            <el-table-column prop="taskname" label="任务名称" width="100"></el-table-column>
            <!--<el-table-column prop="postUserId" label="上传者ID" width="100"></el-table-column>-->
            <el-table-column prop="taskLevel" label="任务等级" width="100"></el-table-column>
            <el-table-column prop="totalPoints" label="任务积分" width="100"></el-table-column>
            <el-table-column prop="maxParticipator" label="最大参与者数量" width="140"></el-table-column>
            <el-table-column prop="reward" label="报酬" width="100"></el-table-column>
            <el-table-column prop="beginDate" label="开始时间" width="185"></el-table-column>
            <el-table-column prop="endDate" label="截止时间" width="185"></el-table-column>

            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button size="small"
                            @click="handleEdit(scope.$index, scope.row)">详情</el-button>
                    <el-button size="small" type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="任务详情" :visible.sync="dialogVisible">
            <div>
                <div>
                    <el-form>
                        <el-form-item label="任务名称">{{taskname}}</el-form-item>
                        <el-form-item label="总进度" label-width="50pt">
                            <el-progress type="line" :text-inside="true" :stroke-width="18" :percentage="totalRate" style="width:100%;margin-top: 10px" ></el-progress>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="el-table" style="width: 100%">
                    <el-table :data="selectTable" style="width:100%">
                        <el-table-column prop="taskOrderId" label="编号" sortable></el-table-column>
                        <el-table-column prop="acceptUsername" label="用户名"></el-table-column>
                        <el-table-column prop="percentage" label="进度" sortable>
                            <template slot-scope="scope">
                                <el-progress type="line" :percentage="scope.row.percentage*100"></el-progress>
                            </template>
                        </el-table-column>
                        <el-table-column prop="state" label="状态"></el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button  type="text" v-show="scope.row.state=='待评审'" @click="handleRate(scope.row)">评审</el-button>
                                <el-button  type="text" v-show="scope.row.state!='待评审'" disabled>暂不可操作</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-dialog>
    </div>
</template>


<script>
import axios from "axios"

export default {
    data() {
        return {
            //父窗口的参数
            tableData: [],
            dialogVisible: false,
            selectTable: [],
            select_cate: '',
            //弹出窗口的参数
            totalRate:0,
            taskname:'',
            taskId:''
        }
    },
    methods: {
        handleSelectionChange(val) {
                this.multipleSelection = val;
        },
        handleEdit(index, row){
            this.address = row.address;
            this.dialogVisible = true;
            this.taskname = row.taskname;
            this.taskId = row.taskId;
            this.getTaskRateMessage(row.taskId);
        },
        calculateTotalRate(){
            var tempData = this.selectTable;
            var tempRate = 0;
            for(var i = 0;i<tempData.length;i++){
                 tempRate += tempData[i].rate;
            }
            this.totalRate = ((tempRate/tempData.length)*100).toFixed(2);
        },
        getTaskRateMessage(taskId){
            const self = this;
            axios.get("http://localhost:8080/getTaskMessage", {
                params:{
                    taskId:taskId
                }
            }).then(response => {
                // self.selectTable = response.data.data;
                // console.log(self.selectTable);
                // self.calculateTotalRate();
                
                var indata = response.data.data;

                for(var i = 0;i < indata.length;++i) {
                    if(indata[i].state == "unSubmitted"){
                    
                    indata[i].state = "标注中"
                    }else if(indata[i].state == "submitted"){
                        indata[i].state = "待评审"
                    }else if(indata[i].state == "fail"){
                        indata[i].state = "失败"
                    }else if(indata[i].state == "finish") {
                        indata[i].state = "审批通过"
                    }else if(indata[i].state == "appoint") {
                        indata[i].state = "预约中"
                    }else if(indata[i].state == "waiting") {
                        indata[i].state = "等待被加入工作"
                    }
                }
                
                

                self.selectTable = indata;

            })
        },
        handleRate(row){
            var type;
            axios.post("http://localhost:8080/task/taskInfo",{
                "taskId": this.taskId
            }).then(response=>{
                console.log(response);
                type = response.data.data.annotationType;
                if(type == 'option1'){
                    this.$router.push({
                        path: "/annotation/rect", query: {
                            taskOrderId: row.taskOrderId,
                            acceptUserId:localStorage.getItem("userId")
                        }
                    })
                }
                else if(type == 'option2'){
                    this.$router.push({
                        path: "/annotation/classified", query: {
                            taskOrderId: row.taskOrderId,
                            acceptUserId:localStorage.getItem("userId")
                        }
                    })
                }
                else if(type == 'option3'){
                    this.$router.push({
                        path: "/annotation/region", query: {
                            taskOrderId: row.taskOrderId,
                            acceptUserId:localStorage.getItem("userId")
                        }
                    })
                }
                else{
                    this.$router.push({
                        path: "/annotation/all", query: {
                            taskOrderId: row.taskOrderId,
                            acceptUserId:localStorage.getItem("userId")
                        }
                    })
                }
            })
        },
        handleDelete(index, row){

                axios.get("http://localhost:8080/task/delete",{
                    params:{
                        taskId:row.taskId
                    }
                }).then((response)=>{
                    console.log(response);
                    if(response.data.code ===0){
                        this.$message.success("删除成功！");
                        this.placeData();
                    }else {
                        this.$message.error(response.data.msg);
                    }
                }).catch(()=>{
                    this.$message.error("网络异常！")
                })

        },
        saveChange() {
            this.dialogVisible = false
            // console.log("here")
            // var tmpData = this.tableData
            // var i = 0
            // var i = 0
            //     for(i = 0;i < tmpData.length;++i){
            //         var beginDate = tmpData[i].beginDate
            //         var endDate = tmpData[i].endDate
            //         beginDate = new Date(beginDate)
            //         console.log(beginDate)
            //         tmpData[i].beginDate = beginDate
            //         endDate = new Date(endDate)
            //         tmpData[i].endDate = endDate
            //     }

            // axios.post("http://localhost:8080/task/update", tmpData)
            // .then(response => {
            //     console.log(response)
            // }).catch(err => {
            //     console.log(err)
            // })
        },
        handleChange() {
            var selection = this.select_cate;
            var userId = localStorage.getItem("userId");
            var mydata;
            if(selection === "进行中"){
                axios.post("http://localhost:8080/task/getAllunFinished", {"userId": userId, "password": ""})
                .then(response => {
                    console.log(response);
                    mydata = response.data.data;
                    var i = 0;
                    for(i = 0;i < mydata.length;++i){
                        mydata[i].endDate = new Date(mydata[i].endDate).Format("yyyy-MM-dd");
                        mydata[i].beginDate =new Date(mydata[i].beginDate).Format("yyyy-MM-dd");
                    }
                    this.tableData = mydata;
                }).catch(err => {
                    console.log(err)
                })
            }else if(selection === "已结束"){
                axios.post("http://localhost:8080/task/getAllFinished", {"userId": userId, "password": ""})
                .then(response => {
                    console.log(response);
                    mydata = response.data.data;
                    var i = 0;
                    for(i = 0;i < mydata.length;++i){
                        mydata[i].endDate = new Date(mydata[i].endDate).Format("yyyy-MM-dd");
                        mydata[i].beginDate =new Date(mydata[i].beginDate).Format("yyyy-MM-dd");
                    }
                    this.tableData = mydata;
                }).catch(err => {
                    console.log(err)
                })
            }
        },
        placeData(){
            var userId = localStorage.getItem("userId");
            axios.post("http://localhost:8080/task/getAllPost", {"userId": userId, "password": ""})
            .then(response => {
                console.log(response);
                var mydata = response.data.data;
                var i = 0;
                for(i = 0;i < mydata.length;++i){
                    mydata[i].endDate = new Date(mydata[i].endDate).Format("yyyy-MM-dd");
                    mydata[i].beginDate =new Date(mydata[i].beginDate).Format("yyyy-MM-dd")
                }
                this.tableData = mydata
            }).catch(err => {
                console.log(err)
            })
        },
        toCreate() {
            this.$router.push('/createTask');
        }
    },

    mounted() {
        var username = localStorage.getItem('username')
        if(username != null && username.length > 0) {
                //donothing
        }else {
            this.$message('请先登陆');
            this.$router.push('login');
        }
        this.placeData()
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
    .handle-box{
        margin-bottom: 20px;
    }
    .handle-select{
        width: 120px;
    }
    .handle-input{
        width: 300px;
        display: inline-block;
    }
</style>
