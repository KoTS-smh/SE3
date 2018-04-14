<template>
    <div class="table">
        <!-- <el-carousel height="300px">
        <el-carousel-item v-for="item in imgList" :key="item">
            <h3><img :src="item" alt=""></h3>
        </el-carousel-item>
        </el-carousel> -->

        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 表格</el-breadcrumb-item>
                <el-breadcrumb-item>接取任务</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-row>
        <el-col :span="12" class="taskInfos"><div class="grid-content bg-purple">
            <!-- <h2>
            <span>任务名称: </span>
            <span>{{task.name}}</span>
            </h2>

            <div>
                <span>发起人: </span>
                <span>{{ task.sponsor }}</span>
            </div>

            <div class="block">
                <span>开始时间: </span>

            </div> -->

            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
                <el-form-item label="任务名称" prop="name" class="formItems">
                    <el-input v-model="ruleForm.name" readonly></el-input>
                </el-form-item>
                <el-form-item label="发起人" prop="sponsorName" class="formItems">
                    <el-input v-model="ruleForm.sponsorName" readonly></el-input>
                </el-form-item>
                <el-form-item label="发起时间" prop="startTime" class="formItems">
                    <el-input v-model="ruleForm.startTime" readonly></el-input>
                </el-form-item>
                <el-form-item label="截止时间" prop="endTime" class="formItems">
                    <el-input v-model="ruleForm.endTime" readonly></el-input>
                </el-form-item>
                <el-form-item label="标注类型" prop="tagType" class="formItems">
                    <el-input v-model="ruleForm.tagType" readonly></el-input>
                </el-form-item>
                <el-form-item label="任务级别" prop="taskLevel" class="formItems">
                    <el-input v-model="ruleForm.taskLevel" readonly></el-input>
                </el-form-item>
                <el-form-item label="任务进度" prop="process" class="formItems">
                    <el-progress type="circle" :percentage="0"></el-progress>
                </el-form-item>

            </el-form>
        </div></el-col>
        <el-col :span="12"><div class="grid-content bg-purple-light" >
            <el-carousel height="400px">
            <el-carousel-item v-for="item in imgList" :key="item" >
                <img :src="item" alt="">
            </el-carousel-item>
            </el-carousel>
        </div></el-col>
        </el-row>

        <el-row>
            <el-button type="primary">接受任务</el-button>
            <el-button type="success">开始标注</el-button>
        </el-row>


        <!-- <div class="block">
            <h2>上传结果</h2>
            <div id="result">
                <p>hash: {{hashes}}</p>
                <p>key: {{keys}}</p>
            </div>
            <div>
                <img :src="item" alt="hashes" class="viewimg" v-for="item of hashes" :key="item">
            </div>
            <div>
                <img :src="item" alt="keys" class="viewimg" v-for="item of keys" :key="item">
            </div>
        </div>
        <div class="block" id="ajaxErr" v-show="uploadMsg.length">
            <h4>uploadMsg: </h4>
            <p v-for="(item, index) in uploadMsg" :key="index">{{item}}</p>
        </div> -->

    </div>


</template>

<script>
import upload from '../../common/upload.vue'
export default {
    name: 'app',
    components: {
            upload
    },
    data() {

        return {
            readonly: true,
            currentDate: new Date(),
            imgList: [
                'http://p0.ifengimg.com/pmop/2017/0925/2D6C95104267D4F27325E7502509FF0BCB67304F_size1443_w479_h314.gif',
                'https://up.enterdesk.com/edpic_360_360/23/c5/a5/23c5a544259db587a535efaaba046187.jpg'
            ],
            task: {
                name: '任务1',
                sponsor: 'tony马'

            },
            value5: '',
            ruleForm: {
                name: '任务1',
                sponsorName: 'tony王',
                startTime: '2018/4/6 10:27',
                endTime: '2018/5/6 10:27',
                tagType: '标框标注',
                taskLevel: 2
            },
            rules: {

            }
        }
    },
    methods: {

        }
}
</script>


<style>
    .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }

  .clearfix:after {
      clear: both;
  }

  .forPics {
      /* line-height: 20px; */
  }

  .taskInfos {
      /* line-height: 40px; */
  }

  .formItems {
      width: 300px;
      vertical-align: middle;
  }

    .block,
    .viewimg {
        margin: 15px;
    }
    .btn {
        padding: 10px 10px;
        border: 0px solid #ccc;
        border-radius: 3px;
    }
    .viewimg {
        max-width: 200px;
        border: 1px dashed #ccc;
        border-radius: 4px;
        padding: 5px;
        background-color: #ccc;
    }
</style>
