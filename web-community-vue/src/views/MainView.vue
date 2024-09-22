<template>
  <div>
    <top-tab :user="user" :show-back="false"></top-tab>
    <div>
      <br>
      <br>
      <br>
      <div style="text-align: center; font-size: 24px">
        Community
      </div>
      <br>
      <el-row>
        <el-col :span="4">&nbsp;</el-col>
        <el-col :span="10">
          <div>
            <el-button icon="el-icon-food" size="large" @click="clickSection(1)">闲聊</el-button>
            <el-button icon="el-icon-edit" size="large" @click="clickSection(2)">技术交流</el-button>
          </div>
        </el-col>
        <el-col :span="5">
          <br>
          <div style="text-align: center; font-size: 18px">
            活跃分数排行
          </div>
          <el-table :data="scores" :show-header="false">
            <el-table-column
              type="index"
              width="70">
            </el-table-column>
            <el-table-column
              prop="frontName"
              label="名字"
              width="120">
              <template slot-scope="scope">
                <a :href="scope.row.userUrl">{{ scope.row.frontName }}</a>
              </template>
            </el-table-column>
            <el-table-column
              prop="score"
              label="活跃分数"
              width="120">
            </el-table-column>
          </el-table>
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
import { getScores } from '@/api/scoreApi';
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
      sectionUrl: [],
      scores: []
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
    getScores(10)
    .then((res) => {
      this.scores = res.result
      for (var i = 0; i < this.scores.length; i++){ 
        this.scores[i].userUrl = 'user?id=' + this.scores[i].userId
      }
    }).catch((error) => {
    })
  }
}
</script>

