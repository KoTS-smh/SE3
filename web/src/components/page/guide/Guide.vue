<template>
    <div class="background" style="background-image: url('https://images.pexels.com/photos/325193/pexels-photo-325193.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260')">
        <div class="title">
            <el-tabs tab-position="top-right" value="">
                <el-tab-pane label="首页" >
                    <div class="main-page" style="text-align: center">
                        <h1 style="margin-top: 200px;font-size: 50px;color:whitesmoke;"  >欢迎访问众包标注系统</h1>
                        <h2 style="font-size: 20px;font-weight: lighter;color:white" >用有限的时间创造无限的未来</h2>
                        <el-tooltip effect="dark" content="将以游客身份进入" placement="bottom">
                            <el-button round style="margin-top:50px;background-color:Transparent;font-size: 30px;color: honeydew" icon="el-icon-d-arrow-right" @click="toHome">立即体验</el-button>
                        </el-tooltip>
                        <br>
                        <el-button round icon="el-icon-d-arrow-right" style="margin-top:50px;background-color:Transparent;font-size: 30px;color: honeydew;font-family: '微软雅黑'" @click="login">登录注册</el-button>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="了解我们">
                    <el-container>
                        <el-main >
                            <!--小队信息-->
                            <div class="head">

                            </div>
                        </el-main>
                    </el-container>
                </el-tab-pane>
                <el-tab-pane label="加入我们">
                    <el-container>
                        <el-main>
                            <!--未完待续的图片-->
                        </el-main>
                    </el-container>
                </el-tab-pane>
                <el-tab-pane label="帮助">
                    <el-container>
                        <el-main>
                            <!--系统简介-->
                        </el-main>
                    </el-container>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div >
</template>

<script>
    export default {
        name: "Guide",
        methods:{
            login(){
                this.$router.push('/login')
            },
            toHome(){
                this.$router.push('/homepage')
            }
        }
    }
</script>

<style scoped>
    .background{
        background-attachment: fixed;
        background-repeat: no-repeat;background-size: cover;
        margin: 0 0;
        padding: 0 0;
        height:100%;
        width:100%;
    }
</style>
