<template>
	<div class="b-head">
		<span class="b-head-i" :class="category"></span>
		<span class="b-head-t">
			<a href="" :title="title">
				<h2>{{title}}</h2>
			</a>
		</span>
		<ul class="b-slt-tab">
			<li class="on">
				<span class="b-tab-text">有新动态</span>
			</li>
			<li class="">
				<span class="b-tab-text">最新投稿</span>
			</li>
		</ul>
		<div class="b-link-more">
			<a href="/video/music.html">
				更多<i class="b-icon b-icon-arrow-r"></i>
			</a>
		</div>
		<div class="read-push" style="display: block;">
			<span class="icon-refresh"></span>
			<span class="info">
				<b>4865</b>
				<em>条新动态</em>
			</span>
		</div>
	</div>
</template>

<script>
export default {
	props: {
		category: {
			type: String
		}
	},

    computed: {
		title() {
			let title = '未知标题';
			if (this.category) {
				switch(this.category) {
					case 'biaokuang':
						title = '标框标注'
						break
					case 'fenlei':
						title = '分类标注'
						break
					case 'quyu':
						title = '区域标注'
						break
					case 'zhengti':
						title = '整体标注'
						break
					default:
						title ="未知标题"
				}
			}
			return title
		}
	},
}
</script>

<style lang="stylus" scoped>
	.b-head
		height 24px
		white-space nowrap
		position relative
		.b-head-i
			position absolute
			left 0px
			top -8px
			background url("../../assets/icons.png") no-repeat
			width 40px
			height 39px
			display inline-block
			&.biaokuang
				background-position -141px -525px
			&.quyu
				background-position -141px -332px
			&.zhengti
				background-position -141px -1032px
			&.fenlei
				background-position -141px -396px
		.b-head-t
			float left
			margin-left 46px
			vertical-align middle
			display inline-block
			font-size 18px
			line-height 24px
			color #222
			a
				color #222
			h2
				font-size 24px!important
				line-height 24px
				font-weight normal
		.b-slt-tab
			position relative
			display inline-block
			vertical-align middle
			margin-left 20px
			float left
			li
				float left
				height 20px
				line-height 20px
				cursor pointer
				text-align center
				transition .2s
				transition-property border, color
				padding 1px 0 2px
				position relative
				border-radius 0
				border-bottom 1px solid transparent
				margin-left 24px
				&:first-child
					margin-left 0!important
				&.on
					background-color transparent
					border-color #00a1d6
					color #00a1d6
					&:before
						content ''
						display block
						position absolute
						left 50%
						margin-left -3px
						bottom 0px
						width 0
						height 0
						border 3px dashed #00a1d6
						border-bottom-style solid
						border-top 0
						border-left-color transparent
						border-right-color transparent
		.b-link-more
			float right
			text-align center
			margin-left 10px
			a
				display block
				width 52px
				height 22px
				line-height 22px
				background-color #fff
				border 1px solid #ccd0d7
				color #555
				border-radius 4px
				.b-icon-arrow-r
					display inline-block
					vertical-align middle
					background url("../../assets/icons.png") no-repeat
					width 6px
					height 12px
					margin -2px 0 0 5px
					background-position -478px -218px
		.read-push
			float right
			cursor pointer
			background-color #fff
			border 1px solid #ccd0d7
			border-radius 4px
			height 22px
			padding 0 10px
			.icon-refresh
				background url("../../assets/icons.png") -475px -89px no-repeat
				display inline-block
				width 12px
				height 13px
				vertical-align: top
				transition .2s
				margin-top 5px
			.info
				display inline-block
				vertical-align top
				line-height 22px
				margin-left 5px
				b
					font-weight bold
				em
					font-style normal
					font-weight normal
</style>
