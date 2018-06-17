<template>
  <div class="pictures">
    <el-row :gutter="10">
		  <!-- <div class="block pagination" style="padding: 10px;text-align: right"> -->
				<el-col :span="17">
				<h1 style="margin-top:6px">预约中任务</h1>
				</el-col>
				<el-col :span="7">
          <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-sizes="[8, 16, 24, 32]"
          :page-size="perPage"
          layout="total, sizes, prev, pager, next"
          :total="appoint_list.length">
          </el-pagination>	
				</el-col>				
			<!-- </div> -->

      <el-col :sm="12" :md="6" v-for="(card, index) in showAppointList" :key="card.id">
        <picture-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :viewedTimes="card.viewedTimes" :reward="card.reward" :upRate="card.upRate" @remove="removeItem(card)" @getInfo="getInfo(card)"></picture-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" style="margin-top:20px">
        <el-col :span="17">
				<h1 style="margin-top:6px">进行中任务</h1>
				</el-col>
        <el-col :span="7">
          <el-pagination
          @size-change="handleSizeChange2"
          @current-change="handleCurrentChange2"
          :current-page.sync="currentPage"
          :page-sizes="[8, 16, 24, 32]"
          :page-size="perPage2"
          layout="total, sizes, prev, pager, next"
          :total="ongoing_list.length">
          </el-pagination>	
				</el-col>
        <el-col :sm="12" :md="6" v-for="(card, index) in showOngoingList" :key="card.id">
          <picture-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :viewedTimes="card.viewedTimes" :reward="card.reward" :upRate="card.upRate" @remove="removeItem(card)" @getInfo="getInfo(card)"></picture-card>
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
        appoint_list:[

        ],
        ongoing_list:[

        ],
        currentPage: 1,
        perPage: 16,
        currentPage2: 1,
        perPage2: 16,
        messageNum: ''
      };
    },
    computed: {
      showAppointList() {
        return this.appoint_list.slice((this.currentPage - 1) * this.perPage, this.currentPage * this.perPage);
      },
      showOngoingList() {
        return this.ongoing_list.slice((this.currentPage2 - 1) * this.perPage2, this.currentPage2 * this.perPage2);
      }
    },
    methods: {
      handleSizeChange(val) {
        this.perPage = val
      },
      handleCurrentChange(val) {
        this.currentPage = val
      },
      handleSizeChange2(val) {
        this.perpage2 = val
      },
      handleCurrentChange2(val) {
        this.currentPage = val;
      },

	  placeData() {
		  axios.post('http://localhost:8080/getContent').then(response => {
			  console.log(response.data)
			  var array = JSON.parse(response.data.data)
			  // this.card_list = array
        for(var i = 0;i < array.length; ++i) {
          if(array[i].taskState == 'appoint'){
            this.appoint_list.push(array[i]);
          }else if(array[i].taskState == 'ongoing'){
            this.ongoing_list.push(array[i]);
          }
        }
		  })
	  },

	  getInfo: function(item) {
		  var taskId = item.id;
      axios.get('http://localhost:8080/task/viewedTimeInc', {
        params: {
          taskId: taskId
        }
      })
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
