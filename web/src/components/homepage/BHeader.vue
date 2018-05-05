<template>
  <div class="header">
  	<div class="h-center">
        <search class="msearch"></search>
  	</div >
      <div style="text-align: center">
          <BMenu></BMenu>
      </div>
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
    margin -42px auto 0
    background-color white
    .h-center
    	width 960px
    	margin 0 auto
    	position relative
    	height 170px
    	transition .2s height

</style>
