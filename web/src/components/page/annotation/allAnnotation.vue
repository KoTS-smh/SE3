<template >
  <div>
  <el-container style="height: 716px">
    <el-header style="text-align: right; font-size: 15px">
      <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">标注中心</el-menu-item>
          <el-submenu index="2">
              <template slot="title">我的任务</template>
              <el-menu-item index="2-1" @click="submitTask">提交</el-menu-item>
              <el-menu-item index="2-2" @click="removeTask">移除</el-menu-item>
          </el-submenu>
        <span style="color: dodgerblue">{{myName}}</span>
          <el-dropdown @command="handleCommand">
              <i class="el-icon-arrow-down" style="margin-right: 10px"></i>
              <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="logout">退出</el-dropdown-item>
              </el-dropdown-menu>
          </el-dropdown>
      </el-menu>
    </el-header>
    <el-container style="height: 656px">
      <el-container style="height: 656px">
        <el-main style="height: 596px;display: flex;align-items:center;justify-content: center" v-loading.fullscreen.lock="fullscreenLoading">
            <div style="width: 100%;height: 100%" >
              <canvas id="canvas" :style="{backgroundImage:imgUrls,width:imgX+'px',height:imgY+'px'}">
              </canvas>
            </div>
        </el-main>
        <el-footer style="height: 60px">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page.sync="currentPage"
              :page-size="1"
              background
              layout="prev, pager, next, jumper"
              :total="totalNum"
              style="margin-top: 15px; margin-right: 10px">
            </el-pagination>
        </el-footer>
      </el-container>
      <el-aside width="400px" style="border-left: gainsboro 1px solid">
          <el-steps id="steps" :active="1" finish-status="success" style="margin-top: 10px;margin-left: 10px;width: 90%">
              <el-step title="开始" :description="beginDate"></el-step>
              <el-step v-if="test1" title="检测点1" :description="test1Date"></el-step>
              <el-step v-if="test2" title="检测点2" :description="test2Date"></el-step>
              <el-step v-if="endTask" title="提交" :description="endDate"></el-step>
          </el-steps>

        <el-row style="margin-top: 20px">
          <el-col :span="12" style="border-right: gainsboro solid 1px">
              <el-form size="mini" style="margin-left: 5%">
                  <el-form-item label="当前进度：">
                      {{process}}%
                  </el-form-item>
                  <el-form-item label="评估质量: ">
                      {{currentQuality}}
                  </el-form-item>
              </el-form>
          </el-col>
          <el-col :span="12">
              <el-form size="mini" style="margin-left: 5%">
                  <el-form-item label="目标进度：">
                      {{targetProcess}}%
                  </el-form-item>
                  <el-form-item label="目标质量: ">
                      60
                  </el-form-item>
              </el-form>
          </el-col>
        </el-row>
        <div style="margin-top: 10px;display: flex;align-items:center;justify-content: center">
          <i class="el-icon-edit"></i>
          <span style="font-weight: bold">标注信息</span>
        </div>
        <div style="display: flex;align-items:center;justify-content: center">
        <el-input
          type="textarea"
          :rows="4"
          placeholder="请输入标注信息"
          resize="none"
          :readonly = 'canNotAnno'
          style="width: 95%;margin-top: 10px"
          v-model="textarea">
        </el-input>
        </div>
          <div style="margin-top: 10px">
              <span style="color: #242f42;margin-left: 12px">大家将本图标注为：</span>
          </div>
          <div style="width: 100%;height: 250px">
              <el-tag
                  :key="tag"
                  v-for="tag in Tags"
                  :disable-transitions="false"
                  style="margin-top: 10px;margin-left: 12px">
                  <span style="color: royalblue">{{tag.split(/\s+/)[0]}}</span>&nbsp;&nbsp;<small style="color: #8c939d">{{tag.split(/\s+/)[1]}}</small>
              </el-tag>
          </div>
        <div>
          <el-button-group style="margin-left: 10%;">
            <el-tooltip content="保存" placement="top">
              <el-button type="primary" icon="el-icon-upload" style="width: 80px" @click="save" :disabled="canNotAnno"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button type="primary" icon="el-icon-delete" style="width: 80px" @click="remove" :disabled="canNotAnno"></el-button>
            </el-tooltip>
              <el-tooltip content="下一张" placement="top">
                  <el-button type="primary" icon="el-icon-d-arrow-right" style="width: 80px" @click="nextPic"></el-button>
              </el-tooltip>
              <el-tooltip content="离开" placement="top">
                  <el-button type="primary" icon="el-icon-circle-close" style="width: 80px" @click="leave"></el-button>
              </el-tooltip>
          </el-button-group>
        </div>
      </el-aside>
    </el-container>
  </el-container>
  </div>
</template>

<script>
    import axios from 'axios'
    let taskOrder;
    let taskOrderId;
    let taskId;
    let acceptUserId;
    let imgUrlList;
    let annotated;
    let annotationModel;
    let img =new Image();
    let isNew = false;
    function Annotation(taskOrderId,pictureNum,sentence, words, coordinates) {
        this.sentence=sentence;
        this.words=words;
        this.coordinates=coordinates;
        this.pictureNum = pictureNum;
        this.taskOrderId = taskOrderId;
    }
    export default {
      mounted(){
          this.myName = localStorage.getItem("username");
          if(this.myName == null){
              this.$router.push("/homepage")
          }
          if(this.$route.query == null){
              this.$router.push("/homepage")
          }else if(this.$route.query.taskOrderId == null){
              this.$router.push("/homepage")
          }
          taskOrderId = this.$route.query.taskOrderId;
          this.test2 = true;
          this.test1 = true;
          this.endTask = true;
          axios.get('http://localhost:8080/taskOrder/orderInfo',{
              params:{
                  taskOrderId:taskOrderId
              }
          }).then((response) => {
              taskOrder=response.data.data;
              this.currentPage = taskOrder.lastPic;
              this.currentQuality = taskOrder.rate;
              if(taskOrder.finishedPics===0){
                  this.totalNum=1;
              }else {
                  this.totalNum = taskOrder.finishedPics;
              }
              annotated = taskOrder.finishedPics;
              taskId = taskOrder.taskId;
              acceptUserId = taskOrder.acceptUserId;
              axios.post('http://localhost:8080/task/taskInfo',{
                      taskId:taskId
              }).then((response) => {
                  if(response.data.data.postUserId == localStorage.getItem("userId")||response.data.data.state == 'finish'){
                      this.canNotAnno = true;
                  }
                  imgUrlList = response.data.data.imgUrlList;
                  this.process = annotated / imgUrlList.length*100;
                  this.beginDate = new Date(response.data.data.beginDate).Format("yyyy-MM-dd");
                  this.endDate = new Date(response.data.data.endDate).Format("yyyy-MM-dd");
                  img.addEventListener('load',() =>{
                     this.imgX =img.width;
                     this.imgY = img.height;
                  });
                  img.src = imgUrlList[this.currentPage - 1];
                  this.imgUrls = "url('"+img.src+"')";
              });
              //获得标注信息的tag
              this.getAnnoTag(taskId,this.currentPage);
              //获得标注信息
              this.getAnnotation(taskOrderId,this.currentPage);
              this.fullscreenLoading = false;
          }).catch( ()=> {
              this.$message({
                  showClose:true,
                  message:'网络异常!',
                  type:'error'
              })
          });
      },
      data() {
        return {
            beginDate:'',
            endDate:"",
            test1Date:"",
            test2Date:'',
            test1:'',
            test2:'',
            endTask:'',
            currentQuality:'',
            targetProcess:"",
            canNotAnno:'',
          Tags:[],
          activeIndex: '1',
          currentPage: 1,
          textarea:'',
          totalNum:null,
          imgX:null,
          imgY:null,
          imgUrls: '',
          fullscreenLoading:true,
          process:0,
          myName:null,
        };
      },
      methods: {
        handleSelect(key, keyPath) {
          console.log(key, keyPath);
        },
        handleSizeChange(val) {
          console.log(`每页 ${val} 条`);
        },
        handleCurrentChange(val) {
          this.fullscreenLoading = true;
            img.addEventListener('load',() =>{
                this.imgX =img.width;
                this.imgY = img.height;
            });
            img.src = imgUrlList[val - 1];
            this.imgUrls = "url('"+img.src+"')";
            this.currentPage = val;
            this.getAnnotation(taskOrderId,this.currentPage);
            this.getAnnoTag(taskId,this.currentPage);
            this.fullscreenLoading = false;
        },
          save(){
              let annotation =new Annotation(taskOrderId,this.currentPage,this.textarea,"","");
              axios.patch('http://localhost:8080/annotation/update',
                  annotation
              ).then((response)=>{
                  if(response.data.code!==0){
                      this.$message.error(response.data.msg)
                  }else {
                      if(isNew){
                          isNew=false;
                          annotated++;
                          this.process = annotated / this.totalNum*100;
                      }
                      this.$message({
                          showClose: true,
                          message: '保存成功！',
                          type: 'success'
                      });
                      this.autoSave()
                  }
              }).catch(()=>{
                  this.$message({
                      showClose:true,
                      message:'网络异常!',
                      type:'error'
                  })
              });
          },
          remove(){
              this.$confirm('此操作将移除该标注信息, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  if(isNew){
                      this.textarea = '';
                  }else {
                      axios.get('http://localhost:8080/annotation/delete', {
                          params: {
                              taskOrderId: taskOrderId,
                              pictureNum: this.currentPage
                          }
                      }).then((response) => {
                          if (response.data.code !== 0) {
                              this.$message.error(response.data.msg)
                          } else {
                              this.textarea = '';
                              if (!isNew) {
                                  isNew = true;
                                  annotated--;
                                  this.process = annotated / this.totalNum * 100;
                              }
                              this.$message({
                                  showClose: true,
                                  message: '删除成功！',
                                  type: 'success'
                              });
                              this.autoSave()
                          }
                      }).catch(() => {
                          this.$message({
                              showClose: true,
                              type: 'error',
                              message: '网络异常!'
                          });
                      });
                  }
              }).catch(() => {
                  this.$message({
                      showClose:true,
                      type: 'info',
                      message: '已取消删除'
                  });
              });
          },
          leave(){
              taskOrder.lastPic = this.currentPage;
              taskOrder.finishedPics = annotated;
              axios.patch('http://localhost:8080/taskOrder/update',
                  taskOrder
                  ).then((response)=>{
                  if(response.data.code!==0){
                      this.$confirm('网络异常，信息未正常保存, 是否继续?', '提示', {
                          confirmButtonText: '确定',
                          cancelButtonText: '取消',
                          type: 'warning'
                      }).then(() => {
                          this.$router.go(-1);
                      }).catch(() => {
                          this.$message({
                              type: 'info',
                              message: '已取消离开'
                          });
                      });
                  }else {
                      this.$router.go(-1)
                  }
              }) .catch(()=>{
                  this.$confirm('网络异常，信息未正常保存, 是否继续?', '提示', {
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'
                  }).then(() => {
                      this.$router.go(-1)
                  }).catch(() => {
                      this.$message({
                          type: 'info',
                          message: '已取消离开'
                      });
                  });
              })
          },
          submitTask(){
            if(this.process !== 100){
                this.$message({
                    type: 'error',
                    message: '未完成，无法提交！'
                });
            }else {
                this.$confirm('此操作将提交该标注任务，之后无法更改, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    taskOrder.submited = 'submitted';
                    taskOrder.lastPic = this.currentPage;
                    taskOrder.finishedPics = annotated;
                    axios.post('http://localhost:8080/taskOrder/update',
                        taskOrder
                    ).then((response) => {
                        if (response.data.code !== 0) {
                            this.$message({
                                type: 'error',
                                message: '提交失败!'
                            });
                        } else {
                            this.$message({
                                type: 'success',
                                message: '提交成功!'
                            });
                            this.$router.go(-1)
                        }
                    }).catch(() => {
                        this.$message({
                            type: 'error',
                            message: '提交失败!'
                        });
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消提交！'
                    });
                });
            }
          },
          removeTask(){
              this.$confirm('此操作将移除该标注任务, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  axios.get('http://localhost:8080/taskOrder/delete',{
                      params:{
                          taskOrderId:taskOrderId,
                      }
                  }).then((response)=>{
                      if(response.data.code!==0){
                          this.$message({
                              type: 'error',
                              message: '移除失败!'
                          });
                      }else {
                          this.$message({
                              type: 'success',
                              message: '移除成功!'
                          });
                          this.$router.go(-1)
                      }
                  }).catch(()=>{
                      this.$message({
                          type: 'error',
                          message: '网络异常!'
                      });
                  })
              }).catch(() => {
                  this.$message({
                      type: 'error',
                      message: '已取消！'
                  });
              });
          },
          autoSave(){
              taskOrder.lastPic = this.currentPage;
              taskOrder.finishedPics = annotated;
              axios.patch('http://localhost:8080/taskOrder/update',
                  taskOrder
              )
          },
          handleCommand(command) {
              if (command === 'logout') {
                  axios.get("http://localhost:8080/user/logout", {
                      params: {
                          username: localStorage.getItem("username")
                      }
                  });
                  localStorage.removeItem("username");
                  localStorage.removeItem("userId");
                  this.$router.push("/homepage")
              }
          },
          getAnnoTag(taskId,pictureNum){
              axios.get('http://localhost:8080/annotation/tags',{
                  params:{
                      taskId:taskId,
                      pictureNum:pictureNum
                  }
              }).then((response)=>{
                  let tag  = response.data.data;
                  let temp = [];
                  for(let k in tag){
                      if(tag.hasOwnProperty(k)){
                          temp.push(k+' '+tag[k]);
                      }
                  }
                  this.Tags = temp;
              }).catch(()=>{
                  this.$message({
                      showClose:true,
                      message:'tag加载异常!',
                      type:'error'
                  })
              });
          },
          getAnnotation(taskOrderId,pictureNum){
              axios.get('http://localhost:8080/annotation/get',{
                  params:{
                      taskOrderId:taskOrderId,
                      pictureNum:pictureNum
                  }
              }).then((response) => {
                  annotationModel = response.data.data;
                  if(response.data.code !== 0){
                      isNew = true;
                  }else {
                      this.textarea = annotationModel.sentence;
                  }
              })
          },
          nextPic() {
              if (isNew == true) {
                    this.$message.info("本图还未标注！");
              } else {
                  if (this.currentPage === imgUrlList.length) {
                      this.$message.info("已经是最后一张了！");
                  } else {
                      if(this.currentPage === this.totalNum){
                          this.totalNum++;
                          this.handleCurrentChange(this.totalNum);
                      }else{
                          this.handleCurrentChange(this.currentPage+1);
                      }
                  }
              }
          }
      },
        name: "allAnnotation"
    }

    Date.prototype.Format = function(fmt)
    {
        let o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(let k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length===1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }

</script>

<style scoped>
  .el-header {
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-footer{
    color: #333;
    text-align: center;
  }
  .el-aside {
    color: #333;
  }
  .el-main {
    color: #333;
    text-align: center;
  }
</style>
