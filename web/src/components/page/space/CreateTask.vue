<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="12">
                <div class="grid-content bg-purple">
                    <el-form ref="form" :model="form" label-width="100px">
                            <el-form-item label="任务名称">
                                <el-input v-model="form.taskname"></el-input>
                            </el-form-item>
                            <el-form-item label="标注类型">
                                <el-select v-model="annotationType" placeholder="请选择">
                                    <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="开始时间">
                                <el-col :span="14">
                                <el-date-picker type="datetime" placeholder="选择日期和时间" value-format="yyyy-MM-dd HH:mm:ss" v-model="form.beginDate" @change="beginDateChange" style="width: 100%;"></el-date-picker>
                                </el-col>
                            </el-form-item>

                            <el-form-item label="截止时间">
                                <el-col :span="14">
                                <el-date-picker type="datetime" placeholder="选择日期和时间" value-format="yyyy-MM-dd HH:mm:ss" v-model="form.endDate" @change="endDateChange" style="width: 100%;"></el-date-picker>
                                </el-col>
                            </el-form-item>

                            <el-form-item label="奖励积分">
                                <div class="block">
                                    <el-slider
                                        v-model="form.totalPoints"
                                        show-input>
                                    </el-slider>
                                </div>
                            </el-form-item>

                            <el-form-item label="最大标注人数">
                                <el-input v-model="form.maxParticipator" placeholder="请输入最大标注人数"></el-input>
                            </el-form-item>

                            <el-form-item label="任务级别">
                                <el-input-number v-model="form.taskLevel" @change="handleChange" :min="1" :max="10" label="描述文字"></el-input-number>
                                <p id="description">任务级别越高，接取任务需要的用户级别也越高，0级任务表示所有用户都可以接取</p>
                            </el-form-item>

                            <el-form-item label="上传图片">
                                <div id="app">
                                    <upload :action="action" :token="token" @on-upload="uploadFile" :multiple="multiple" :accept="accept" @on-error="uploadErr" @on-progress="progress">
                                        <template slot="form">
                                        </template>
                                        <template slot="picker">
                                            <span class="btn">点击按钮</span>
                                        </template>
                                    </upload>
                                    <div class="block" id="ajaxErr" v-show="uploadMsg.length">
                                        <h4>uploadMsg: </h4>
                                        <p v-for="(item, index) in uploadMsg" :key="index">{{item}}</p>
                                    </div>
                                </div>
                            </el-form-item>

                            <el-form-item label="备注">
                                <el-input type="textarea" v-model="form.taskInfo"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="onSubmit">立即创建</el-button>
                                <el-button>取消</el-button>
                            </el-form-item>
                        </el-form>
                </div>
            </el-col>

            <el-col :span="12">
                <div class="grid-content bg-purple">
                    <el-carousel height="400px">
                    <el-carousel-item v-for="item in form.imgList" :key="item" >
                        <img :src="item" alt="" >
                    </el-carousel-item>
                    </el-carousel>
                </div>
            </el-col>

        </el-row>

        <el-dialog title="请输入标注分类" :visible.sync="dialogTableVisible">
            <el-form id="parentForm">
                <el-form-item label="分类1">
                    <el-input  auto-complete="off" v-model="form.classification[0]"></el-input>

                </el-form-item>

                <el-form-item label="分类2">
                    <el-input  auto-complete="off" v-model="form.classification[1]"></el-input>

                </el-form-item>

                <el-form-item label="分类3">
                    <el-input  auto-complete="off" v-model="form.classification[2]"></el-input>

                </el-form-item>

                <el-form-item label="分类">
                    <el-input  auto-complete="off" v-model="form.classification[3]"></el-input>

                </el-form-item>
            </el-form>

            <div style="text-align:right;">
                <el-button type="primary" @click="dialogTableVisible = false">确认</el-button>
            </div>
        </el-dialog>

    </div>
</template>


<script>
import upload from '../../common/upload.vue'
import axios from 'axios'
export default {
    name: 'app',
    components: {
        upload
    },
  data() {
      return {
        imgList:[],
        taskData: [],
        apiUrl: 'localhost:8080/#/api/tasks',
        item: {},
        options: [
            {
                value: 'option1',
                label: '标框标注'
            },
            {
                value: 'option2',
                label: '分类标注'
            },
            {
                value: 'option3',
                label: '区域标注'
            },
            {
                value: 'option4',
                label: '整体标注'
            }
        ],


        dialogTableVisible: false,
        form: {
            postUserId: localStorage.getItem("userId"),
            acceptUserIds:[],
          taskname: '',
          beginDate: '',
          endDate: '',
          taskInfo: '',
          totalPoints: 0,
          maxParticipator: '',
          taskLevel: 0,
          imgUrlList: [],
          classification:['','','','']
        },

        action: 'http://upload.qiniu.com/', // 替换自己的上传链接
        accept: 'image/png, image/jpeg, image/gif, image/jpg',
        multiple: true,
        token: 'j0dwMMGFcKPhncC7vb_PWXshbpiSMEWB69NiKhn4:MekzrWCymy2QrrqlXii3_3lp_qM=:eyJzY29wZSI6Im1yZ3MtYnVja2V0IiwiY2FsbGJhY2tCb2R5Ijoia2V5PSQoa2V5KSZoYXNoPSQoZXRhZykmd2lkdGg9JChpbWFnZUluZm8ud2lkdGgpJmhlaWdodD0kKGltYWdlSW5mby5oZWlnaHQpJnVzZXI9JChlbmRVc2VyKSZzaXplPSQoZnNpemUpJm1pbWU9JChtaW1lVHlwZSkiLCJkZWFkbGluZSI6MTUyNDk4Njc3OH0=',
        hashes: [],
        keys: [],
        uploadMsg: [],
        annotationType: ''
      }
    },
    methods: {
        beginDateChange(val){
            console.log(val);
            this.form.beginDate = val
        },
        endDateChange(val){
            this.form.endDate = val;
        },
        uploadFile (res) {
            this.hashes.push(`http://p6r9un2qj.bkt.clouddn.com/${res.hash}`);
            this.keys.push(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`);
            this.form.imgUrlList.push(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`)
        },
        uploadErr (res) {
            this.uploadMsg.push(JSON.stringify(res))
        },
        progress (e) {
            this.uploadMsg.push(`e.loaded: ${e.loaded}, e.total: ${e.total}, percent: ${(e.loaded / e.total) * 100}`)
        },
        handleChange() {

        },
        onSubmit() {
            const self = this;
            this.form.annotationType = this.annotationType;
            axios.post('http://localhost:8080/task/create', this.form).then(function (response) {
                console.log(response);
                self.success();
            }).catch(function(err) {
                console.log(err);
            });

        },
        addLabel() {
            var elFormItem = document.createElement('el-form-item');
            var parent = document.getElementById('parentForm');
            parent.appendChild(elFormItem)
        },
        changeClassification() {

        },
        success() {
            this.$message('任务创建成功！');
        },
        placeToken() {
            axios.post("http://localhost:8080/user/getToken").then(response =>{
                console.log(response.data)
                this.token = response.data;
            }).catch(err => {
                console.log(err)
            })
        }

    },
    watch: {
        annotationType: function(val) {
            if(val === 'option2'){
                this.dialogTableVisible = true
            }
        }
        // form: {
        //     handler: function(val) {
        //         if (val.annotationType === 'option2' && !this.annotationTypeHasBeenSet){

        //                 this.dialogTableVisible = true
        //                 this.annotationTypeHasBeenSet = true
        //         }else{
        //             this.annotationTypeHasBeenSet = false
        //             this.form.classification1 = ''
        //             this.form.classification2 = ''
        //             this.form.classification3 = ''
        //             this.form.classification4 = ''
        //         }
        //     },
        //     deep: true
        // }
    },
    mounted() {
        this.placeToken();
    }
}

function download(content, fileName, contentType) {
    var a = document.createElement("a");
    var file = new Blob([content], {type: contentType});
    a.href = URL.createObjectURL(file);
    a.download = fileName;
    a.click();
}
</script>

<style>
  /*.el-row {*/
    /*margin-bottom: 20px;*/
    /*&:last-child {*/
      /*margin-bottom: 0;*/
    /*}*/
  /*}*/
  .el-col {
    border-radius: 4px;
  }

  .el-form-item {
      align-self: left;
  }

  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
  }

    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        /*   */

        /* margin-top: 60px; */
    }

    #description {
        font-family: "PingFang SC";
        font-size: 15px;
        font-style: italic;
    }

    .btn {
        padding: 5px 5px;
        border: 1px solid #ccc;
        border-radius: 3px;
    }
</style>
