<template>
  <div>
    <top-tab :user="user"></top-tab>
    <div>
      <el-row>
        <el-col :span="4">&nbsp;</el-col>
        <el-col :span="16">
          <br>
          <br>
          <br>
          <div style="text-align: center; font-size: 24px">
            Community
          </div>
          <br>
          <div>
            <el-button icon="el-icon-food" size="large" @click="clickSection(1)">闲聊</el-button>
            <el-button icon="el-icon-edit" size="large" @click="clickSection(2)">技术交流</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 440px;">
      <el-row>
        <el-col :span="21">
          &nbsp;
        </el-col>
        <el-col :span="3">
          <bei-an></bei-an>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getCurrentUser } from '@/api/userApi'
import TopTab from "@/components/TopTab.vue"
import BeiAn from "@/components/BeiAn.vue"

export default {
  name: 'MainView',
  components: {
    TopTab,
    BeiAn
  },
  methods: {
    clickPersonal(){
      this.$router.push("/personal")
    },
    clickLogin(){
      this.$router.push("/login")
    },
    clickSection(id){
      this.$router.push("/section?id=" + id)
    }
  },
  data() {
    return {
      user: {},
      sections: [],
      sectionUrl: []
    }
  },
  created() {
    getCurrentUser()
    .then((res) => {
      if(res.result){
        this.user = res.result
      }
    }).catch((error) => {
    })
  }
}
</script>

