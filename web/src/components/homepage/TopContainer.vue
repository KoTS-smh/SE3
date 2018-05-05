<template>
  <div class="top_container">
      <el-menu default-active="/homepage" class="el-menu-demo" mode="horizontal" @select="handleSelect" router="true">
          <el-row>
              <el-col :span="2">
                  <el-menu-item index="/guide" style="text-align: center">首页</el-menu-item>
              </el-col>
              <el-col :span="2">
                  <el-menu-item index="/homepage" style="text-align: center">主站</el-menu-item>
              </el-col>
              <el-col :span="2">
                  <el-menu-item index="/search" style="text-align: center">搜索</el-menu-item>
              </el-col>
              <el-col :span="2" :offset="11">
                  <el-menu-item index="/login" style="text-align: center">登陆</el-menu-item>
              </el-col>
              <el-col :span="2">
                  <el-menu-item index="/register" style="text-align: center">注册</el-menu-item>
              </el-col>
              <el-col :span="2">
                  <el-dropdown style="text-align: center;margin-top: 7.5px" @command="handleCommand">
                      <el-button type="primary">
                          发布任务<i class="el-icon-arrow-down el-icon--right"></i>
                      </el-button>
                      <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item command="a">发布界面</el-dropdown-item>
                          <el-dropdown-item command="b">任务管理</el-dropdown-item>
                      </el-dropdown-menu>
                  </el-dropdown>
              </el-col>
          </el-row>

          <div class="line"></div>
      </el-menu>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                isLogin: false
            }
        },
        methods: {
            handleSelect() {

            },
            handleCommand(command) {
                if(this.isLogin) {
                    if(command == 'a'){
                    this.$router.push('createTask');
                    }else if(command == 'b'){
                        this.$router.push('publishedTasks');
                    }
                }else{
                    this.$message('请先登录');
                }
            }
        }
    }
</script>

<style lang="stylus">
  .top_container
    color #222
    position relative
</style>
