<template >
    <div>
        <el-container style="height: 716px">
            <el-header style="text-align: right; font-size: 15px">
                <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="1">标注中心</el-menu-item>
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
                        <span style="margin-left: 16px">标注人名称：</span><span style="color: #3a8ee6">{{toRateName}}</span>
                    </el-row>
                    <el-row style="margin-top: 20px">
                        <span style="margin-left: 16px">标注人Id:</span><span style="color: #3a8ee6">{{toRateId}}</span>
                    </el-row>
                    <el-row style="margin-top: 20px;border-bottom:gainsboro 1px solid;height: 40px">
                        <el-col :span="6">
                            <span style="margin-left: 16px">评分：</span>
                        </el-col>
                        <el-col :span="18">
                            <el-rate
                                :disabled="canNotRate"
                                v-model="ratePoint"
                                :colors="['#99A9BF', '#F7BA2A', '#FF9900']">
                            </el-rate>
                        </el-col>
                    </el-row>
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
                            style="width: 90%;margin-top: 20px"
                            readonly = 'true'
                            v-model="textarea">
                        </el-input>
                    </div>
                    <div style="margin-top: 10px">
                        <span style="color: #242f42;margin-left: 12px">大家将本图标注为：</span>
                    </div>
                    <div style="width: 100%;height:200px">
                        <el-tag
                            :key="tag"
                            v-for="tag in Tags"
                            :disable-transitions="false"
                            style="margin-top: 10px;margin-left: 12px">
                            <span style="color: royalblue">{{tag.split(/\s+/)[0]}}</span>&nbsp;&nbsp;<small style="color: #8c939d">{{tag.split(/\s+/)[1]}}</small>
                        </el-tag>
                    </div>
                    <el-button-group style="position: relative; left: 180px">
                        <el-button type="primary" v-show="rateBtn" @click="finishRate">完成评分<i class="el-icon-circle-check-outline"></i></el-button>
                        <el-button type="primary" @click="leave">离 开 <i class="el-icon-d-arrow-right"></i></el-button>
                    </el-button-group>
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
    let annotated;
    let annotationInfo;
    let annotationMap;
    let annotations=[];
    let thisAnnotation;
    let img =new Image();
    let isNew = false;
    let draw;
    let canDraw = true;
    let coordinates = [];
    export default {
        created(){
            this.myName = localStorage.getItem("username");
            if(this.myName == null){
                this.$router.push("/homepage")
            }
            if(this.$route.query == null){
                this.$router.push("/homepage")
            }else if(this.$route.query.taskOrderId == null){
                this.$router.push("/homepage")
            }
            axios.get('http://localhost:8080/taskOrder/orderInfo',{
                params:{
                    taskOrderId:this.$route.query.taskOrderId,
                    userId:this.$route.query.userId
                }
            }).then((response) => {
                taskOrder=response.data.data;
                thisPage = taskOrder.lastPic;
                this.toRateId = taskOrder.acceptUserId;
                annotated = taskOrder.degreeOfCompletion;
                if(taskOrder.rate !=null){
                    this.ratePoint = taskOrder.rate;
                    this.canNotRate = true;
                }
                this.currentPage = thisPage;
                if(localStorage.getItem("userId") ==taskOrder.acceptUserId){
                    this.rateBtn = false;
                }
                axios.get('http://localhost:8080/user/getUser',{
                    params:{
                        userId:taskOrder.acceptUserId
                    }
                }).then((response)=>{
                    this.toRateName = response.data.data.username;
                }).catch(()=>{
                    this.$message({
                        showClose:true,
                        message:'网络异常!',
                        type:'error'
                    })
                });
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
                    this.imgUrl = "url('"+img.src+"')";
                });
                axios.get('http://localhost:8080//annotation/tags',{
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
                        coordinates = thisAnnotation.coordinates;
                        this.showAnnotation();
                    }
                    this.fullscreenLoading = false;
                })
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
                ratePoint:null,
                toRateName:'',
                toRateId:'',
                myName:'',
                canNotRate:false,
                rateBtn:true
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
                    this.fullscreenLoading = false;
                });
                img.src = task.imgUrlList[val - 1];
                this.imgUrl = "url('"+img.src+"')";
                thisPage = val;
                annotations = annotationMap[thisPage];
                thisAnnotation = annotations[0];
                if(thisAnnotation ==null){
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
            leave(){
                taskOrder.lastPic = thisPage;
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
            finishRate(){
                if(this.ratePoint == null){
                    this.$message({
                        type: 'error',
                        message: '还未评分！'
                    });
                }else {
                    this.$confirm('评分一旦提交，就无法修改，是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        taskOrder.rate = this.ratePoint;
                        axios.patch('http://localhost:8080/taskOrder/update', {
                            taskOrder: JSON.stringify(taskOrder)
                        }).then((response) => {
                            if (response.data.code !== 0) {
                                this.$confirm('网络异常，评分未正常保存, 是否继续?', '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }).then(() => {
                                    this.$router.go(-1);
                                }).catch(() => {
                                    this.$message({
                                        type: 'info',
                                        message: '已取消'
                                    });
                                });
                            }
                        }).catch(() => {
                            this.$confirm('网络异常，评分未正常保存, 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                this.$router.go(-1)
                            }).catch(() => {
                                this.$message({
                                    type: 'info',
                                    message: '已取消'
                                });
                            });
                        })
                    })
                }
            },
            showAnnotation(){
                draw = new Draw();
                draw.drawFirst();
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

        name: "regionAnnotationRate"
    }
    window.addEventListener('load',()=>{
        draw = new Draw();
    });

    let Draw = ()=>{
        this.penal = document.getElementById('canvas');
        this.pen = this.penal.getContext('2d');
        this.isDraw = false;
        this.pen.strokeStyle = 'black';
        this.pen.lineWidth = 1;
    };

    Draw.prototype.refresh = ()=>{
        canDraw = true;
        this.pen.clearRect(0,0,this.penal.width,this.penal.height);
    };

    Draw.prototype.drawFirst = ()=>{
        if(thisAnnotation.coordinates !==null){
            canDraw = false;
            this.pen.beginPath();
            this.pen.moveTo(coordinates[0].x,coordinates[0].y);
            for(let i = 1;i<coordinates.length;i++){
                this.pen.lineTo(coordinates[i].x,coordinates[i].y);
                this.pen.stroke();
            }
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
