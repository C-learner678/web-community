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
            <el-row>
              <el-col :span="20">
                <el-input v-model="searchText" placeholder="搜索帖子"></el-input>
              </el-col>
              <el-col :span="4">
                &nbsp;&nbsp;&nbsp;<el-button type="primary">搜索</el-button>
              </el-col>
            </el-row>
          </div>
          <br>
          <div>
            <el-button icon="el-icon-food" size="large" @click="clickSection(1)">闲聊</el-button>
            <el-button icon="el-icon-edit" size="large" @click="clickSection(2)">技术交流</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getCurrentUser } from '@/api/userApi'
import TopTab from "@/components/TopTab.vue"

export default {
  name: 'MainView',
  components: {
    TopTab
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

