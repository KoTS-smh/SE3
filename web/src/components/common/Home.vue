<template>
    <div class="wrapper">
        <v-head></v-head>
        <component :is="currentView"></component>
        <div class="content">
            <transition name="move" mode="out-in"><router-view></router-view></transition>
        </div>
    </div>
</template>

<script>
    import vHead from './Header.vue';
    import vSidebar from './Sidebar.vue';
    import adSidebar from './AdminSidebar.vue';
    export default {
        components:{
            vHead, vSidebar,
            component1: vSidebar,
            component2: adSidebar
        },
        data() {
            return {
                currentView: 'component1'
            }
        },
        mounted() {
            var username = localStorage.getItem('username');
            if(username == 'admin'){
                this.currentView = 'component2'
            }else{
                this.currentView = 'component1'
            }
        }
    }
</script>
