<template>
  <div>
    <div style="text-align:left;">
      <el-button size="small" @click="clickBack">回到首页</el-button>
      <div style="float:right;">
        <div v-if="user != null && user.role != null">
          <el-button size="small" @click="clickSearch" icon="el-icon-search">站内搜索</el-button>
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
export default {
  name: 'TopTab',
  props: ["user"],
  methods: {
    clickBack(){
      this.$router.push("/main")
    },
    clickPersonal(){
      this.$router.push("/personal")
    },
    clickLogin(){
      this.$router.push("/login")
    },
    clickSearch(){
      this.$router.push("/searchPost")
    }
  },
  data() {
    return {

    }
  },
  created() {
    
  }
}
</script>