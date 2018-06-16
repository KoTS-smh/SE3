<template>
    <div class="all" id="all">
            <div class="crumbs">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item><i class="el-icon-menu"></i>个人信息</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="form-box">
                <!--头像显示修改区域-->
                <el-form ref="form" :model="form" label-width="80px">
                    <!--ID显示区域-->
                    <el-form-item label="ID">
                        {{form.userId}}
                    </el-form-item>
                    <!--用户名的显示修改区域-->
                    <el-form-item label="用户名">
                        <el-input v-model="form.username" :disabled="true" style="width:50%"></el-input>
                    </el-form-item>
                    <!--性别选择区域-->
                    <el-form-item label="性别">
                        <el-radio-group v-model="form.sex">
                            <el-radio label="Man"></el-radio>
                            <el-radio label="Woman"></el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <!--学历选择区域-->
                    <el-form-item label="职业">
                        <el-select v-model="form.profession" placeholder="请选择">
                            <el-option key="STUDENT" label="学生" value="STUDENT"></el-option>
                            <el-option key="TEACHER" label="教师" value="TEACHER"></el-option>
                            <el-option key="DOCTOR" label="医生" value="DOCTOR"></el-option>
                            <el-option key="ENGINEER" label="工程师" value="ENGINEER"></el-option>
                            <el-option key="DESIGNER" label="设计师" value="DESIGNER"></el-option>
                            <el-option key="CHEF" label="厨师" value="CHEF"></el-option>
                            <el-option key="GEOGRAPHER" label="地理学家" value="GEOGRAPHER"></el-option>
                        </el-select>
                    </el-form-item>
                    <!--手机号修改区域-->
                    <el-form-item label="手机号">
                        <el-input v-model="form.telPhone"></el-input>
                    </el-form-item>
                    <!--邮箱输入区域-->
                    <el-form-item label="邮箱">
                        <el-input v-model="form.email">
                        </el-input>
                    </el-form-item>
                    <!--积分显示区域-->
                    <el-form-item label="积分">
                        {{form.point}}
                    </el-form-item>
                    <!--余额显示区域-->
                    <el-form-item label="余额">
                        {{form.balance}}
                    </el-form-item>
                    <!--等级显示区域-->
                    <el-form-item label="等级">
                        {{form.userLevel}}
                    </el-form-item>
                    <!--修改按钮区域-->
                    <el-form-item label="修改信息">
                        <el-switch v-model="form.delivery"></el-switch>
                    </el-form-item>
                    <!--个人简历填写区域-->
                    <el-form-item label="个人简介">
                    <el-input type="textarea" v-model="form.description"></el-input>
                    </el-form-item>
                    <el-form-item>
                    <el-button type="primary" @click="saveUserMessage">保存修改</el-button>
                    <el-button>取消修改</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data: function(){
            return {
                form: {
                    userId:'',
                    username: '',
                    profession: '',
                    sex: '',
                    description: '',
                    telPhone:'',
                    email:'',
                    point:'',
                    userLevel:'',
                    password:'',
                    balance: ''
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
            this.getUserMessage();
        },
        methods: {
            //todo 加载当前登录用户信息
            getUserMessage(){
                const self = this;
                var userId = localStorage.getItem("userId");
                axios.get('http://localhost:8080/user/getUser',{
                    params:{
                        userId: userId
                    }
                }).then(response => {
                    console.log(response);
                    this.form = response.data.data;
                }).catch(()=>{
                    this.$message('网络异常！');
                })
            },
            saveUserMessage(){
                const self = this;

                axios.post('http://localhost:8080/user/update',this.form).then(response => {
                    console.log(response);
                    self.$message('修改成功');
                }).catch( (err)=> {
                    self.$message('修改失败');
                })
            },
            onSubmit() {
                this.$message.success('提交成功！');
            }
        //  todo 点击switch会更改输入框的禁止状态
        //  todo
        }
    }
</script>

<style scoped>
    .form-box{
        width:600px;
    }
    .el-select{
        width:130px;
    }
</style>
