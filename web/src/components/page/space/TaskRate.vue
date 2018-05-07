<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-service"></i> 任务完成进度</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div>
            <el-form>
                <el-form-item label="任务名称">{{taskId}}</el-form-item>
                <el-form-item label="总进度" label-width="50pt">
                    <el-progress type="line" :text-inside="true" :stroke-width="18" :percentage="totalRate" style="width:100%;margin-top: 10px" ></el-progress>
                </el-form-item>
            </el-form>
        </div>
        <div class="el-table" style="width: 100%">
            <el-table :data="tableData" style="width:100%"  :row-class-name="tableRowClassName" @row-click="change">
                <el-table-column prop="acceptUserId" label="编号" sortable></el-table-column>
                <el-table-column prop="acceptUserName" label="用户名"></el-table-column>
                <el-table-column prop="rate" label="进度" sortable>
                    <template slot-scope="scope">
                        <el-progress type="line" :percentage="scope.row.rate*100"></el-progress>
                    </template>
                </el-table-column>
                <el-table-column prop="state" label="状态"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" type="text" v-show="scope.row.state=='待评审'">评审</el-button>
                        <el-button @click="handleClick(scope.row)" type="text" v-show="scope.row.state!='待评审'" disabled>暂不可操作</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data:function () {
            return {
                taskId:0,
                tableData:[""],
                value:null,
                rateValue:0,
                totalRate:80
            }
        },
        mounted:function () {
            var username = localStorage.getItem('username')
            if(username != null && username.length > 0) {
                //donothing
            }else {
                this.$message('请先登陆');
                this.$router.push('login');
            }
            this.getTaskRateMessage();
        },
        methods: {
            change(row,event,column){
                console.log(row);
                this.rateValue = row.rate*100
            },
            handleClick(row){
                console.log(row)
            },
            getTaskRateMessage(){
                const self = this;
                axios.get("http://localhost:8080/getTaskMessage", {
                    params:{
                        // taskId:this.$route.query.taskId todo 接受taskId
                        taskId:0
                    }
                }).then(response => {
                    self.tableData = response.data.data;
                }).catch(error => {
                    self.$message("获取失败");
                })
            },
            tableRowClassName({row,rowIndex}){
                if(rowIndex ===1)
                    return 'success-row';
                else
                    return 'warning-row';
            }
        }
    }
</script>

<style scoped>
    .el-table .warning-row{
        background:oldlace;
    }

    .el-table .success-row{
        background:#f0f9eb;
    }
</style>
