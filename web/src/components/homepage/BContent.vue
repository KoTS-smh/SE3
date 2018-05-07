<template>
  <div class="pictures">
    <el-row :gutter="10">
	  
			  
		  <!-- <div class="block pagination" style="padding: 10px;text-align: right"> -->
				<el-col :span="17">
				<h1 style="margin-top:6px">任务列表</h1>
				</el-col>
				<el-col :span="7">
				<el-pagination
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				:current-page.sync="currentPage"
				:page-sizes="[8, 16, 24, 32]"
				:page-size="perPage"
				layout="total, sizes, prev, pager, next"
				:total="card_list.length">
				</el-pagination>	
				</el-col>				
			<!-- </div> -->

      <el-col :sm="12" :md="6" v-for="(card, index) in showCardList" :key="card.id">
        <picture-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :views="card.views" :comments="card.comments" @remove="removeItem(card)" @getInfo="getInfo(card)"></picture-card>
      </el-col>

    </el-row>
  </div>
</template>

<script type="javascript">
  import PictureCard from '../common/picture_card.vue'
  import axios from 'axios'
  export default{
    name: 'Picture',
    components: {
      'picture-card': PictureCard
    },
    data() {
      return {
        pictures: [
          
        ],
        card_list: [
          
        ],
        currentPage: 1,
        perPage: 16
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
		  axios.post('http://localhost:8080/getContent').then(response => {
			  console.log(response.data)
			  
			  var array = JSON.parse(response.data.data)
			  this.card_list = array
			  console.log(array.length)
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
	.pictures{
		display: inline;
	}
</style>
