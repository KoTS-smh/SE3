<template>
    <div class="login-wrap">
        <div class="ms-title">MRGS众包标注系统</div>
        <div class="ms-login">
            <div class="login-field">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                    <el-form-item prop="username">
                        <el-input v-model="ruleForm.username" placeholder="username">
                        </el-input>
                    </el-form-item>
                        <el-form-item prop="password">
                            <el-input type="password" placeholder="password" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')" ></el-input>
                        </el-form-item>
                    <!--登录按钮-->
                        <div class="login-btn" >
                            <el-button type="primary" @click="submitForm('ruleForm')" style="width:100%">登录</el-button>
                            <!--<el-button type="primary" @click="cancel" style="width:100%">取消</el-button>-->
                        </div>
                    <!--取消按钮-->
                        <div class="cancel-btn">
                            <el-button type="primary" @click="goBack" style="width:100%">取消</el-button>
                        </div>
                    <!--通往register界面-->
                        <div class="login-to-register" style="height: 30px">
                            <a href="/#/register" style="text-decoration: underline ;margin-top:10px;color: #999;font-size:12px;line-height:30px">注册一个新的账户</a>
                        </div>
                </el-form>
            </div>
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
                    password: ''
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            submitForm(formName) {
                const self = this;
                self.$refs[formName].validate((valid) => {
                    if (valid) {
                        localStorage.setItem('ms_username',self.ruleForm.username);
                        //self.$router.push('/readme');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
                var username = this.ruleForm.username;
                console.log(username);
                axios.post('http://localhost:8080/user/login', this.ruleForm).then(function(response){
                   if(response.data.code !== 0){
                       self.open();
                   }else{
                       localStorage.setItem("username", username);
                       localStorage.setItem("userId", response.data.data.userId);
                       self.$router.push('/readme');
                   }
                }).catch(function(err){
                    console.log(err);
                })
            },
            open() {
                this.$message('用户名或密码错误');
            },
            goBack(){
                this.$confirm('您将返回离开登录界面，是否确定？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    window.history.length>1
                        ?this.$router.go(-1)
                        :this.router.push('/guide')
                })
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
        height:220px;
        margin:-150px 0 0 -190px;
        padding:40px;
        border-radius: 5px;
        background: #fff;
    }
    .login-btn{
        text-align: center;
        height:40px;
    }
    .cancel-btn{
        text-align:center;
        width:300px;
        height:40px;
        margin-top:10px
    }

</style>
