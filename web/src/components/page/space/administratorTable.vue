<template>
    <div>
        <h2>系统信息</h2>
        <el-form style="margin-top: 40px;">
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
