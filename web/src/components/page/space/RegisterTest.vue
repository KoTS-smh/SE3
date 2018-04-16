<template>
    <div class="login-wrap">
        <div class="ms-title">注册页面</div>
        <div class="ms-login">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  label-width="0px" class="demo-ruleForm">
                <el-form-item prop="username">
                    <el-input v-model="ruleForm.username" placeholder="username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="ruleForm.password" type="password" placeholder="password"></el-input>
                </el-form-item>
                <el-form-item prop="password_again">
                    <el-input v-model="ruleForm.password_again" type="password" placeholder="password again"></el-input>
                </el-form-item>
                <el-form-item prop="tel_number">
                    <el-input v-model="ruleForm.tel_number" placeholder="telephone number"></el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="register()">注册</el-button>
                </div>
                <p style="font-size:12px;line-height:30px;color:#999;">Tips : 用户名和密码随便填。</p>
            </el-form>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data: function(){
        return {
            ruleForm: {
                    username: '',
                    password: '',
                    password_again: '',
                    tel_number: ''
                },
            rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                    password_again: [
                        {required: true, message: '请再次输入密码', trigger: 'blur'}
                    ],
                    tel_number: [
                        {required: true, message: '请输入电话号码', trigger: 'blur'}
                    ]
                }
        }
    },
    methods: {
        register(){
            const self = this;
            //judge before register
            if(this.ruleForm.password != this.ruleForm.password_again){
                self.open();
            }else{
                axios.post('http://localhost:8080/user/register', this.ruleForm).then(function(response){
                console.log(response)
                }).catch(function(err){
                    console.log(err)
                })
            }
            
        },
        open() {
                this.$message('两次输入密码不一致');
        }
    }
}
</script>



<style scoped>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
    }
    .ms-title{
        position: absolute;
        top:50%;
        width:100%;
        margin-top: -230px;
        text-align: center;
        font-size:30px;
        color: #fff;

    }
    .ms-login{
        position: absolute;
        left:50%;
        top:50%;
        width:300px;
        height:300px;
        margin:-150px 0 0 -190px;
        padding:40px;
        border-radius: 5px;
        background: #fff;
    }
    .login-btn{
        text-align: center;
    }
    .login-btn button{
        width:100%;
        height:36px;
    }
</style>
