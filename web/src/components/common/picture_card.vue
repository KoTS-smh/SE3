<template>
  <div class="lee-picture-card">
  
    <div class="preview" :style="{ backgroundImage: 'url(' + url + ')' }"></div>
    <div class="card-info" style="border-bottom:1px solid gainsboro;height:50px">
      <span class="large-text">&nbsp;&nbsp;{{ name }} - {{ id }}</span>
      <span class="description">&nbsp;&nbsp;{{ description }}</span>
    </div>
    <div class="card-footer justify-between" style="height: 40px">
      <div class="left" >
        <span ><icon name="eye" style="margin-top:12px; margin-left:5px"></icon>{{ viewedTimes }}</span>
        <span ><icon name="money-bill-alt" style="margin-top:12px margin-left:5px"></icon> {{ reward }}</span>
        <span v-if="this.upRate != ''" style="background-color:#1CC1E9;color:#FFFFFF">{{ upRate }}</span>
        <el-button class="moreBtn" size="mini" round @click="getInfo" style="float: right; margin-top:5px; margin-right:10px">more</el-button>
      </div>

    </div>
  </div>
</template>

<script type="javascript">
  import Icon from 'vue-awesome/components/Icon'
  import 'vue-awesome/icons'
  export default{
    name: 'pictureCard',
    components: {
        Icon
    },
    props: {
      name: {
        type: String,
        default: 'lijiaxunOuO'
      },
      id: {
        type: Number,
        default: 19940722
      },
      url: {
        type: String,
        default:''
      },
      description: {
        type: String,
        default: "The sorrowful gust of wind that blew right between you and me, Where did it find the loneliness it carried on the breeze, Looking up at the sky after shedding a stream of tears, I could see for miles of blue,It's never been so clear"
      },
      viewedTimes: {
        type: Number,
        default: 0
      },
      reward: {
        type: Number,
        default: 0
      },
      upRate: {
        type: String,
        default: ''
      }
    },
    methods: {
      removeItem: function() {
        this.$emit('remove')
      },
      getInfo(item) {
          this.$emit('getInfo')
      }
    }
  }
</script>

<style lang="scss">
    .preview{
        width: 100%;
        height: 150px;
        background-size: cover;
        border-radius: 5px;
    }

    .lee-picture-card{
        position: relative;
        background-color: #f3f3f3;
        margin-top: 2rem;
        border-radius: .4rem;
        height: 240px;
    }

    .large-text{
        font-size: 1.2rem;
    }

    .description{
        margin-top: 0rem;
        font-size: 1.0rem;

        color: gray;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        height: 20px;
    }

    .card-footer {
    padding: 0rem;
    text-align: left;

    .left {
      color: grey;
      font-size: 1.0rem;
      text-align: center center;

      span {
        margin-right: 0.5rem;

        &:last-child {
          margin-right: 0px;
        }
      }
      i {
        font-size: 1.4rem;
        margin-right: 0.5rem;
      }
      
    }
    .el-tag {
      cursor: pointer;
    }
  }

  .moreBtn {
      border: 1px solid #00bfff;
      color: #00bfff;
  }

</style>
