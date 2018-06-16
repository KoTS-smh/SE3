<template>
    <div class="card">
        <el-col class="title">
            <h4 style="margin-left:15px;margin-top:10px;margin-bottom:3px">{{title}}</h4>
            <p style="margin-left:20px">{{messageInfo}}</p>
            <el-tooltip class="item" effect="dark" content="已读" placement="top">
                <el-button type="primary" class="check-button" icon="el-icon-check" circle @click="setRead"></el-button>
            </el-tooltip>
            
            <el-tooltip class="item" effect="dark" content="删除" placement="top">
                <el-button type="danger" class="close-button" icon="el-icon-close" circle @click="deleteMessage"></el-button>
            </el-tooltip> 
        </el-col>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    props:{
        messageInfo: {
            type: String,
            default: '测试内容'
        },
        title: {
            type: String,
            default: '测试标题'
        },
        messageId: {
            type: Number
        }
    },
    data() {
        return {
            mId: this.messageId
        }
    },
    methods: {
        setRead() {
            axios.post('http://localhost:8080/user/setRead', {"messageId": this.mId})
            .then(response => {
                console.log(response);
                this.$emit('setReadMethod', this.mId);
            })
        },
        deleteMessage() {
            this.$emit('sonMethod',this.mId);
        }
    }
}
</script>

<style>
    .title{
        margin-top: 10px;
        background: #f3f3f3;
        height: 100px;
        border-radius: 8px;
    }
    .check-button{
        margin-left: 1000px;
    }
    .close-button{
        margin-left: 1050px;
    }
</style>
