<template>
  <div>
    <div style="text-align:left;">
      <el-button size="small" @click="clickBack">回到首页</el-button>
      <div style="float:right;">
        <div v-if="user != null && user.role != null">
          <el-button size="small" @click="clickSearch" icon="el-icon-search">站内搜索</el-button>
          <el-button v-if="messageCnt == 0" size="small" @click="clickMessage" icon="el-icon-bell">消息通知</el-button>
          <el-button v-else size="small" @click="clickMessage" icon="el-icon-bell">{{ messageCnt }}条新消息</el-button>
          <el-button size="small" @click="clickPersonal" icon="el-icon-user-solid">个人中心</el-button>
        </div>
        <div v-else>
          <el-button size="small" @click="clickSearch" icon="el-icon-search">站内搜索</el-button>
          <el-button size="small" @click="clickLogin" icon="el-icon-user-solid">登录</el-button>
        </div>
      </div>
    </div>
    <br>
  </div>
</template>

<script>
import { getNotifyMessageCount } from '@/api/notifyMessageApi';

export default {
  name: 'TopTab',
  props: ["user"],
  methods: {
    // 首页
    clickBack(){
      this.$router.push("/main").catch((error) => {
      })
    },
    // 个人中心
    clickPersonal(){
      this.$router.push("/personal")
    },
    // 登录界面
    clickLogin(){
      this.$router.push("/login")
    },
    // 站内搜索
    clickSearch(){
      this.$router.push("/searchPost")
    },
    // 通知
    clickMessage(){
      this.$router.push("/message")
    }
  },
  data() {
    return {
      messageCnt: 0
    }
  },
  created() {
    getNotifyMessageCount(false)
    .then((res) => {
      this.messageCnt = res.result
    }).catch((error) => {
    })
  }
}
</script>