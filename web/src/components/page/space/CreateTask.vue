<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="12">
                <div class="grid-content bg-purple">
                    <el-form ref="form" :model="form" label-width="100px">
                            <el-form-item label="任务名称">
                                <el-input v-model="form.name"></el-input>
                            </el-form-item>
                            <el-form-item label="标注类型">
                                <!-- <el-select v-model="form.region"  placeholder="请选择标注类型">
                                    <el-option label="标框标注" value=""></el-option>
                                    <el-option label="分类标注" value=""></el-option>
                                    <el-option label="区域标注" value=""></el-option>
                                    <el-option label="整体标注" value=""></el-option>
                                </el-select> -->
                                <el-select v-model="value" placeholder="请选择">
                                    <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="截止时间">
                                <el-col :span="11">
                                <el-date-picker type="date" placeholder="选择日期" v-model="form.date1" style="width: 100%;"></el-date-picker>
                                </el-col>
                                <el-col class="line" :span="2">-</el-col>
                                <el-col :span="11">
                                <el-time-picker type="fixed-time" placeholder="选择时间" v-model="form.date2" style="width: 100%;"></el-time-picker>
                                </el-col>
                            </el-form-item>

                            <el-form-item label="奖励积分">
                                <div class="block">
                                    <el-slider
                                        v-model="form.value"
                                        show-input>
                                    </el-slider>
                                </div>
                            </el-form-item>

                            <el-form-item label="目标标注次数">
                                <el-input v-model="form.input" placeholder="请输入标注次数"></el-input>
                            </el-form-item>

                            <el-form-item label="任务级别">
                                <el-input-number v-model="form.num" @change="handleChange" :min="1" :max="10" label="描述文字"></el-input-number>
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
                                <el-input type="textarea" v-model="form.desc"></el-input>
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
                    <el-carousel-item v-for="item in imgList" :key="item" >
                        <img :src="item" alt="" >
                    </el-carousel-item>
                    </el-carousel>
                </div>
            </el-col>

        </el-row>

        <el-dialog title="请输入标注分类" :visible.sync="dialogTableVisible">
            <el-form id="parentForm">
                <el-form-item label="分类1">
                    <el-input  auto-complete="off" v-model="form.classification1"></el-input>

                </el-form-item>

                <el-form-item label="分类2">
                    <el-input  auto-complete="off" v-model="form.classification2"></el-input>

                </el-form-item>

                <el-form-item label="分类3">
                    <el-input  auto-complete="off" v-model="form.classification3"></el-input>

                </el-form-item>

                <el-form-item label="分类">
                    <el-input  auto-complete="off" v-model="form.classification4"></el-input>

                </el-form-item>
            </el-form>

            <div style="text-align:right;">
                <el-button type="primary" @click="changeClassification">确认</el-button>
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
        value: '',

        dialogTableVisible: false,
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: '',
          value: 0,
          input: '',
          num: 0,
          classification1: '',
          classification2: '',
          classification3: '',
          classification4: '',

        },

        action: 'http://upload.qiniu.com/', // 替换自己的上传链接
        accept: 'image/png, image/jpeg, image/gif',
        multiple: true,
        token: 'Ujh0-lp7Hk_fUrta0DWEQeBR4sWlavb3firT_Ivd:VmCHx8isaXhNM5VdessXh92qL6w=:eyJzY29wZSI6Im1yZ3MtYnVja2V0IiwiZGVhZGxpbmUiOjE1MjMzOTg1MTh9',
        hashes: [],
        keys: [],
        uploadMsg: []
      }
    },
    methods: {
        onSubmit() {
            console.log('submit!');
        },

        uploadFile (res) {
            this.hashes.push(`http://p6r9un2qj.bkt.clouddn.com/${res.hash}`)
            this.keys.push(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`)
            this.imgList.push(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`)
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
            var tmpName = this.form.name
            var tmpType = this.value
            var tmpDate = this.form.date1 + this.form.date2
            var tmpPoint = this.form.value
            var tmpTimes = this.form.input
            var tmpLevel = this.form.num
            var tmpImgList = this.imgList
            var tmpNote = this.form.desc
            var tmpC1 = this.form.classification1
            var tmpC2 = this.form.classification2
            var tmpC3 = this.form.classification3
            var tmpC4 = this.form.classification4

            function task(name, type, date, point, targetTimes, taskLevel, imgUrls, note, c1, c2, c3, c4){
                this.name = name
                this.type = type
                this.date = date
                this.point = point
                this.targetTimes = targetTimes
                this.taskLevel = taskLevel
                this.imgUrls = imgUrls
                this.note = note
                this.c1 = c1
                this.c2 = c2
                this.c3 = c3
                this.c4 = c4
            }

            var newTask = new task(tmpName, tmpType, tmpDate, tmpPoint, tmpTimes, tmpLevel, tmpImgList, tmpNote, tmpC1, tmpC2, tmpC3, tmpC4)
            var j = JSON.stringify(newTask)
            console.log(j)
            
            axios.get('http://localhost:8080/taskInfo', j).then(function (response) {
                console.log(response);
            }).catch(function(err) {
                console.log(err);
            });

        },
        addLabel() {
            var elFormItem = document.createElement('el-form-item')
            var parent = document.getElementById('parentForm')
            parent.appendChild(elFormItem)
        },
        changeClassification() {

        }

    },
    watch: {
        value: function(val) {
            if(val === 'option2'){
                this.dialogTableVisible = true
            }
        }
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
