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
                            <el-form-item label="起止时间">
                                <el-tooltip content="开始日期前的时间为预约期，用户可以预约任务，但不能标注。" placement="bottom" effect="light">
                                <el-date-picker
                                    v-model="dataValue"
                                    type="daterange"
                                    value-format="yyyy-MM-dd"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    :picker-options="pickerOptions"
                                    style="width: 260px">
                                </el-date-picker>
                                </el-tooltip>
                            </el-form-item>


                            <el-form-item label="标注人数">
                                <el-input v-model="form.maxParticipator" placeholder="请输入最大标注人数" style="width: 260px"></el-input>
                            </el-form-item>

                            <el-form-item label="上传图片">
                                <div id="app">
                                    <upload :action="action" :token="token" @on-upload="uploadFile" :multiple="multiple" :accept="accept" @on-error="uploadErr" @on-progress="progress">
                                        <template slot="form">
                                        </template>
                                        <template slot="picker">
                                            <span class="btn">点击按钮上传</span>
                                        </template>
                                    </upload>
                                    <div class="block" id="ajaxErr" v-show="uploadMsg.length">
                                        <h4>uploadMsg: </h4>
                                        <p v-for="(item, index) in uploadMsg" :key="index">{{item}}</p>
                                    </div>
                                </div>
                            </el-form-item>

                            <el-form-item style="height:100px;width: 380px" label="可选标签">
                                <div id="available-tags">
                                    <el-button
                                        :key="tag"
                                        v-for="tag in availableTags"
                                        size="mini"
                                        @click="handleAddTag(tag)"
                                        >
                                    {{tag}}
                                    </el-button>
                                </div>
                            </el-form-item>

                            <el-form-item style="width: 380px" label="任务标签">
                                <div id="for-tag">
                                    <el-tag
                                        :key="tag"
                                        v-for="tag in dynamicTags"
                                        closable
                                        :disable-transitions="false"
                                        @close="handleClose(tag)">
                                        {{tag}}
                                    </el-tag>

                                    <el-input
                                        class="input-new-tag"
                                        v-if="inputVisible"
                                        v-model="inputValue"
                                        ref="saveTagInput"
                                        size="small"
                                        @keyup.enter.native="handleInputConfirm"
                                        @blur="handleInputConfirm">
                                    </el-input>
                                </div>
                            </el-form-item>

                            <el-form-item label="任务描述">
                                <el-input type="textarea" v-model="form.taskInfo" style="width: 260px" :rows="2" resize="none"></el-input>
                            </el-form-item>

                            <el-form-item label="任务报酬">
                                <el-input v-model="form.reward" style="width: 260px" @focus="getReward"></el-input>
                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary" @click="onSubmit" style="width: 260px">立即创建</el-button>
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

        <el-dialog title="充值" :visible.sync="rechargeTableVisible">
            <el-container>
                <el-header style="margin-left:50px" class="myheader">
                    <span style="margin-right:200px"><icon style="margin-right:20px" name="credit-card"></icon> 充值 </span>
                </el-header>
                <el-main style="margin-left:40px">
                    <span style="margin-bottom:20px"><p>充值数量</p></span>
                    <div style="margin-top:20px">
                        <el-button plain @click="setNum1">6.0¥</el-button>
                        <el-button plain @click="setNum2">18.0¥</el-button>
                        <el-button plain @click="setNum3">68.0¥</el-button>
                    </div>

                    <div style="margin-top:20px">
                        <el-button plain @click="setNum4">233.0¥</el-button>
                        <el-button plain @click="setNum5">648.0¥</el-button>
                        <el-button plain @click="setNum6">998.0¥</el-button>
                    </div>


                    <div style="margin-top:50px;margin-left:330px">
                        <el-button type="primary" @click="recharge">充值</el-button>
                    </div>
                </el-main>
            </el-container>
        </el-dialog>

    </div>
</template>


<script>
import upload from '../../common/upload.vue'
import axios from 'axios'
import Icon from 'vue-awesome/components/Icon'
import 'vue-awesome/icons'
export default {
    name: 'app',
    components: {
        upload,
        Icon
    },
  data() {
      return {
        pickerOptions:{
            disabledDate(time) {
                return time.getTime() < Date.now();
            }
        },
          dataValue:'',
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
          totalPoints:3,
          maxParticipator: '',
          imgUrlList: [],
            reward:'',
          state: 'appoint',
          classifiedInfo:['','','','']
        },
        action: 'http://upload.qiniu.com/', // 替换自己的上传链接
        accept: 'image/png, image/jpeg, image/gif, image/jpg, image/bmp',
        multiple: true,
        token: '',
        hashes: [],
        keys: [],
        uploadMsg: [],
        annotationType: '',
        rechargeTableVisible: false,
        dynamicTags:[],
        inputVisible: false,
        inputValue: '',
        availableTags:[
            '动物',
            '人物',
            '自然',
            '日常用品',
            '建筑',
            '科技',
            '食物',
            '交通',
            '家具',
            '工业'
        ]
      }
    },
    methods: {
        uploadFile (res) {
            this.hashes.push(`http://p6r9un2qj.bkt.clouddn.com/${res.hash}`);
            this.keys.push(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`);
            console.log(`http://p6r9un2qj.bkt.clouddn.com/${res.key}`);
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
            this.form.endDate = this.dataValue[1];
            this.form.beginDate = this.dataValue[0];
            this.form.dynamicTags = this.dynamicTags;
            
            if( !this.isNum(this.form.maxParticipator)) {
                
                this.$message.error('请输入正确标注人数！');
                return;
            }
            if(this.form.reward!==''&&this.form.reward!=='请先完善任务信息！') {
                axios.post('http://localhost:8080/task/create', this.form).then(function (response) {
                    console.log(response);
                    if (response.data.code == 12222) {
                        self.shortOfBalance();
                        // self.gotoRecharge();
                        self.rechargeTableVisible = true;
                    } else {
                        self.success();
                    }
                }).catch((err) => {
                    this.$message.error("创建失败！")
                });
            }
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
        shortOfBalance() {
            this.$message('余额不足，请充值！')
        },
        gotoRecharge() {
            this.$router.push('recharge')
        },
        placeToken() {
            axios.post("http://localhost:8080/user/getToken").then(response =>{
                this.token = response.data.data;
            }).catch(err => {
                console.log(err)
            })
        },
        getReward(){
            if(this.form.taskname!==''&&this.form.imgUrlList.length!==0&&this.annotationType !==''&&this.form.maxParticipator!==''&&this.form.taskInfo!=='') {
                this.form.reward = 0;
                this.form.annotationType = this.annotationType;
                this.form.endDate = this.dataValue[1];
                this.form.beginDate = this.dataValue[0];
                this.form.dynamicTags = this.dynamicTags;
                axios.post("http://localhost:8080/task/getReward", this.form).then(reponse => {
                    this.form.reward = reponse.data.data;
                }).catch(err => {
                    this.form.reward = '网络异常。';
                })
            }else{
                this.form.reward ='请先完善任务信息！';
            }
        },
        recharge() {
            axios.post('http://localhost:8080/user/recharge', {'userId':localStorage.getItem('userId'), 'rechargeNum': this.rechargeNum})
            .then(response => {
                console.log(response);
                if(response.data.code == 0) {
                    this.$message('充值成功!');
                    this.sleep(500).then(() => {
                        this.rechargeTableVisible = false;
                    })
                }
            })
        },
        setNum1() {
            this.rechargeNum = 6;
        },
        setNum2() {
            this.rechargeNum = 18;
        },
        setNum3() {
            this.rechargeNum = 68;
        },
        setNum4() {
            this.rechargeNum = 233;
        },
        setNum5() {
            this.rechargeNum = 648;
        },
        setNum6() {
            this.rechargeNum = 998;
        },
        sleep (time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
        handleClose(tag) {
            this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
            this.availableTags.push(tag);
        },
        handleAddTag(tag) {
            this.availableTags.splice(this.availableTags.indexOf(tag), 1);
            this.dynamicTags.push(tag)
        },
        isNum(s) {
            if(s != null) {
                var r, re;
                re = /\d*/i; //\d表示数字,*表示匹配多个数字
                r = s.match(re);
                return (r==s)?true:false;
            }
            return false;
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
        var username = localStorage.getItem('username');
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

    .el-tag + .el-tag {
    margin-left: 10px;
    }
    .button-new-tag {
        margin-left: 10px;
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }
    .input-new-tag {
        width: 90px;
        margin-left: 10px;
        vertical-align: bottom;
    }
</style>
