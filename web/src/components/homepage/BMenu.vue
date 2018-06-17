<template>
	<div class="menu" style="background: #E4F0A2">
		<div class="menu-wrapper">
			<ul class="nav-menu">
				<BMenuItem  v-for="i in classify" :item="i"></BMenuItem>
			</ul>
			<div class="menu-r">
				<!--菜单右端，可添加额外功能-->
			</div>
		</div>
	</div>
</template>

<script>
import BMenuItem from 'components/homepage/BMenuItem'
import axios from 'axios'
export default {
	data() {
		return {
			showMobileLink: false,
			classify: [
				{
					title: '首页',
					num: 0,
					home: true
				},
				{
					title: '标框标注',
					num: 468,	
				},
				{
					title: '分类标注',
					num: 61
				},
				{
					title: '区域标注',
					num: 749
				},
				{
					title: '整体标注',
					num: 108
				},
				{
					title: '全部任务',
					num: 999
				},
				{
					title: '使用教程',
					num: 0,
					sequare: true
				},
				{
					title: '个人空间',
					num: 0,
					live: true,
					link: 'http://localhost:8888/#/personalSpace'
				}
			]
		}
	},
	components: {
		BMenuItem
	},
	methods: {
		placeData(){
			axios.get('http://localhost:8080/taskNum')
			.then(response => {
				console.log(response);
				var array = JSON.parse(response.data.data);
				this.classify = array;
			})
		}
	},

	mounted() {
		this.placeData();
	}
}
</script>

<style lang="stylus" scoped>
	.menu
		width 100%
		background #fff
		.menu-wrapper
			position relative
			width 980px
			margin 0 auto
			padding 6px 0
			z-index 100
			.nav-menu
				zoom 1
				display inline-block
				vertical-align top
				height 50px
			.menu-r
				position absolute
				right 0px
				top 0px
				height 50px
				padding 6px 0
				.random-p
					width 80px
					height 44px
					margin 3px 12px
					display inline-block
					vertical-align top
					overflow hidden
					img
						display block
						max-width 100%
						height 100%
						margin 0 auto
				.mobile-p
					display inline-block
					vertical-align top
					margin 3px 0
					width 58px
					height 44px
					background url("../../assets/images/app-link.png") center center no-repeat
					position relative
					overflow visible
					.mobile-p-box
						position absolute
						overflow hidden
						top 44px
						width 259px
						height 174px
						right -20px
						background url("../../assets/images/app-box.png") center center no-repeat
						transition .2s
						transition-property opacity
						&.fade-enter-active, &.fade-leave
							opacity 1
						&.fade-enter, &.fade-leave-active
							opacity 0
						.mobile-p-qrcode
							position absolute
							top 30px
							width 100px
							height 100px
							left 80px
							background url("../../assets/images/app-qrcode.png") center center no-repeat
</style>
