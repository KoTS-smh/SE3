<template>
    <el-container>
    <el-aside width="300px">
        <span style="top:50px">
            账户余额: {{balance}} ¥
        </span>
    </el-aside>
    <el-container>
        <el-header style="margin-left:50px" class="myheader">
            <span style="margin-right:500px"><icon style="margin-right:330px" name="credit-card"></icon> 充值 </span>
        </el-header>
        <el-main style="margin-left:40px">
            <span style="margin-bottom:20px"><p>充值数量</p></span>
            <div style="margin-top:20px">
                <el-button plain @click="setNum1">6.0¥</el-button>
                <el-button plain @click="setNum2">18.0¥</el-button>
                <el-button plain @click="setNum3">68.0¥</el-button>
                <el-button plain @click="setNum4">233.0¥</el-button>
                <el-button plain @click="setNum5">648.0¥</el-button>
                <el-button plain @click="setNum6">998.0¥</el-button>
            </div>


            <div style="margin-top:50px;margin-left:330px">
                <el-button type="primary" @click="recharge">充值</el-button>
            </div>
        </el-main>
    </el-container>
    </el-container>
</template>


<script>
import Icon from 'vue-awesome/components/Icon'
import 'vue-awesome/icons'
import axios from 'axios'
export default {
    components: {
        Icon
    },
    data() {
        return {
            rechargeNum: 0,
            username: '',
            balance: 0
        }
    },
    methods: {
        recharge() {
            axios.post('http://localhost:8080/user/recharge', {'userId':localStorage.getItem('userId'), 'rechargeNum': this.rechargeNum})
            .then(response => {
                console.log(response);
                if(response.data.code == 0) {
                    this.$message('充值成功!')
                    this.sleep(500).then(() => {
                        location.reload();
                    })
                }
            })
        },
        setNum1() {
            this.rechargeNum = 6;
        },
        setNum2() {
            this.rechargeNum = 18;
        },
        setNum3() {
            this.rechargeNum = 68;
        },
        setNum4() {
            this.rechargeNum = 233;
        },
        setNum5() {
            this.rechargeNum = 648;
        },
        setNum6() {
            this.rechargeNum = 998;
        },
        placeData() {
            this.username = localStorage.getItem("username");
            axios.post('http://localhost:8080/user/getBalance', {'userId':localStorage.getItem('userId'), 'rechargeNum': this.rechargeNum})
            .then(response => {
                console.log(response);
                this.balance = response.data.data;
            })
            
        },
        sleep (time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        }
    },
    mounted() {
        this.placeData();
    }
}
</script>


<style>
    .myheader {
        background-color: #edf2f3;
        
        text-align: center;
        line-height: 60px;
    }
    .user-logo{
        position: absolute;
        left:20px;
        top:30px;
        width:40px;
        height:40px;
        border-radius: 50%;
    }
    

    
</style>
