<template>
    <div class="register-wrap">
        <div class="ms-title">后台管理系统</div>
        <div class="ms-register" id="register-father">
            <!--信息填写区域-->
            <div class="register-field">
                <!--表单-->
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                    <!--用户名填写区域-->
                        <el-form-item prop="username">
                            <el-input v-model="ruleForm.username" placeholder="username"></el-input>
                        </el-form-item>
                    <!--密码填写区域-->
                        <el-form-item prop="password">
                            <el-input v-model="ruleForm.password" placeholder="password" type="password"></el-input>
                        </el-form-item>
                    <!--密码确认区域-->
                        <el-form-item prop="passwordConfirm">
                            <el-input v-model="ruleForm.passwordConfirm" placeholder="passwordConfirm" type="password"></el-input>
                        </el-form-item>
                    <!--手机填写区域-->
                        <el-form-item prop="phone">
                            <el-input v-model="ruleForm.tel_number" placeholder="phone"></el-input>
                        </el-form-item>
                </el-form>
                <!--注册按钮-->
                <div class="register-btn">
                    <el-button icon="icon-person_add" type="primary" @click="register()" style="width: 100%">  注册</el-button>
                </div>
                <!--取消按钮-->
                <div class="cancel-btn">
                    <el-button icon="icon-exit" type="primary" @click="goBack" style="width: 100%">  取消</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
    export default {
        name: "Register",
        data: function(){
            var checkUsername = (rule,value,callback)=>{
                if(!value){
                    return callback(new Error('用户名不能为空'))
                }
                // setTimeout(()=>{
                //     if(value.length<5){
                //         callback(new Error('用户名不能少于五位数'))
                //     }
                // })
            };
            var checkPassword = (rule,value,callback)=>{
                if(value===''){
                    callback(new Error('密码不能为空'))
                }else{
                    if(this.ruleForm.passwordConfirm !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var checkPasswordConfirm = (rule,value,callback) => {
                if(value===''){
                    callback(new Error('请再次输入密码'))
                }
                else if(value !== this.ruleForm.password){
                    callback(new Error('两次输入密码不一致！'));
                }
                else{
                    callback();
                }
            };
            var checkPhone = (rule,value,callback) => {
                if(value===''){
                    callback(new Error('请输入手机号'))
                }
            };
            return {
                ruleForm: {
                    username: '',
                    password: '',
                    passwordConfirm:'',
                    tel_number:''
                },
                rules: {
                    username: [
                        { validator:checkUsername, trigger: 'blur' }
                    ],
                    password: [
                        { validator:checkPassword, trigger: 'blur' }
                    ],
                    passwordConfirm:[
                        { validator:checkPasswordConfirm,trigger:'blur'}
                    ],
                    tel_number:[
                        { validator:checkPhone,trigger:'blur'}
                    ]
                }
            }
        },
        methods: {
            register() {
                const self = this;
                //judge before register

                axios.post('http://localhost:8080/user/register', this.ruleForm).then(function(response){
                    console.log(response);
                    self.success();
                    self.sleep(1000).then(() => {
                        self.$router.push('/login')
                    });

                }).catch(function(err){
                    console.log(err);
                    self.failed();
                })

            },

            goBack(){
                this.$confirm('您将返回离开注册界面，是否确定？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    window.history.length>1
                        ?this.$router.go(-1)
                        :this.router.push('/guide')
                })
            },

            success() {
                this.$message('注册成功,请登陆');
            },
            failed() {
                this.$message('注册失败，请检查网络情况')
            },
            sleep(time) {
                return new Promise((resolve) => setTimeout(resolve, time));
            }
        }
    }
</script>

<style scoped>
    #register-father{
        display: flex;
    }
    .register-wrap {
        position:relative;
        width: 100%;
        height: 100%;
        background-image: url('https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-118824.jpg');
        background-attachment: fixed;
        background-repeat: no-repeat;
        background-size: cover;
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
    .ms-register{
        position: absolute;
        left:50%;
        top:50%;
        width:300px;
        height:320px;
        margin:-150px 0 0 -190px;
        padding:40px;
        border-radius: 5px;
        background: #fff;
    }
    .register-icon{
        width:20px;
        height:116px;
        border:none;
    }
    .register-field{
        margin:0 0;
        padding:0 0;
        border:none;
        width:265px;
        height:116px;
    }
    .register-btn{
        margin: 0 0;
        padding: 0 0;
        border:none;
        width:265px;
        height:40px;
    }
    .cancel-btn{
        margin:20px 0;
        padding:0 0;
        border:none;
        width:265px;
        height:40px;
    }
</style>
