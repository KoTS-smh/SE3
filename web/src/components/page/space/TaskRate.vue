<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-service"></i> 任务进度</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div>
            <el-table :data="tableData" style="width:100%" border :row-class-name="tableRowClassName">
                <el-table-column prop="acceptUserId" label="编号" width="180" sortable></el-table-column>
                <el-table-column prop="acceptUserName" label="用户名" width="180"></el-table-column>
                <el-table-column prop="rate" label="进度" width="180" sortable></el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data:function () {
            return {
                tableData:[""]
            }
        },
        mounted:function () {
            this.getTaskRateMessage();
        },
        methods: {
            getTaskRateMessage(){
                const self = this;
                axios.get("http://localhost:8080/getTaskMessage", {
                    params:{
                        // taskId:this.$router.query.taskId todo 接受taskId
                        taskId:0
                    }
                }).then(response => {
                    console.log("获取中...");
                    console.log(response.data.data);
                    self.tableData = response.data.data;
                }).catch(error => {
                    self.$message("获取失败");
                })
            },
            tableRowClassName({row,rowIndex}){
                console.log(rowIndex);
                if(rowIndex ===1)
                    return 'success-row';
                else
                    return 'warning-row';
            }
        },
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
