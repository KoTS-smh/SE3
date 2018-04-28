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
        <el-dropdown>
          <i class="el-icon-arrow-down" style="margin-right: 10px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu>
    </el-header>
    <el-container style="height: 656px">
      <el-container style="height: 656px">
        <el-main style="height: 596px;display: flex;align-items:center;justify-content: center" v-loading.fullscreen.lock="fullscreenLoading">
            <div style="width: 100%;height: 100%" >
              <canvas id="canvas" :style="{backgroundImage:imgUrl,width:imgX+'px',height:imgY+'px'}">
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
        <el-row style="margin-top: 20px">
          <el-col :span="6" style="display: flex;align-items:center;justify-content: center">
            <span>任务进度:</span>
          </el-col>
          <el-col :span="18">
            <el-progress :text-inside="true" :stroke-width="18" :percentage="process" style="width: 280px"></el-progress>
          </el-col>
        </el-row>
        <div style="margin-top: 20px;display: flex;align-items:center;justify-content: center">
          <i class="el-icon-edit"></i>
          <span style="font-weight: bold">标注信息</span>
        </div>
        <div style="display: flex;align-items:center;justify-content: center">
        <el-input
          type="textarea"
          :rows="6"
          placeholder="请输入标注信息"
          resize="none"
          style="width: 380px;margin-top: 20px"
          v-model="textarea">
        </el-input>
        </div>
          <div style="width: 100%">
              <el-tag
                  :key="tag"
                  v-for="tag in Tags"
                  :disable-transitions="false"
                  style="margin-top: 10px;margin-left: 12px">
                  <span style="color: royalblue">{{tag.split(/\s+/)[0]}}</span><span style="color: #8c939d">{{tag.split(/\s+/)[1]}}</span>
              </el-tag>
          </div>
        <div>
          <el-button-group style="margin-left: 130px;margin-top: 20px">
            <el-tooltip content="保存" placement="bottom">
              <el-button type="primary" icon="el-icon-upload" style="width: 80px" @click="save"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="bottom">
              <el-button type="primary" icon="el-icon-delete" style="width: 80px" @click="remove"></el-button>
            </el-tooltip>
          </el-button-group>
        </div>
        <el-button type="primary" style="position: relative; left: 295px; top: 300px" @click="leave">离 开 <i class="el-icon-d-arrow-right"></i></el-button>
      </el-aside>
    </el-container>
  </el-container>
  </div>
</template>

<script>
    import axios from 'axios'
    let taskOrder;
    let task;
    let thisPage;
    let sentence = '';
    let annotated;
    let annotationInfo;
    let annotationMap;
    let annotations=[];
    let thisAnnotation;
    let img =new Image();
    let isNew = false;
    let tag;
    let thisTag;
    export default {
      created(){
          this.myName = localStorage.getItem("userId");
          if(this.$router.query == null){
            this.$router.push('/readme')
          }else if(this.$router.query.taskOrderId == null){
              this.$router.push('/readme')
          }
          axios.get('http://localhost:8080/taskOrder/orderInfo',{
              params:{
                  //taskOrderId:this.$router.query.taskOrderId
                  taskOrderId:111
              }
          }).then((response) => {
              taskOrder=response.data.data;
              thisPage = taskOrder.lastPic;
              annotated = taskOrder.degreeOfCompletion;
              this.currentPage = thisPage;
              axios.get('http://localhost:8080/task/taskInfo',{
                  params:{
                      taskId:taskOrder.taskId
                  }
              }).then((response) => {
                  task = response.data.data;
                  this.totalNum = task.imgUrlList.length;
                  this.process = annotated / this.totalNum*100;
                  img.addEventListener('load',() =>{
                     this.imgX =img.width;
                     this.imgY = img.height;
                  });
                  img.src = task.imgUrlList[thisPage - 1];
                  this.imgUrl = "url('"+img.src+"')";
              });
              axios.get('http://localhost:8080//annotation/tags',{
                  params:{
                      taskId:taskOrder.taskId
                  }
              }).then((response)=>{
                  tag  = response.data.data;
                  thisTag = tag[thisTag];
                  let temp = [];
                  for(let k in thisTag){
                      if(thisTag.hasOwnProperty(k)){
                          temp.push(k+' '+thisTag[k]);
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
              axios.get('http://localhost:8080/annotation/getAll',{
                  params:{
                      annotationId:taskOrder.annotationId
                  }
              }).then((response) => {
                  annotationInfo = response.data.data;
                  annotationMap = annotationInfo.annotationMap;
                  annotations = annotationMap[thisPage];
                  thisAnnotation = annotations[0];
                  if(thisAnnotation ==null){
                      isNew = true;
                  }else {
                      this.textarea = thisAnnotation.sentence;
                  }
                  this.fullscreenLoading = false;
              })
          }).catch(function () {
              this.$message({
                  showClose:true,
                  message:'网络异常!',
                  type:'error'
              })
          });
      },
      data() {
        return {
            Tags:[],
          activeIndex: '1',
          currentPage: 1,
          textarea:'',
          totalNum:null,
          imgX:null,
          imgY:null,
          imgUrl: '',
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
                this.fullscreenLoading = false;
            });
            img.src = task.imgUrlList[val - 1];
            this.imgUrl = "url('"+img.src+"')";
            thisPage = val;
            annotations = annotationMap[thisPage];
            thisAnnotation = annotations[0];
            if(thisAnnotation ==null){
                isNew = true;
            }else {
                this.textarea = thisAnnotation.sentence;
            }
            thisTag = tag[thisTag];
            let temp = [];
            for(let k in thisTag){
                if(thisTag.hasOwnProperty(k)){
                    temp.push(k+' '+thisTag[k]);
                }
            }
            this.Tags = temp;
            this.fullscreenLoading = false;
        },
          save(){
              sentence = this.textarea;
              words =sentence.trim().split(/\s+|\.|,|\(|\)|;|:|"|\?|!/);
              thisAnnotation =new Annotation(sentence,words,[]);
              annotations[0] = thisAnnotation;
              annotationMap[thisPage] = annotations;
              annotationInfo.annotationMap=annotationMap;
              axios.patch('http://localhost:8080/annotation/update',{
                  annotationInfo:JSON.stringify(annotationInfo)
              }).then((response)=>{
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
                  }
              }).catch((error)=>{
                  annotated--;
                  this.$message({
                      showClose:true,
                      message:'网络异常!',
                      type:'error'
                  })
              })
          },
          remove(){
              this.$confirm('此操作将永久删除该标注信息, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  sentence = '';
                  this.textarea = '';
                  thisAnnotation = {};
                  annotations = [];
                  annotationMap[thisPage] = annotations;
                  annotationInfo.annotationMap=annotationMap;
                  axios.patch('http://localhost:8080/annotation/update',{
                      annotationInfo:JSON.stringify(annotationInfo)
                  }).then((response)=>{
                      if(response.data.code!==0){
                          this.$message.error(response.data.msg)
                      }else {
                          if(!isNew){
                              isNew = true;
                              annotated--;
                              this.process = annotated / this.totalNum*100;
                          }
                          this.$message({
                              showClose: true,
                              message: '删除成功！',
                              type: 'success'
                          });
                      }
                  }).catch((error)=>{
                      this.$message({
                          showClose:true,
                          type: 'error',
                          message: '网络异常!'
                      });
                  });
              }).catch(() => {
                  this.$message({
                      showClose:true,
                      type: 'info',
                      message: '已取消删除'
                  });
              });
          },
          leave(){
              taskOrder.lastPic = thisPage;
              taskOrder.degreeOfCompletion = annotated;
              axios.patch('http://localhost:8080/taskOrder/update',{
                  taskOrder:JSON.stringify(taskOrder)
              }).then((response)=>{
                  if(response.data.code!==0){
                      this.$confirm('网络异常，信息未正常保存, 是否继续?', '提示', {
                          confirmButtonText: '确定',
                          cancelButtonText: '取消',
                          type: 'warning'
                      }).then(() => {
                          this.$router.push('/readme');
                      }).catch(() => {
                          this.$message({
                              type: 'info',
                              message: '已取消离开'
                          });
                      });
                  }else {
                      this.$router.push('/readme');
                  }
              }) .catch((error)=>{
                  this.$confirm('网络异常，信息未正常保存, 是否继续?', '提示', {
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'
                  }).then(() => {
                      this.$router.push('/readme');
                  }).catch(() => {
                      this.$message({
                          type: 'info',
                          message: '已取消离开'
                      });
                  });
              })
          },
          submitTask(){
              this.$confirm('此操作将提交该标注任务，之后无法更改, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  taskOrder.isSubmited = true;
                  taskOrder.lastPic = thisPage;
                  taskOrder.degreeOfCompletion = annotated;
                  axios.post('http://localhost:8080/taskOrder/update',{
                      taskOrder:JSON.stringify(taskOrder)
                  }).then((response)=>{
                      if(response.data.code!==0){
                          this.$message({
                              type: 'error',
                              message: '提交失败!'
                          });
                      }else{
                          this.$message({
                              type: 'success',
                              message: '提交成功!'
                          });
                          // todo leave
                      }
                  }).catch(()=>{
                      this.$message({
                          type: 'error',
                          message: '提交失败!'
                      });
                  })
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消提交'
                  });
              });
          },
          removeTask(){
              this.$confirm('此操作将移除该标注任务, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  axios.get('http://localhost:8080/taskOrder/delete',{
                      params:{
                          taskOrderId:taskOrder.taskOrderId
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
                          //todo leave
                      }
                  }).catch(()=>{
                      this.$message({
                          type: 'error',
                          message: '移除失败!'
                      });
                  })
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消提交'
                  });
              });
          }
      },
        name: "allAnnotation"
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
