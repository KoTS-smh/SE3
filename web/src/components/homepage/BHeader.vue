<template>
  <div class="header" style="background: url('https://ws1.sinaimg.cn/large/0073JsqJly1fsea6alqjqj3107065jsd.jpg') no-repeat;background-size:1380px 240px;">
  	<div class="h-center">
        <search class="msearch"></search>
  	</div >
      <el-row>
        <el-col :span="4">&nbsp;</el-col>
        <el-col :span="16">
            <BMenu></BMenu>
        </el-col>
        <el-col :span="4">&nbsp;</el-col>
      </el-row>
  </div>
</template>

<script>
import Search from 'components/homepage/Search'
import BMenu from 'components/homepage/BMenu'
import Banner from 'components/homepage/Banner'
export default {
  components: {
    Search,
    BMenu,
    Banner
  }
}
</script>

<style lang="stylus">
 .header
     position relative
     
     background-color white
    .h-center
    	width 960px
    	margin 0 auto
    	position relative
    	height 170px
    	transition .2s height

</style>
