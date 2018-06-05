<template>
  <div class="pictures">
    <el-row :gutter="10">
	
      <el-col :sm="12" :md="6" v-for="(card, index) in card_list" :key="card.id">
        <picture-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :views="card.views" :comments="card.comments" @remove="removeItem(card)" @getInfo="getInfo(card)"></picture-card>
      </el-col>

      <div class="block" style="text-align: center">
                    <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[8, 16, 24, 32]"
                    :page-size="perPage"
                    layout="total, sizes, prev, pager, next"
                    :total="card_list.length">
                    </el-pagination>
        </div>

    </el-row>
  </div>
</template>

<script type="javascript">
  import PictureCard from '../common/picture_card.vue'
  import axios from 'axios'
  export default{
    name: 'mycontent',
    components: {
      'picture-card': PictureCard
    },
    props: {
        searchInfo: {
            type: String,
            required: true,
            default: ''
        }
    },
    data() {
      return {
        pictures: [
          
        ],
        card_list: [
          
        ],
        currentPage: 1,
        perPage: 16,
        toSearch: this.searchInfo
      };
    },
    computed: {
      showCardList() {
        return this.card_list.slice((this.currentPage - 1) * this.perPage, this.currentPage * this.perPage);
      }
    },
    methods: {
      handleSizeChange(val) {
        this.perPage = val
      },
      handleCurrentChange(val) {
        this.currentPage = val
      },
      removeItem: function(item) {
        for(let i = 0; i < this.card_list.length; i++) {
          if(this.card_list[i].id === item.id) {
            this.card_list.splice(i, 1);
            return
          }
        }
      },

	  placeData() {
		  
	  },

	  getInfo: function(item) {
		  var taskId = item.id;
		  this.$router.push({path: 'checkTask', query:{"taskId":taskId}})
	  },
      searchForTasks(input, activeName, tag) {
        console.log(activeName);
          var taskType;
          if(activeName == 0){
              taskType = 'all'
          }else if(activeName == 1){
              taskType = 'option1'
          }else if(activeName == 2){
              taskType = 'option2'
          }else if(activeName == 3){
              taskType = 'option3'
          }else if(activeName == 4){
              taskType = 'option4'
          }
          axios.get('http://localhost:8080/search/getTask', {
              params:{
                  message: input,
                  taskType: taskType,
                  taskTag: tag
              }
          }).then(response => {
              console.log(response.data)
              // var array = JSON.parse(response.data.data)
              this.card_list = response.data.data
          })
      }
    },
    watch: {
        searchInfo: function() {
            this.toSearch = this.searchInfo;
            if(this.toSearch != null && this.toSearch.length > 0) {
                
            }
        }
    },

	mounted() {
		this.placeData()
	}
  }
</script>

<style>
	.pictures{
		display: inline;
	}
</style>
