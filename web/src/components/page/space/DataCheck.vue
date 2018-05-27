<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 数据分析</el-breadcrumb-item>
                <el-breadcrumb-item>个人数据</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="积分">
                <el-input v-model="form.point" readonly="true" class="input_area"></el-input>
            </el-form-item>

            <el-form-item label="进行中任务数">
                <el-input v-model="form.tasknumber" readonly="true" class="input_area"></el-input>
            </el-form-item>

            <el-form-item label="全体排名">
                <el-input v-model="form.rank" readonly="true" class="input_area"></el-input>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import axios from "axios"
export default {
    data() {
        return {
            form: {
                point: "200",
                tasknumber: 100,
                rank: "前20%"
            }
        }
    },
    methods: {
        placeData() {
            var userId = localStorage.getItem('userId');
            axios.post("http://localhost:8080/personalData", {"userId": userId, "password": ''})
            .then(response => {
                console.log(response);
                this.form = response.data.data
            }).catch(err => {

            })
        }
    },

    mounted() {
        var username = localStorage.getItem('username')
        if(username != null && username.length > 0) {
                //donothing
        }else {
            this.$message('请先登陆');
            this.$router.push('login');
        }
        this.placeData()
    }
}
</script>

<style>
    .input_area {
        width: 30%;
    }
</style>
