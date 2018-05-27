<template>
    <div style="height:100%">
        <el-container style="height:100% " >
            <el-header>
                <!-- <TopContainer></TopContainer> -->
                <component :is="currentView"></component>

            </el-header>
            <el-main style="height:100%">
                <BHeader></BHeader>
                <el-row>
                <el-col :span="4">&nbsp;</el-col>
                <el-col :span="16">
                    <BContent></BContent>
                </el-col>
                <el-col :span="4">&nbsp;</el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import TopContainer from 'components/homepage/TopContainer.vue'
    import BHeader from 'components/homepage/BHeader.vue'
    import BContent from 'components/homepage/BContent.vue'
    import BNavSide from 'components/homepage/BNavSide.vue'
    import TopContainerAfterLogin from 'components/homepage/TopContainerAfterLogin.vue'
    import { mapGetters } from 'vuex'
    export default {
        name: 'app',
        components: {
            TopContainer,
            BHeader,
            BContent,
            BNavSide,
            TopContainerAfterLogin,
            component1: TopContainer,
            component2: TopContainerAfterLogin,
        },

        data() {
            return {
                showMask: false,
                currentView: 'component1'
            }
        },
        watch: {
            options() {
                console.log('options 变化了')
            },
            items() {
                console.log('items 变化了')
            }
        },
        computed: {
            ...mapGetters([
                'requesting',
                'error',
                'rows'
            ]),
            options() {
                let options = {
                    offset: 100, //偏移的距离
                    items: this.rows,
                    offsetTop: 0 //距离顶部距离
                }
                return options
            }
        },
        methods: {
            isShowMask() {
                this.showMask = !this.showMask
            },
            placeData() {
                var username = this.$route.query.username
                var isLogin = this.$route.query.isLogin
                var usernameInStorage = localStorage.getItem("username");
                if(isLogin == true && usernameInStorage.length > 0){
                    this.currentView = 'component2'
                }
            }
        },
        mounted() {
            var userId = localStorage.getItem('userId');
            if(userId != null && userId.length > 0){
                this.currentView = 'component2'
            }else{
                this.currentView = 'component1'
            }
            this.placeData();
        }
    }
</script>

<style lang="stylus">
    #app
        font-family "Microsoft YaHei",Arial,Helvetica,sans-serif
        -webkit-font-smoothing antialiased
        font-size 12px
        margin 0
        padding 0
        background #fff
        color #222
        min-width 990px
        tap-highlight-color transparent
        -webkit-tap-highlight-color transparent
</style>
