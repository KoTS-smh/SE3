<template>
	<div>
		<!-- 推广 -->
		<div class="container-row">
			<BPromote></BPromote>
		</div>
		<!-- 各分类具体内容 -->
        <!--需传入数组Row-->
		<div class="container-row"  v-for="(row, index) in rows" :id="row.b_id" v-if='rows'>
			<BContentRow :category="row.category" :categoryId="row.categoryId" :row="row.item"></BContentRow>
		</div>
	</div>
</template>

<script>
import BContentTop from 'components/homepage/BContentTop'
import BPromote from 'components/homepage/BPromote'
import BContentRow from 'components/homepage/BContentRow'
import Banner from 'components/homepage/Banner'

import { mapGetters } from 'vuex'
export default {
	props: {
		rows: {
			type: Array
		}
	},
	components: {
		BContentTop,
		BPromote,
		BContentRow,
        Banner
	}
}
</script>

<style lang="stylus" scoped>
	.content
		zoom 1
		.container-top-wrapper
			&:after
				content ''
				display block
				visibility hidden
				height 0
				clear both
				font-size 0
		.container-top
			padding 0
			margin 0 auto
			margin-bottom 20px
			zoom 1
			width 980px
			&:after
				content ''
				display block
				visibility hidden
				height 0
				clear both
				font-size 0
			.b-l
				float left
				width 440px
			.b-r
				float right
				width 540px
				height auto
		.container-row
			margin 0 auto
			zoom 1
			width 980px
			&:after
				content ''
				display block
				visibility hidden
				height 0
				clear both
				font-size 0

</style>
