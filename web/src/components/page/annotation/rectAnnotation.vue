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
                            <canvas id="canvas" :style="{backgroundImage:imgUrls,width:imgX+'px',height:imgY+'px'}" onload="loadthis">
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
                    <div style="margin-top: 10px">
                        <span style="color: #242f42;margin-left: 12px">大家将本图标注为：</span>
                    </div>
                    <div style="width: 100%;height: 300px">
                        <el-tag
                            :key="tag"
                            v-for="tag in Tags"
                            :disable-transitions="false"
                            style="margin-top: 10px;margin-left: 12px">
                            <span style="color: royalblue">{{tag.split(/\s+/)[0]}}</span>&nbsp;&nbsp;<small style="color: #8c939d">{{tag.split(/\s+/)[1]}}</small>
                        </el-tag>
                    </div>
                    <div>
                        <el-button-group style="margin-left: 60px;margin-top: 30px">
                            <el-tooltip content="重新标注" placement="top">
                                <el-button type="primary" icon="el-icon-refresh" style="width: 80px" @click="reAnnotation"></el-button>
                            </el-tooltip>
                            <el-tooltip content="保存" placement="top">
                                <el-button type="primary" icon="el-icon-upload" style="width: 80px" @click="save"></el-button>
                            </el-tooltip>
                            <el-tooltip content="删除" placement="top">
                                <el-button type="primary" icon="el-icon-delete" style="width: 80px" @click="remove"></el-button>
                            </el-tooltip>
                            <el-tooltip content="离开" placement="top">
                                <el-button type="primary" icon="el-icon-d-arrow-right" style="width: 80px" @click="leave"></el-button>
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
    let draw ;
    let canDraw = true;
    let tag;
    let thisTag;
    function Coordinate(x,y) {
        this.x=x;
        this.y=y;
    }
    function Annotation(sentence, words, coordinates) {
        this.sentence=sentence;
        this.words=words;
        this.coordinates=coordinates;
    }
    let coordinates = [];
    let words = [];
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
            draw = new Draw();
            draw.init();
            axios.get('http://localhost:8080/taskOrder/orderInfo',{
                params:{
                    taskOrderId:this.$route.query.taskOrderId,
                    userId:localStorage.getItem("userId")
                }
            }).then((response) => {
                taskOrder=response.data.data;
                thisPage = taskOrder.lastPic;
                annotated = taskOrder.finishedPics;
                this.currentPage = thisPage;
                axios.post('http://localhost:8080/task/taskInfo',{
                        taskId:taskOrder.taskId
                }).then((response) => {
                    task = response.data.data;
                    this.totalNum = task.imgUrlList.length;
                    this.process = annotated / this.totalNum*100;
                    img.addEventListener('load',() =>{
                        this.imgX =img.width;
                        this.imgY = img.height;
                    });
                    img.src = task.imgUrlList[thisPage - 1];
                    this.imgUrls = "url('"+img.src+"')";
                });
                axios.get('http://localhost:8080/annotation/tags',{
                    params:{
                        taskId:taskOrder.taskId
                    }
                }).then((response)=>{
                    tag  = response.data.data;
                    thisTag = tag[thisPage];
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
                    },
                }).then((response) => {
                    annotationInfo = response.data.data;
                        annotationMap = annotationInfo.annotationMap;
                        annotations = annotationMap[thisPage];
                        thisAnnotation = annotations[0];
                        if(thisAnnotation == null){
                            isNew = true;
                        }else {
                            this.textarea = thisAnnotation.sentence;
                            coordinates = thisAnnotation.coordinates;
                            this.showAnnotation();
                        }
                    this.fullscreenLoading = false;
                })
            }).catch(()=>    {
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
                imgUrls:'',
                fullscreenLoading:true,
                process:0,
                myName:'',
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
                draw.refresh();
                img.addEventListener('load',() =>{
                    this.imgX =img.width;
                    this.imgY = img.height;
                });
                img.src = task.imgUrlList[val - 1];
                this.imgUrls = "url('"+img.src+"')";
                thisPage = val;
                annotations = annotationMap[thisPage];
                thisAnnotation = annotations[0];
                if(thisAnnotation == null){
                    coordinates=new Coordinate();
                    this.textarea = '';
                    isNew = true;
                }else {
                    this.textarea = thisAnnotation.sentence;
                    coordinates = thisAnnotation.coordinates;
                    this.showAnnotation();
                }
                thisTag = tag[thisPage];
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
                words = [];
                sentence = this.textarea;
                let temp =sentence.trim().split(/\s+|\.|,|\(|\)|;|:|"|\?|!/);
                for(let i=0;i<temp.length;i++){
                    if(temp[i] !==""){
                        words.push(temp[i])
                    }
                }
                thisAnnotation =new Annotation(sentence,words,coordinates);
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
                        this.autoSave();
                    }
                }).catch(()=>{
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
                    this.reAnnotation();
                    coordinates = [];
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
                            this.autoSave();
                        }
                    }).catch(()=>{
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
            reAnnotation(){
                sentence = '';
                this.textarea = '';
                draw.refresh();
            },
            leave(){
                taskOrder.lastPic = thisPage;
                taskOrder.finishedPics = annotated;
              axios.patch('http://localhost:8080/taskOrder/update',{
                      taskOrder:JSON.stringify(taskOrder)
              }).then((response)=>{
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
                      this.$router.go(-1);
                  }
              }) .catch(()=>{
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
              })
            },
            showAnnotation(){
                draw = new Draw();
                draw.drawFirst();
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
                        taskOrder.submited = true;
                        taskOrder.lastPic = thisPage;
                        taskOrder.finishedPics = annotated;
                        axios.post('http://localhost:8080/taskOrder/update', {
                            taskOrder: JSON.stringify(taskOrder)
                        }).then((response) => {
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
                                this.$router.go(-1);
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
                            message: '已取消提交'
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
                            taskOrderId:taskOrder.taskOrderId,
                            userId:localStorage.getItem("userId")
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
                            this.$router.go(-1);
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
            },
            autoSave(){
                taskOrder.lastPic = thisPage;
                taskOrder.finishedPics = annotated;
                axios.patch('http://localhost:8080/taskOrder/update',{
                    taskOrder:JSON.stringify(taskOrder),
                })
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
            }
        },
        name: "rectAnnotation"
    };

    window.addEventListener('load',()=>{
        draw = new Draw();
        draw.init();
    });

    function loadthis() {
        draw = new Draw();
        draw.init();
    }

    let Draw = ()=>{
        this.penal = document.getElementById('canvas');
        this.pen = this.penal.getContext('2d');
        this.isDraw = false;
    };

    Draw.prototype.init = ()=>{
      let originX = null;
      let originY = null;
      this.penal.addEventListener('mousedown',(event)=>{
          if(canDraw) {
              this.isDraw = true;
              let rect =this.penal.getBoundingClientRect();
              originX = (event.clientX - rect.left)*(this.penal.width/rect.width);
              originY = (event.clientY - rect.top)*(this.penal.height/rect.height);
              coordinates[0] = new Coordinate(originX,originY);
              this.pen.moveTo(originX, originY);
              this.pen.strokeStyle = 'black';
              this.pen.lineWidth = 1;
          }
      },false);
      this.penal.addEventListener('mousemove',(event)=>{
          if(this.isDraw){
              let rect =this.penal.getBoundingClientRect();
              let x = (event.clientX - rect.left)*(this.penal.width/rect.width);
              let y = (event.clientY - rect.top)*(this.penal.height/rect.height);
              let newOriginX = originX;
              let newOriginY = originY;
              coordinates[1]=new Coordinate(x,y);
              this.pen.clearRect(0,0,this.penal.width,this.penal.height);
              this.pen.lineWidth = 1;
              this.pen.beginPath();
              if(x < originX){
                  newOriginX = x;
              }
              if(y < originY){
                  newOriginY = y;
              }
              this.pen.rect(newOriginX,newOriginY,Math.abs(x-originX),Math.abs(y-originY));
              this.pen.stroke();
              this.pen.closePath();
          }
      },false);
        this.penal.addEventListener('mouseleave',()=>{
            if(this.isDraw) {
                this.isDraw = false;
                canDraw = false;
                this.pen.closePath();
            }
        },false);
        this.penal.addEventListener('mouseup',()=>{
            this.isDraw = false;
            canDraw = false;
        },false)
    };

    Draw.prototype.refresh = ()=>{
        canDraw = true;
        this.pen.clearRect(0,0,this.penal.width,this.penal.height);
    };

    Draw.prototype.drawFirst = ()=>{
        if(thisAnnotation.coordinates.length !== 0){
            canDraw = false;
            this.pen.beginPath();
            let x = thisAnnotation.coordinates[1].x;
            let y = thisAnnotation.coordinates[1].y;
            let originX =thisAnnotation.coordinates[0].x;
            let originY =thisAnnotation.coordinates[0].y;
            let newOriginX = originX;
            let newOriginY = originY;
            if(x < originX){
                newOriginX = x;
            }
            if(y < originY){
                newOriginY = y;
            }
            this.pen.rect(newOriginX,newOriginY,Math.abs(x-originX),Math.abs(y-originY));
            this.pen.stroke();
            this.pen.closePath();
        }
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
