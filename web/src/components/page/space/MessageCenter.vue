<template>
    <div class="message-center">
        <el-col class="top-col">
            <h4 style="margin-top:10px;margin-left:5px">系统通知</h4>
        </el-col>

        <el-row :sm="12" :md="6" v-for="(card, index) in card_list" :key="card.id">
            <message-card :message-info="card.messageInfo" :title="card.title" :messageId="card.messageId"></message-card>
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
            console.log('here')
            axios.post('http://localhost:8080/user/getMessage', {
                    "userId": localStorage.getItem("userId")
                }).then(response => {
                    console.log(response)
                    this.card_list = response.data.data
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
    background: #f3f3f3;
    height: 40px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
