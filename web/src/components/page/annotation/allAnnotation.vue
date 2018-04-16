<template >
  <div>
  <el-container style="height: 716px">
    <el-header style="text-align: right; font-size: 15px">
      <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">标注中心</el-menu-item>
      <el-submenu index="2">
      <template slot="title">我的工作台</template>
      <el-menu-item index="2-1">选项1</el-menu-item>
      <el-menu-item index="2-2">选项2</el-menu-item>
      <el-menu-item index="2-3">选项3</el-menu-item>
      <el-submenu index="2-4">
      <template slot="title">选项4</template>
      <el-menu-item index="2-4-1">选项1</el-menu-item>
      <el-menu-item index="2-4-2">选项2</el-menu-item>
      <el-menu-item index="2-4-3">选项3</el-menu-item>
      </el-submenu>
      </el-submenu>
        <span style="color: dodgerblue">孙铭辉</span>
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
        <el-main style="height: 596px;display: flex;align-items:center;justify-content: center">
            <div style="width: 100%;height: 100%">
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
              :total="1000"
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
            <el-progress :text-inside="true" :stroke-width="18" :percentage="70" style="width: 280px"></el-progress>
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
        <el-row>
          <el-button-group style="margin-left: 230px;margin-top: 20px">
            <el-tooltip content="保存" placement="bottom">
              <el-button type="primary" icon="el-icon-upload" style="width: 80px"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="bottom">
              <el-button type="primary" icon="el-icon-delete" style="width: 80px"></el-button>
            </el-tooltip>
          </el-button-group>
        </el-row>
        <el-button type="primary" style="position: relative; left: 295px; top: 300px">离 开 <i class="el-icon-d-arrow-right"></i></el-button>
      </el-aside>
    </el-container>
  </el-container>
  </div>
</template>

<script>
  let img =new Image();
  img.src='https://p.upyun.com/docs/cloud/demo.jpg';
    export default {
      created(){

      },
      data() {
        return {
          activeIndex: '1',
          currentPage: 1,
          textarea:'',
          imgX:img.width,
          imgY:img.height,
          imgUrl: "url('https://p.upyun.com/docs/cloud/demo.jpg')"
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
          console.log(`当前页: ${val}`);
        },
        fetchData(){
            axios.get('http://localhost:8080/annotation/getAll',{
                params:{
                    annotationId: localStorage.getItem('')
                }
            })
        }
      },
        name: "allAnnotation"
    }

    // window.onload = function() {
    //   let can = document.getElementById('canvas');
    //    let img = new Image();
    //    img.src="https://p.upyun.com/docs/cloud/demo.jpg";
    //
    //    let ctx = can.getContext('2d');
    //    img.onload=function () {
    //       ctx.drawImage(img,0,0,img.width,img.height);
    //     };
    // };
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
