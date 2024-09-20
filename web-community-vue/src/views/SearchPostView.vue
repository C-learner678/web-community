<template>
  <div>
    <top-tab :user="user" :show-search="false"></top-tab>
    <el-row>
      <el-col :span="1">&nbsp;</el-col>
      <el-col :span="20">
        <div>
          <div>
            <el-row>
              <el-col :span="4">
                <el-select v-model="sectionId" placeholder="全部板块" clearable>
                  <el-option
                    v-for="section in sections"
                    :key="section.id"
                    :label="section.name"
                    :value="section.id">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="16">
                <el-input v-model="text" placeholder="帖子内容"></el-input>
              </el-col>
              <el-col :span="4">
                &nbsp;&nbsp;&nbsp;<el-button type="primary" @click="clickSearch">搜索</el-button>
              </el-col>
            </el-row>
          </div>
          <br>
          <el-table :data="posts" style="width: 100%">
            <el-table-column
              prop="title"
              label="帖子标题"
              width="360">
              <template slot-scope="scope">
                <a :href="scope.row.postUrl">{{ scope.row.title }}</a>
              </template>
            </el-table-column>
            <el-table-column
              prop="sectionName"
              label="板块"
              width="180">
              <template slot-scope="scope">
                <a :href="scope.row.sectionUrl">{{ scope.row.sectionName }}</a>
              </template>
            </el-table-column>
            <el-table-column
              prop="userFrontName"
              label="作者"
              width="180">
              <template slot-scope="scope">
                <a :href="scope.row.authorUrl">{{ scope.row.userFrontName }}</a>
              </template>
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="发帖时间"
              width="240">
            </el-table-column>
            <el-table-column
              prop="updateTime"
              label="更新时间"
              width="240">
            </el-table-column>
            <el-table-column
              prop="op"
              label="操作"
              width="180"
              v-if="user != null && user.role=='admin'">
              <template slot-scope="scope">
                <el-button @click="clickDeletePost(scope.row)" type="text" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            layout="prev, pager, next"
            :total="total"
            :page-size='pageSize'
            @current-change='pageChange'>
          </el-pagination>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCurrentUser } from '@/api/userApi'
import { getSections } from '@/api/sectionApi'
import { getSearchPostByPage, getSearchPostCount } from '@/api/postApi'
import TopTab from "@/components/TopTab.vue"

export default {
  name: 'SearchPostView',
  components: {
    TopTab
  },
  computed: {
    routerKey() {
      return this.$route.path + Math.random()
    }
	},
  methods: {
    // 翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getSearchPostByPage(this.text, this.sectionId, this.pageSize, this.pageIndex)
      .then((res) => {
        this.posts = res.result
        for (var i = 0; i < this.posts.length; i++){ 
          this.posts[i].postUrl = 'post?id=' + this.posts[i].id
          this.posts[i].authorUrl = 'user?id=' + this.posts[i].userId
          this.posts[i].sectionUrl = 'section?id=' + this.posts[i].sectionId
          this.posts[i].createTime = new Date(this.posts[i].createTime).toLocaleString()
          this.posts[i].updateTime = new Date(this.posts[i].updateTime).toLocaleString()
        }
      }).catch((error) => {
      })
    },
    // 点击搜索
    clickSearch(){
      if(this.text && this.text != ''){
        getSearchPostCount(this.text, this.sectionId)
        .then((res) => {
          this.total = res.result;
        }).catch((error) => {
        })
        getSearchPostByPage(this.text, this.sectionId, this.pageSize, this.pageIndex)
        .then((res) => {
          this.posts = res.result
          for (var i = 0; i < this.posts.length; i++){ 
            this.posts[i].postUrl = 'post?id=' + this.posts[i].id
            this.posts[i].authorUrl = 'user?id=' + this.posts[i].userId
            this.posts[i].sectionUrl = 'section?id=' + this.posts[i].sectionId
            this.posts[i].createTime = new Date(this.posts[i].createTime).toLocaleString()
            this.posts[i].updateTime = new Date(this.posts[i].updateTime).toLocaleString()
          }
        }).catch((error) => {
        })
      }
    }
  },
  data() {
    return {
      user: {},
      sections: [],
      text: '',
      sectionId: null,
      posts: [],
      pageSize: 50,
      pageIndex: 1,
      total: 0,
    }
  },
  created() {
    // 获取当前用户
    getCurrentUser()
    .then((res) => {
      this.user = res.result
    }).catch((error) => {
    })
    getSections()
    .then((res) => {
      this.sections = res.result
    }).catch((error) => {
    })
  }
}
</script>
