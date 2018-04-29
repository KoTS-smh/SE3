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

            <el-button type="primary">新建</el-button>
        </div>

        <el-table :data="tableData" border style="width: 100%" ref="multipleTable" >
            <el-table-column prop="taskId" label="任务Id" width="70"></el-table-column>
            <el-table-column prop="taskname" label="任务名称" width="100"></el-table-column>
            <el-table-column prop="postUserId" label="上传者ID" width="100"></el-table-column>
            <el-table-column prop="taskLevel" label="任务等级" width="100"></el-table-column>
            <el-table-column prop="totalPoints" label="任务积分" width="100"></el-table-column>
            <el-table-column prop="maxParticipator" label="最大参与者数量" width="140"></el-table-column>
            <el-table-column prop="beginDate" label="开始时间" width="140"></el-table-column>
            <el-table-column prop="endDate" label="截止时间" width="140"></el-table-column>

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
            <el-form :model="selectTable">
                <el-form-item label="任务ID" label-width="100px">
                    <el-input v-model="selectTable.taskId" auto-complete="off" readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="任务名称" label-width="100px">
                    <el-input v-model="selectTable.taskname" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="截止时间" label-width="100px">
                    <el-input v-model="selectTable.endDate" auto-complete="off"></el-input>


                </el-form-item>
                <el-form-item label="任务等级" label-width="100px">
                    <el-input v-model="selectTable.taskLevel" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="任务积分" label-width="100px">
                    <el-input v-model="selectTable.totalPoints" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="最大参与者数" label-width="100px">
                    <el-input v-model="selectTable.maxParticipator" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>

             <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveChange()">确 定</el-button>
                <!--<el-button type="success" @click="publish()">发布</el-button>-->
            </div>
        </el-dialog>
    </div>
</template>


<script>
import axios from "axios"

export default {
    data() {
        return {
            tableData: [],
            dialogVisible: false,
            selectTable: {},
            select_cate: ''
        }
    },
    methods: {
        handleSelectionChange(val) {
                this.multipleSelection = val;
        },
        handleEdit(index, row){
            this.selectTable = row;
            this.address = row.address;
            this.dialogVisible = true;
        },
        handleDelete(index, row){
            if(this.tableData[index].acceptUserIds.length === 0){
                axios.get("http://localhost:8080/task/delete",{
                    params:{
                        taskId:row.taskId
                    }
                }).then((response)=>{
                    if(response.data.code ===0){
                        this.$message.success("删除成功！");
                        this.placeData();
                    }else {
                        this.$message.error("删除失败！")
                    }
                }).catch(()=>{
                    this.$message.error("网络异常！")
                })
            }else {
                this.$message.info("该任务已有人参与，无法删除！")
            }
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
                axios.post("http://localhost:8080/task/getAllFinished", {"userId": userId, "password": ""})
                .then(response => {
                    mydata = JSON.parse(response.data.data);
                    var i = 0;
                    for(i = 0;i < mydata.length;++i){
                        mydata[i].endDate = this.convertDate(mydata[i].endDate);
                        mydata[i].beginDate = this.convertDate(mydata[i].beginDate)
                    }
                    this.tableData = mydata;
                }).catch(err => {
                    console.log(err)
                })
            }else if(selection === "已结束"){
                axios.post("http://localhost:8080/task/getAllunFinished", {"userId": userId, "password": ""})
                .then(response => {
                    console.log(response);
                    mydata = JSON.parse(response.data.data);
                    var i = 0;
                    for(i = 0;i < mydata.length;++i){
                        mydata[i].endDate = this.convertDate(mydata[i].endDate);
                        mydata[i].beginDate = this.convertDate(mydata[i].beginDate)
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
                var mydata = JSON.parse(response.data.data);

                var i = 0;
                for(i = 0;i < mydata.length;++i){
                    mydata[i].endDate = this.convertDate(mydata[i].endDate);
                    mydata[i].beginDate = this.convertDate(mydata[i].beginDate)
                }


                this.tableData = mydata
            }).catch(err => {
                console.log(err)
            })
        },
        convertDate(indate) {
        var date = new Date(indate);
        var year = date.getFullYear() + '-';
        var month = (date.getMonth() + 1) + '-';
        var day = date.getDate() - 1;

        var hour = date.getUTCHours();

        hour = hour - 6;
        if(hour <= 0){
            hour = hour + 24;
            day = day - 1;
        }
            day = day + ' ';
            hour = hour + ':';
            var minute = date.getMinutes();

        return year + month + day + hour + minute;
        }
    },

    mounted() {
        this.placeData()
    }
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
