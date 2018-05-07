<template>
    <div>
        <div class="crumbs">
        <el-breadcrumb separator="/">
            <el-breadcrumb-item><i class="el-icon-message"></i>系统信息</el-breadcrumb-item>
        </el-breadcrumb>
        </div>
        <div style="margin-left: 50px">
            <el-form style="margin-top: 40px">
                <!--显示用户数-->
                <el-form-item label="用户数目">
                    {{form.userNumber}}
                </el-form-item>
                <!--显示任务数-->
                <el-form-item label="任务数目">
                    {{form.taskNumber}}
                </el-form-item>
                <!--显示已完成任务数-->
                <el-form-item label="已完成任务数目">
                    {{form.finishedTaskNumber}}
                </el-form-item>
                <!--显示未完成任务数-->
                <el-form-item label="未完成任务数目">
                    {{form.unfinishedTaskNumber}}
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data:function () {
            return {
                form:{
                    userNumber:0,
                    taskNumber:0,
                    finishedTaskNumber:0,
                    unfinishedTaskNumber:0
                }
            }
        },
        mounted:function(){
            var username = localStorage.getItem('username')
            if(username != null && username.length > 0) {
                //donothing
            }else {
                this.$message('请先登陆');
                this.$router.push('login');
            }
            this.getSystemMessage();
        },
        methods:{
            getSystemMessage(){
                const self = this;
                console.log("获取中...");
                axios.get("http://localhost:8080/getSystemMessage").then(response =>{
                    console.log(response.data.data);
                    self.form = response.data.data;
                }).catch(error=>{
                    self.$message("获取失败")
                })
            }
        }
    }
</script>

<style scoped>

</style>
