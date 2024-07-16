<template>
  <div>
    <top-tab :user="user"></top-tab>
    <el-row>
      <el-col :span="1">&nbsp;</el-col>
      <el-col :span="20">
        <div>
          <div style="font-size: 24px;">
            {{ post.title }}
          </div>
          <el-image style="width: 80px; height: 80px" :src="authorImageUrl">
            <original-avatar slot="error"></original-avatar>
          </el-image><br>
          作者：<a :href="authorUrl">{{ author.frontName }}</a>&nbsp;&nbsp;&nbsp;
          所属板块：<a :href="sectionUrl">{{ section.name }}</a>&nbsp;&nbsp;&nbsp;
          <el-button type="text" @click="clickDeletePost"
            v-if="user.role == 'admin' || user.userId == post.userId">删除</el-button><br>
          <v-md-preview :text="post.content"></v-md-preview>
        </div>
        <div>
          <br>评论：<br>
          <el-table :data="comments" style="width: 100%" :show-header="false" v-if="comments.length > 0">
            <el-table-column
              prop="avatar"
              label="头像"
              width="60">
              <template slot-scope="scope">
                <el-image style="width: 40px; height: 40px" :src="scope.row.imageUrl">
                  <original-avatar slot="error"></original-avatar>
                </el-image>
              </template>
            </el-table-column>
            <el-table-column
              prop="frontName"
              label="用户名"
              width="120">
              <template slot-scope="scope">
                <a :href="scope.row.authorUrl">{{ scope.row.frontName }}</a>
              </template>
            </el-table-column>
            <el-table-column
              prop="content"
              label="内容"
              width="720">
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="评论时间"
              width="180">
            </el-table-column>
            <el-table-column
              prop="op"
              label="操作"
              width="180"
              v-if="user.role=='admin'">
              <template slot-scope="scope">
                <el-button @click="clickDeleteComment(scope.row)" type="text" size="small">删除</el-button>
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
          <br>我要评论：<br>
          <el-form ref="addCommentForm" :model="addCommentForm" label-width="0px">
            <el-form-item label="">
              <el-input v-model="addCommentForm.content" type="textarea"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="clickAddCommentButtion">发布</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import BASE_URL from '@/request/baseUrl'
import { getPost, deletePost, adminDeletePost } from '@/api/postApi'
import { getCommentByPage, getCommentCount, addComment, deleteComment } from '@/api/commentApi'
import { getCurrentUser, getUserInfo } from '@/api/userApi'
import { getSection } from '@/api/sectionApi'
import TopTab from '@/components/TopTab.vue'
import OriginalAvatar from '@/components/OriginalAvatar.vue'

export default {
  name: 'PostView',
  components:{
    TopTab,
    OriginalAvatar
  },
  methods: {
    // 翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getCommentByPage(this.postId, null, this.pageSize, this.pageIndex)
      .then((res) => {
        this.comments = res.result
        for (var i = 0; i < this.comments.length; i++){ 
          this.comments[i].authorUrl = 'user?id=' + this.comments[i].userId
          this.comments[i].createTime = new Date(this.comments[i].createTime).toLocaleString()
          if(this.comments[i].avatar != null){
            this.comments[i].imageUrl = BASE_URL + 'image/getImage/' + this.comments[i].avatar
          }
        }
      }).catch((error) => {
      })
    },
    clickAddCommentButtion(){
      if(this.addCommentForm.content == ''){
        this.$message.error('请输入内容！')
        return
      }
      addComment(this.postId, this.addCommentForm.content)
      .then((res) => {
        this.$message.success('评论成功')
        this.addCommentForm.content = ''
        // 重新加载
        getCommentCount(this.postId, null)
        .then((res) => {
          this.total = res.result
        }).catch((error) => {
        })
        this.pageChange(1)
      }).catch((error) => {
      })
    },
    // 删除帖子
    clickDeletePost(){
      this.$confirm('确定要删除帖子吗？', '', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if(this.user.role=='admin'){
          adminDeletePost(this.postId)
          .then((res) => {
            this.$message.success('删除成功!')
            this.$router.push('section?id=' + this.post.sectionId)
          }).catch((error) => {
          })
        }else{
          deletePost(this.postId)
          .then((res) => {
            this.$message.success('删除成功!')
            this.$router.push('section?id=' + this.post.sectionId)
          }).catch((error) => {
          })
        }
      }).catch(() => { 
      });
    },
    // 删除评论
    clickDeleteComment(row){
      this.$confirm('确定要删除评论吗？', '', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteComment(row.id)
        .then((res) => {
          this.$message.success('删除成功!')
          this.pageChange(this.pageIndex)
        }).catch((error) => {
        })
      }).catch(() => { 
      });
    },
  },
  data() {
    return {
      user: {},
      postId: 0,
      post: {},
      author: {},
      authorUrl: '',
      section: {},
      sectionUrl: '',
      comments: [],
      pageSize: 30,
      pageIndex: 1,
      total: 0,
      addCommentForm: {
        content: ''
      },
      authorImageUrl: '',
    }
  },
  created() {
    this.postId = Number(this.$route.query.id)
    // 获取当前用户
    getCurrentUser()
    .then((res) => {
      this.user = res.result
    }).catch((error) => {
    })
    // 获取帖子内容
    getPost(this.postId)
    .then((res) => {
      this.post = res.result
      // 获取作者
      getUserInfo(this.post.userId)
      .then((res) => {
        this.author = res.result
        this.authorUrl = 'user?id=' + this.post.userId
        if(this.author.avatar != null){
          this.authorImageUrl = BASE_URL + 'image/getImage/' + this.author.avatar
        }
      }).catch((error) => {
      })
      // 获取版块
      getSection(this.post.sectionId)
      .then((res) => {
        this.section = res.result
        this.sectionUrl = 'section?id=' + this.post.sectionId
      }).catch((error) => {
      })
    }).catch((error) => {
    })
    // 获取评论
    getCommentCount(this.postId, null)
    .then((res) => {
      this.total = res.result
    }).catch((error) => {
    })
    getCommentByPage(this.postId, null, this.pageSize, this.pageIndex)
    .then((res) => {
      this.comments = res.result
      for (var i = 0; i < this.comments.length; i++){ 
        this.comments[i].authorUrl = 'user?id=' + this.comments[i].userId
        this.comments[i].createTime = new Date(this.comments[i].createTime).toLocaleString()
        if(this.comments[i].avatar != null){
          this.comments[i].imageUrl = BASE_URL + 'image/getImage/' + this.comments[i].avatar
        }
      }
    }).catch((error) => {
    })
  }
}
</script>

