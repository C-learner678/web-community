<template>
  <div>
    <top-tab :user="user"></top-tab>
    <el-row>
      <el-col :span="1">&nbsp;</el-col>
      <el-col :span="20">
        <div>
          <div style="font-weight: bold; font-size: 24px;">
           {{ section.name }}
          </div>
          {{ section.info }}
        </div>
        <div>
          <br>
          <el-table :data="topPosts" style="width: 100%" v-if="topPosts.length > 0">
            <el-table-column
              prop="title"
              label="帖子标题"
              width="360">
              <template slot-scope="scope">
                <font style="color:red">[置顶]</font>
                <a :href="scope.row.postUrl">{{ scope.row.title }}</a>
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
          </el-table>
          <el-table :data="posts" style="width: 100%" :show-header="topPosts.length == 0">
            <el-table-column
              prop="title"
              label="帖子标题"
              width="360">
              <template slot-scope="scope">
                <a :href="scope.row.postUrl">{{ scope.row.title }}</a>
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
              v-if="user.role=='admin'">
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
        <div>
          <br>
          <div style="font-size: 20px;">
            发帖
          </div>
          <br>
          <el-form ref="addPostForm" :model="addPostForm" label-width="40px">
            <el-form-item label="标题">
              <el-input v-model="addPostForm.title"></el-input>
            </el-form-item>
            <el-form-item label="内容">
              <v-md-editor 
              v-model="addPostForm.content" 
              height="400px" 
              @save="savePost"
              :disabled-menus="[]"
              @upload-image="handleUploadImage">
              </v-md-editor>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="clickAddPostButtion">发布</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Cookies from "js-cookie"
import BASE_URL from "@/request/baseUrl"
import axios from 'axios'
import { getCurrentUser } from '@/api/userApi'
import { getSection } from '@/api/sectionApi'
import { getPostByPage, getPostCount, addPost, adminDeletePost } from '@/api/postApi'
import TopTab from "@/components/TopTab.vue"

export default {
  name: 'SectionView',
  components: {
    TopTab
  },
  methods: {
    // 翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getPostByPage(null, this.sectionId, false, this.pageSize, this.pageIndex)
      .then((res) => {
        this.posts = res.result
        for (var i = 0; i < this.posts.length; i++){ 
          this.posts[i].postUrl = 'post?id=' + this.posts[i].id
          this.posts[i].authorUrl = 'user?id=' + this.posts[i].userId
          this.posts[i].createTime = new Date(this.posts[i].createTime).toLocaleString()
          this.posts[i].updateTime = new Date(this.posts[i].updateTime).toLocaleString()
        }
      }).catch((error) => {
      })
    },
    //发帖
    clickAddPostButtion(){
      if(this.addPostForm.title == ''){
        this.$message.error('请输入标题！')
        return
      }
      addPost(this.sectionId, this.addPostForm.title, this.addPostForm.content)
      .then((res) => {
        this.$message.success('发帖成功')
        this.addPostForm.title = ''
        this.addPostForm.content = ''
        Cookies.set("title", '', { expires: 30 })
        Cookies.set("content", '', { expires: 30 })
        // 重新加载
        getPostCount(null, this.sectionId, false)
        .then((res) => {
          this.total = res.result
        }).catch((error) => {
        })
        this.pageChange(1)
      }).catch((error) => {
      })
    },
    //暂存
    savePost(){
      Cookies.set("title", this.addPostForm.title, { expires: 30 })
      Cookies.set("content", this.addPostForm.content, { expires: 30 })
      this.$message.success('已暂存到本地')
    },
    //插入图片
    handleUploadImage(event, insertImage, files) {
      const isJPG = files[0].type === 'image/jpeg';
      const isLt2M = files[0].size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
        return false
      }
      const token = sessionStorage.getItem("userToken") ? sessionStorage.getItem("userToken") : null
      const formData = new FormData()
      formData.append('file', files[0])
      axios.post(BASE_URL + 'image/uploadImage', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'userToken': token
        }
      }).then(res => {
        if(res.data.status.code == 0){
          insertImage({
            url: BASE_URL + 'image/getImage/' + res.data.result,
            desc: '',
            width: 'auto',
            height: 'auto',
          })
        }else{
          this.$message.error(res.data.status.msg)
        }
      })
      .catch(error => {
      });
    },
    // 管理员删除帖子
    clickDeletePost(row){
      this.$confirm('确定要删除帖子吗？', '', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adminDeletePost(row.id)
        .then((res) => {
          this.$message.success('删除成功!')
          this.pageChange(this.pageIndex)
        }).catch((error) => {
        })
      }).catch(() => { 
      })
    },
  },
  data() {
    return {
      user: {},
      sectionId: 0,
      section: null,
      topPosts: [],
      posts: [],
      pageSize: 30,
      pageIndex: 1,
      total: 0,
      addPostForm: {
        title: '',
        content: ''
      }
    }
  },
  created() {
    this.sectionId = Number(this.$route.query.id)
    // 获取当前用户
    getCurrentUser()
    .then((res) => {
      this.user = res.result
    }).catch((error) => {
    })
    //获取板块和帖子
    getSection(this.sectionId)
    .then((res) => {
      this.section = res.result
    }).catch((error) => {
    })
    getPostByPage(null, this.sectionId, true, 10, 1)
    .then((res) => {
      this.topPosts = res.result
      for (var i = 0; i < this.topPosts.length; i++){ 
        this.topPosts[i].postUrl = 'post?id=' + this.topPosts[i].id
        this.topPosts[i].authorUrl = 'user?id=' + this.topPosts[i].userId
        this.topPosts[i].createTime = new Date(this.topPosts[i].createTime).toLocaleString()
        this.topPosts[i].updateTime = new Date(this.topPosts[i].updateTime).toLocaleString()
      }
    }).catch((error) => {
    })
    getPostCount(null, this.sectionId, false)
    .then((res) => {
      this.total = res.result
    }).catch((error) => {
    })
    getPostByPage(null, this.sectionId, false, this.pageSize, this.pageIndex)
    .then((res) => {
      this.posts = res.result
      for (var i = 0; i < this.posts.length; i++){ 
        this.posts[i].postUrl = 'post?id=' + this.posts[i].id
        this.posts[i].authorUrl = 'user?id=' + this.posts[i].userId
        this.posts[i].createTime = new Date(this.posts[i].createTime).toLocaleString()
        this.posts[i].updateTime = new Date(this.posts[i].updateTime).toLocaleString()
      }
    }).catch((error) => {
    })
    this.addPostForm.content = Cookies.get("content")
    this.addPostForm.title = Cookies.get("title")
  }
}
</script>

