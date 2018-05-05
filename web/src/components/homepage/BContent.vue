<template>
  <div class="pictures">
    <el-row :gutter="10">
      <div class="block pagination" style="padding: 10px;text-align: right">
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
      <el-col :sm="12" :md="6" v-for="(card, index) in card_list" :key="card.id">
        <picture-card :name="card.name" :url="card.url" :description="card.description" :id="card.id" :views="card.views" :comments="card.comments" @remove="removeItem(card)"></picture-card>
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
          {
            name: 'In Snow',
            url: require('../../assets/img/picture1.jpg')
          },
          {
            name: 'Storm',
            url: require('../../assets/img/picture2.jpg')
          },
          {
            name: 'Little Door Gods',
            url: require('../../assets/img/picture3.jpg')
          },
          {
            name: 'Lesser Panda',
            url: require('../../assets/img/picture4.jpg')
          },
          {
            name: 'Immortal',
            url: require('../../assets/img/picture5.jpg')
          }
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
	  }
    },

	mounted() {
		this.placeData()
	}
  }
</script>
