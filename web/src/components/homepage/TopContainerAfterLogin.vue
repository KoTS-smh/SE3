<template>
  <div class="top_container">
      <el-menu default-active="/homepage" class="el-menu-demo" mode="horizontal" @select="handleSelect" :router="canRouter">
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
                  <!-- <el-menu-item index="/personalSpace" style="text-align: center; position:absolute; top:0px; right:150px;color: dodgerblue">
                    <span>{{myName}}</span>
                  </el-menu-item> -->
                  <el-dropdown style="text-align: center; position:absolute; top:20px; right:250px;color: dodgerblue" @command="handlePersonCommand">
                      <span class="el-dropdown-link">
                          {{myName}}
                      </span>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="personalSpace">个人中心</el-dropdown-item>
                        <el-dropdown-item command="logout">退出登陆</el-dropdown-item>
                      </el-dropdown-menu>
                  </el-dropdown>

                  <el-dropdown style="text-align: center;position:absolute; top:20px;right:200px;color: dodgerblue" @command="gotoMessageCenter">
                      <el-badge :value="this.messageNum">
                          <icon name="bell"></icon>
                      </el-badge>
                      

                      <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item command="go">消息中心</el-dropdown-item>
                      </el-dropdown-menu>
                  </el-dropdown>
              </el-col>

              <el-col :span="2">
                  <el-dropdown style="text-align: center;margin-top: 7.5px;position:absolute;right:30px" @command="handleCommand">
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
    import axios from 'axios'
    import Icon from 'vue-awesome/components/Icon'
    import 'vue-awesome/icons'
    export default {
        components: {
            Icon
        },
        data() {
            return {
                isLogin: true,
                myName: '',
                canRouter:true,
                messageNum:10
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
            },
            placeUsername() {
                var username = localStorage.getItem("username");
                this.myName = username;
                axios.post('http://localhost:8080/user/getMessageNum', {
                    "userId": localStorage.getItem("userId")
                }).then(response => {
                    console.log(response)
                    this.messageNum = response.data.data
                })
            },
            handlePersonCommand(command) {
                if(command == 'personalSpace') {
                    this.$router.push('/readme');
                }else if(command == 'logout') {
                    axios.get('http://localhost:8080/user/logout', {
                        params:{
                            // username: localStorage.getItem("username")
                            userId: localStorage.getItem("userId")
                        }
                    }).then(response => {
                        localStorage.removeItem("userId");
                        localStorage.removeItem("username");

                        this.$router.push('/homepage');
                        location.reload();
                    })
                }
            },
            gotoMessageCenter(command) {
                this.$router.push('messageCenter');
            }
        },
        mounted() {
            this.placeUsername()
        }
    }
</script>

<style lang="stylus">
  .top_container
    color #222
    position relative
</style>
