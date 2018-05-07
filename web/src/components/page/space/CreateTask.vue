<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="9" style="border-right: gainsboro solid 1px">
                <div class="grid-content bg-purple">
                    <el-form ref="form" :model="form" label-width="100px">
                            <el-form-item label="任务名称">
                                <el-input v-model="form.taskname" style="width: 260px"></el-input>
                            </el-form-item>
                            <el-form-item label="标注类型">
                                <el-select v-model="annotationType" placeholder="请选择" style="width: 260px">
                                    <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="开始时间">
                                <el-date-picker type="datetime" placeholder="选择日期和时间" value-format="yyyy-MM-dd HH:mm:ss" v-model="form.beginDate" @change="beginDateChange" style="width: 260px;"></el-date-picker>
                            </el-form-item>

                            <el-form-item label="截止时间">
                                <el-date-picker type="datetime" placeholder="选择日期和时间" value-format="yyyy-MM-dd HH:mm:ss" v-model="form.endDate" @change="endDateChange" style="width: 260px;"></el-date-picker>
                            </el-form-item>

                            <el-form-item label="奖励积分">
                                <div class="block">
                                    <el-slider
                                        style="width: 260px"
                                        v-model="form.totalPoints"
                                        show-input>
                                    </el-slider>
                                </div>
                            </el-form-item>

                            <el-form-item label="标注人数">
                                <el-input v-model="form.maxParticipator" placeholder="请输入最大标注人数" style="width: 260px"></el-input>
                            </el-form-item>

                            <el-form-item label="任务级别">
                                <el-tooltip content="任务级别越高，接取任务需要的用户级别也越高" placement="bottom" effect="light">
                                    <el-input-number v-model="form.taskLevel" @change="handleChange" :min="1" :max="10" label="描述文字" style="width: 260px"></el-input-number>
                                </el-tooltip>
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

                            <el-form-item label="任务描述">
                                <el-input type="textarea" v-model="form.taskInfo" style="width: 260px" :rows="2" resize="none"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="onSubmit" style="width: 100px">立即创建</el-button>
                                <el-button style="width: 100px">取消</el-button>
                            </el-form-item>
                        </el-form>
                </div>
            </el-col>

            <el-col :span="15">
                <div>
                    <lightbox :images="imgList">
                    </lightbox>
                </div>
            </el-col>

        </el-row>

        <el-dialog title="请输入标注分类" :visible.sync="dialogTableVisible">
            <el-form id="parentForm">
                <el-form-item label="分类1">
                    <el-input  auto-complete="off" v-model="form.classifiedInfo[0]"></el-input>

                </el-form-item>

                <el-form-item label="分类2">
                    <el-input  auto-complete="off" v-model="form.classifiedInfo[1]"></el-input>

                </el-form-item>

                <el-form-item label="分类3">
                    <el-input  auto-complete="off" v-model="form.classifiedInfo[2]"></el-input>

                </el-form-item>

                <el-form-item label="分类">
                    <el-input  auto-complete="off" v-model="form.classifiedInfo[3]"></el-input>

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
          isFinished: false,
            classifiedInfo:['','','','']
        },
        action: 'http://upload.qiniu.com/', // 替换自己的上传链接
        accept: 'image/png, image/jpeg, image/gif, image/jpg, image/bmp',
        multiple: true,
        token: '',
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
            this.form.imgUrlList.push(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`);
            this.imgList.push({src:`http://p6r9un2qj.bkt.clouddn.com/${res.key}`})
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
                self.success();
            }).catch((err)=> {
                this.$message.error("创建失败！")
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
                this.token = response.data.data;
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
    },
    mounted() {
        var username = localStorage.getItem('username')
        if(username != null && username.length > 0) {
                //donothing
        }else {
            this.$message('请先登陆');
            this.$router.push('login');
        }
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
    }

    .btn {
        padding: 5px 5px;
        border: 1px solid #ccc;
        border-radius: 3px;
    }
</style>
