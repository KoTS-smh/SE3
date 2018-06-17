<template>
    <div class="pictures">
        <!-- <el-row :gutter="10">
            <el-col :sm="12" :md="6" v-for="(card, index) in card_list" :key="card.id">
                <picture-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :viewedTimes="card.viewedTimes" :reward="card.reward" :upRate="card.upRate"></picture-card>
            </el-col>

        </el-row> -->
        <el-row :sm="12" :md="6" v-for="(card, index) in card_list" :key="card.id">
            <rec-task-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :viewedTimes="card.viewedTimes" :reward="card.reward" :upRate="card.upRate" :tagList="card.tagList" @getInfo="getInfo(card)"></rec-task-card>
        </el-row>
    </div>
</template>

<script>
    import axios from 'axios'
    import RecTaskCard from '../../common/RecTask_card.vue'
    export default {
        name: 'Picture',
        components: {
        'rec-task-card': RecTaskCard
        },
        data() {
            return {
                card_list:[
                ]
            }
        },
        methods: {
            placeData() {
                var userId = localStorage.getItem('userId')
                axios.post('http://localhost:8080/task/recommend', {"userId": userId, "password": ''})
                .then(response => {
                    console.log(response.data.data);
                    var cardList = [];
                    cardList = response.data.data;
                    if(cardList.length == 0) {
                        const h = this.$createElement;
                        this.$notify({
                            title: '无任务可推荐',
                            message: h('i', { style: 'color: teal'}, '当前无任务可推荐！')
                        });

                        return;
                    }
                    for(var i = 0;i < cardList.length;i++) {
                        var tags = cardList[i].tagList
                        for(var j = 0;j < tags.length;j++) {
                            if(tags[j] == 'HUMAN')
                                tags[j] = '人物'
                            else if(tags[j] == 'ANIMAL')
                                tags[j] = '动物'
                            else if(tags[j] == 'NATURE')
                                tags[j] = '自然'
                            else if(tags[j] == 'DAILYSTUFF')
                                tags[j] = '日常用品'
                            else if(tags[j] == 'BUILDING')
                                tags[j] = '建筑'
                            else if(tags[j] == 'TECHNOLOGY')
                                tags[j] = '科技'
                            else if(tags[j] == 'FOOD')
                                tags[j] = '食物'
                            else if(tags[j] == 'TRAFFIC')
                                tags[j] = '交通'
                            else if(tags[j] == 'FURNITURE')
                                tags[j] = '家具'
                            else if(tags[j] == 'INDUSTRY')
                                tags[j] = '工业'
                            else if(tags[j] == 'OTHER')
                                tags[j] = '其他'
                        }
                    }                  
                    this.card_list = cardList;
                })
            },

            getInfo: function(item) {
                var taskId = item.id;
                this.$router.push({path: 'checkTask', query:{"taskId":taskId}})
            }
        },

        mounted() {
		    this.placeData()
	    }
    }
</script>


<style>

</style>
