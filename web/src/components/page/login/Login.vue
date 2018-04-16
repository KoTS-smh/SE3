<template>
    <div class="login-wrap">
        <div class="ms-title">后台管理系统</div>
        <div class="ms-login" id="login-father">
            <!--<div class="login-icon" id="child01">-->
                <!--&lt;!&ndash;&ndash;&gt;-->
                <!--<div style="height: 10px">-->
                <!--</div>-->
                <!--<div class="user-icon" style="height: 63px">-->
                    <!--<i class="icon-user-tie"></i>-->
                <!--</div>-->
                <!--<div class="key-icon">-->
                    <!--<i class="icon-key"></i>-->
                <!--</div>-->
            <!--</div>-->
            <div class="login-field">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                    <el-form-item prop="username">
                        <el-input v-model="ruleForm.username" placeholder="username">
                        </el-input>
                    </el-form-item>
                        <el-form-item prop="password">
                            <el-input type="password" placeholder="password" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')" ></el-input>
                        </el-form-item>
                    <!--通往register界面-->
                        <div class="login-to-register" style="height: 30px">
                            <a target="_blank" style="color: #5e7382">注册一个新的账户</a>
                        </div>
                    <!--登录按钮-->
                        <div class="login-btn">
                            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                            <el-button type="primary" @click="toRegister">取消</el-button>
                        </div>
                        <p style="font-size:12px;line-height:30px;color:#999;">Tips : 用户名和密码随便填。</p>
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
            },
            toRegister(){
                this.$router.push('/register')
            },
            cancel(){
                this.$confirm('您将返回离开登录界面，是否确定？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    window.history.length>1
                        ?this.$router.go(-1)
                        :this.router.push('/guide')
                })

                var userModel = JSON.stringify(this.ruleForm)
                axios.post('http://localhost:8080/user/login', this.ruleForm).then(function(response){
                   console.log(response);
                   console.log(response.data.code);
                   if(response.data.code != 0){
                       self.open();
                   }else{
                       self.$router.push('/readme');
                   }
                }).catch(function(err){
                    console.log(err);
                })
            },
            open() {
                this.$message('用户名或密码错误');
            }
        }
    }
</script>

<style scoped>
    #login-father{
        display: flex;
    }
    .login-icon{
        /*margin: 0;*/
        /*padding: 0;*/
        width: 20px;
        height: 116px;
        border: none;
    }
    .login-field{
        margin: 0 0;
        padding: 0 0;
        border: none;
        width: 265px;
        height: 116px;
    }
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
        height:180px;
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
