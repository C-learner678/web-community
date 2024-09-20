<template>
  <div>
    <top-tab :user="user" :show-personal="false"></top-tab>
    <div>
      <el-row>
        <el-col :span="1">&nbsp;</el-col>
        <el-col :span="20">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeader"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <el-image style="width: 80px; height: 80px" :src="imageUrl">
              <original-avatar slot="error"></original-avatar>
            </el-image>
            <div slot="tip" style="font-size: 12px;">只能上传jpg文件，且不超过2Mb</div>
          </el-upload>
          <el-descriptions title="用户信息" :column="4">
            <template slot="extra">
              <el-button type="primary" size="small" @click="clickModifyInfo">修改信息</el-button>
              <el-button type="primary" size="small" @click="clickModifyPassword">修改密码</el-button>
            </template>
            <el-descriptions-item label="用户名">{{ user.name }}</el-descriptions-item>
            <el-descriptions-item label="展示名称">{{ user.frontName }}</el-descriptions-item>
            <el-descriptions-item label="我的关注">
              <el-button type="text" @click="clickUserFollow">{{ followNum }}</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="我的粉丝">
              <el-button type="text" @click="clickUserFollower">{{ followerNum }}</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="个人简介">{{ user.info }}</el-descriptions-item>
          </el-descriptions>
          <br>
          <div style="font-weight: bold">
           历史发帖
          </div>
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
              prop="createTime"
              label="发帖时间"
              width="180">
            </el-table-column>
            <el-table-column
              prop="updateTime"
              label="更新时间"
              width="180">
            </el-table-column>
            <el-table-column
              prop="readCnt"
              label="已读"
              width="60">
            </el-table-column>
            <el-table-column
              prop="likeCnt"
              label="喜欢"
              width="60">
            </el-table-column>
            <el-table-column
              prop="op"
              label="操作"
              width="200">
              <template slot-scope="scope">
                <el-button @click="clickModifyPost(scope.row)" type="text" size="small">修改</el-button>
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
          <br>
          <div style="font-weight: bold">
            收藏帖子
          </div>
          <el-table :data="collectPosts" style="width: 100%">
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
              prop="createTime"
              label="发帖时间"
              width="180">
            </el-table-column>
            <el-table-column
              prop="updateTime"
              label="更新时间"
              width="180">
            </el-table-column>
            <el-table-column
              prop="readCnt"
              label="已读"
              width="60">
            </el-table-column>
            <el-table-column
              prop="likeCnt"
              label="喜欢"
              width="60">
            </el-table-column>
            <el-table-column
              prop="op"
              label="操作"
              width="200">
              <template slot-scope="scope">
                <el-button @click="clickCancelCollectPost(scope.row)" type="text" size="small">取消收藏</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            layout="prev, pager, next"
            :total="total"
            :page-size='pageSize'
            @current-change='collectPageChange'>
          </el-pagination>
        </el-col>
      </el-row>
    </div>
    <div>
      <el-dialog
        title="我的关注"
        :visible.sync="showUserFollow">
        <user-follow-list :userId="user.userId"></user-follow-list>
      </el-dialog>
      <el-dialog
        title="我的粉丝"
        :visible.sync="showUserFollower">
        <user-follower-list :userId="user.userId"></user-follower-list>
      </el-dialog>
      <el-dialog
        title="修改信息"
        :visible.sync="showModifyUserInfo">
        <modify-user-info :userId="user.userId"></modify-user-info>
      </el-dialog>
      <el-dialog
        title="修改密码"
        :visible.sync="showModifyPassword">
        <modify-password :userId="user.userId"></modify-password>
      </el-dialog>
      <el-dialog
        title="修改帖子"
        :visible.sync="showModifyPost"
        width="90%">
          <el-form :label-position="'left'" label-width="80px" :model="ModifyPostForm" ref="ModifyPostForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="ModifyPostForm.title"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <v-md-editor 
              v-model="ModifyPostForm.content" 
              height="400px" 
              :disabled-menus="[]"
              @upload-image="handleUploadImage">
              </v-md-editor>
          </el-form-item>
          <el-form-item>
            <el-col :span="20">
              &nbsp;
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="submitModifyForm()">确认</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import BASE_URL from '@/request/baseUrl'
import axios from 'axios'
import { getCurrentUser, modifyAvatar } from '@/api/userApi'
import { getFollowNum, getFollowerNum } from '@/api/userFollowApi'
import { getPostByPage, getPostCount, getPost, modifyPost, deletePost } from '@/api/postApi'
import { getCollectPostByPage, modifyPostUserFoot } from '@/api/userFootApi'
import { getNotifyMessageCount } from '@/api/notifyMessageApi';
import UserFollowList from '@/components/UserFollowList.vue'
import UserFollowerList from '@/components/UserFollowerList.vue'
import ModifyUserInfo from '@/components/ModifyUserInfo.vue'
import ModifyPassword from '@/components/ModifyPassword.vue'
import OriginalAvatar from '@/components/OriginalAvatar.vue'
import TopTab from '@/components/TopTab.vue'

export default {
  name: 'PersonalView',
  components: {
    UserFollowList,
    UserFollowerList,
    ModifyUserInfo,
    ModifyPassword,
    OriginalAvatar,
    TopTab
  },
  methods: {
    // 回到首页
    clickBack(){
      this.$router.push("/main")
    },
    // 查看通知
    clickMessage(){
      this.$router.push("/message")
    },
    // 帖子搜索
    clickSearch(){
      this.$router.push("/searchPost")
    },
    // 看关注用户列表
    clickUserFollow() {
      this.showUserFollow = true
    },
    // 看粉丝用户列表
    clickUserFollower() {
      this.showUserFollower = true
    },
    // 修改用户信息
    clickModifyInfo() {
      this.showModifyUserInfo = true
    },
    // 修改密码
    clickModifyPassword() {
      this.showModifyPassword = true
    },
    // 帖子翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getPostByPage(this.user.userId, null, false, this.pageSize, this.pageIndex)
      .then((res) => {
        this.posts = res.result
        for (var i = 0; i < this.posts.length; i++){ 
          this.posts[i].postUrl = 'post?id=' + this.posts[i].id
          this.posts[i].sectionUrl = 'section?id=' + this.posts[i].sectionId
          this.posts[i].createTime = new Date(this.posts[i].createTime).toLocaleString()
          this.posts[i].updateTime = new Date(this.posts[i].updateTime).toLocaleString()
        }
      }).catch((error) => {
      })
    },
    // 修改
    clickModifyPost(row){
      this.ModifyPostForm.postId = row.id
      getPost(this.ModifyPostForm.postId)
      .then((res) => {
        this.ModifyPostForm.title = res.result.title
        this.ModifyPostForm.content = res.result.content
      }).catch((error) => {
      })
      this.showModifyPost = true
    },
    submitModifyForm(){
      modifyPost(this.ModifyPostForm.postId, this.ModifyPostForm.title, this.ModifyPostForm.content)
      .then((res) => {
        this.$message.success('修改帖子成功')
      }).catch((error) => {
      })
    },
    // 删除
    clickDeletePost(row){
      this.$confirm('确定要删除帖子吗？', '', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePost(row.id)
        .then((res) => {
          this.$message.success('删除成功!')
          this.pageChange(this.pageIndex)
        }).catch((error) => {
        })
      }).catch(() => { 
      })
    },
    // 传头像
    handleAvatarSuccess(res, file) {
      if(res.status.code == 0){
        this.imageUrl = BASE_URL + 'image/getImage/' + res.result
        modifyAvatar(res.result)
        .then((res) => {
          this.$message.success('上传头像成功')
        }).catch((error) => {
        })
      }else{
        this.$message.error(res.status.msg)
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      const token = sessionStorage.getItem("userToken") ? sessionStorage.getItem("userToken") : null
      if (token) {
        this.uploadHeader['userToken'] = token
      }
      return isJPG && isLt2M;
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
    // 帖子翻页
    collectPageChange(collectPageIndex){
      this.collectPageIndex = collectPageIndex
      getCollectPostByPage(this.pageSize, this.collectPageIndex)
      .then((res) => {
        this.collectPosts = res.result
        for (var i = 0; i < this.collectPosts.length; i++){ 
          this.collectPosts[i].postUrl = 'post?id=' + this.collectPosts[i].id
          this.collectPosts[i].sectionUrl = 'section?id=' + this.collectPosts[i].sectionId
          this.collectPosts[i].createTime = new Date(this.collectPosts[i].createTime).toLocaleString()
          this.collectPosts[i].updateTime = new Date(this.collectPosts[i].updateTime).toLocaleString()
        }
      }).catch((error) => {
      })
    },
    // 取消收藏
    clickCancelCollectPost(row){
      this.$confirm('确定要取消收藏吗？', '', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        modifyPostUserFoot(row.id, 2, false)
        .then((res) => {
          this.collectPageChange(this.collectPageIndex)
        }).catch((error) => {
        })
      }).catch(() => { 
      })
    },
  },
  data() {
    return {
      user: {},
      followNum: 0,
      followerNum: 0,
      showUserFollow: false,
      showUserFollower: false,
      showModifyUserInfo: false,
      showModifyPassword: false,
      showModifyPost: false,
      posts: [],
      pageSize: 30,
      pageIndex: 1,
      total: 0,
      ModifyPostForm: {
        postId: 0,
        title: '',
        content: ''
      },
      imageUrl: '',
      uploadUrl: BASE_URL + 'image/uploadImage',
      uploadHeader: {},
      collectPosts: [],
      collectPageIndex: 1,
    }
  },
  created() {
    getCurrentUser()
    .then((res) => {
      this.user = res.result
      if(this.user.avatar != null){
        this.imageUrl = BASE_URL + 'image/getImage/' + this.user.avatar
      }
      getFollowNum(this.user.userId)
      .then((res) => {
        this.followNum = res.result
      }).catch((error) => {
      })
      getFollowerNum(this.user.userId)
      .then((res) => {
        this.followerNum = res.result
      }).catch((error) => {
      })
      getPostCount(this.user.userId, null, false)
      .then((res) => {
        this.total = res.result
      }).catch((error) => {
      })
      getPostByPage(this.user.userId, null, false, this.pageSize, this.pageIndex)
      .then((res) => {
        this.posts = res.result
        for (var i = 0; i < this.posts.length; i++){ 
          this.posts[i].postUrl = 'post?id=' + this.posts[i].id
          this.posts[i].sectionUrl = 'section?id=' + this.posts[i].sectionId
          this.posts[i].createTime = new Date(this.posts[i].createTime).toLocaleString()
          this.posts[i].updateTime = new Date(this.posts[i].updateTime).toLocaleString()
        }
      }).catch((error) => {
      })
      getCollectPostByPage(this.pageSize, this.collectPageIndex)
      .then((res) => {
        this.collectPosts = res.result
        for (var i = 0; i < this.collectPosts.length; i++){ 
          this.collectPosts[i].postUrl = 'post?id=' + this.collectPosts[i].id
          this.collectPosts[i].sectionUrl = 'section?id=' + this.collectPosts[i].sectionId
          this.collectPosts[i].createTime = new Date(this.collectPosts[i].createTime).toLocaleString()
          this.collectPosts[i].updateTime = new Date(this.collectPosts[i].updateTime).toLocaleString()
        }
      }).catch((error) => {
      })
      getNotifyMessageCount(false)
      .then((res) => {
        this.messageCnt = res.result
      }).catch((error) => {
      })
    }).catch((error) => {
      sessionStorage.clear()
      this.$router.push("/main")
    })
  }
}
</script>
