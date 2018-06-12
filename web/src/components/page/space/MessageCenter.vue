<template>
    <div class="message-center">
        <el-col class="top-col">
            <h4 style="margin-top:10px;margin-left:5px">系统通知</h4>
        </el-col>

        <el-row :sm="12" :md="6" v-for="(card, index) in card_list" :key="card.id">
            <message-card :message-info="card.messageInfo" :title="card.title" :messageId="card.messageId" v-on:sonMethod="deleteMessage"></message-card>
        </el-row>
    </div>
</template>


<script>
import MessageCard from '../../common/Message_card.vue'
import axios from 'axios'

export default {
    components: {
      'message-card': MessageCard
    },

    data() {
        return {
            card_list:[
                
            ]
        }
    },
    methods: {
        getMessages() {
            axios.post('http://localhost:8080/user/getMessage', {
                    "userId": localStorage.getItem("userId")
                }).then(response => {
                    console.log(response)
                    this.card_list = response.data.data
                })
        },
        deleteMessage(messageId) {
            axios.post('http://localhost:8080/user/deleteMessage', {"messageId": messageId})
            .then(response => {
                console.log(response);
                if(response.data.code == 0){
                    var list = this.card_list;
                    for(let i = 0; i < list.length;i++) {
                        if(list[i].messageId == messageId){
                            list.splice(i, 1)
                        }
                    }
                }
            })
        }
    },

    mounted() {
        this.getMessages();
    }
}
</script>


<style>
  .el-row {
    margin-bottom: 20px;
    
  }
  .el-col {
    border-radius: 4px;
    height: 40px;
  }
</style>
