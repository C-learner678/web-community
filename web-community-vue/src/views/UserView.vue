<template>
  <div>
    <top-tab :user="currentUser"></top-tab>
    <div>
      <el-row>
        <el-col :span="1">&nbsp;</el-col>
        <el-col :span="20">
          <el-image style="width: 80px; height: 80px" :src="imageUrl">
            <original-avatar slot="error"></original-avatar>
          </el-image>
          <el-descriptions title="" :column="4">
            <el-descriptions-item label="名称">{{ user.frontName }}</el-descriptions-item>
            <el-descriptions-item label="关注数">
              <el-button type="text" @click="clickUserFollow">{{ followNum }}</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="粉丝数">
              <el-button type="text" @click="clickUserFollower">{{ followerNum }}</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <div v-if="isCurrentUserFollow==false">
                未关注
              </div>
              <div v-if="isCurrentUserFollow==true && isCurrentUserFollower==false">
                已关注
              </div>
              <div v-if="isCurrentUserFollow==true && isCurrentUserFollower==true">
                已互粉
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="个人简介">{{ user.info }}</el-descriptions-item>
          </el-descriptions>
          <el-button v-if="isCurrentUserFollow==false" size="small" type="primary" @click="clickAddFollow">
            关注
          </el-button>
          <el-button v-if="isCurrentUserFollow==true" size="small" type="primary" @click="clickRemoveFollow">
            取消关注
          </el-button>&nbsp;&nbsp;
          <el-switch v-if="currentUser != null && currentUser.role=='admin'" v-model="banned" active-text="封禁" @change="clickBan">
          </el-switch>
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
              v-if="currentUser != null && currentUser.role=='admin'">
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
        </el-col>
      </el-row>
    </div>
    <div>
      <el-dialog
        title="关注"
        :visible.sync="showUserFollow">
        <user-follow-list :userId="userId"></user-follow-list>
      </el-dialog>
      <el-dialog
        title="粉丝"
        :visible.sync="showUserFollower">
        <user-follower-list :userId="userId"></user-follower-list>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import BASE_URL from '@/request/baseUrl'
import { getCurrentUser, getUserInfo, modifyUserStatus, getUserBanned } from '@/api/userApi'
import { getPostByPage, getPostCount, adminDeletePost } from '@/api/postApi'
import { getFollowNum, getFollowerNum, isFollow, addFollow, removeFollow } from '@/api/userFollowApi'
import UserFollowList from '@/components/UserFollowList.vue'
import UserFollowerList from '@/components/UserFollowerList.vue'
import OriginalAvatar from '@/components/OriginalAvatar.vue'
import TopTab from '@/components/TopTab.vue'

export default {
  name: 'UserView',
  components: {
    UserFollowList,
    UserFollowerList,
    OriginalAvatar,
    TopTab
  },
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
    // 翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getPostByPage(this.userId, null, null, this.pageSize, this.pageIndex)
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
    // 看关注用户列表
    clickUserFollow() {
      this.showUserFollow = true
    },
    // 看粉丝用户列表
    clickUserFollower() {
      this.showUserFollower = true
    },
    // 加关注
    clickAddFollow() {
      addFollow(this.userId)
      .then((res) => {
        this.isCurrentUserFollow = true
      }).catch((error) => {
      })
    },
    // 取消关注
    clickRemoveFollow() {
      removeFollow(this.userId)
      .then((res) => {
        this.isCurrentUserFollow = false
      }).catch((error) => {
      })
    },
    // 封禁
    clickBan(status) {
      modifyUserStatus(this.userId, status)
      .then((res) => {
      }).catch((error) => {
      })
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
    }
  },
  data() {
    return {
      currentUser: {},
      userId: 0,
      user: {},
      followNum: 0,
      followerNum: 0,
      showUserFollow: false,
      showUserFollower: false,
      posts: [],
      pageSize: 30,
      pageIndex: 1,
      total: 0,
      isCurrentUserFollow: false, // 当前用户已关注该用户
      isCurrentUserFollower: false, // 当前用户被该用户关注
      imageUrl: '',
      banned: false
    }
  },
  created() {
    this.userId = Number(this.$route.query.id)
    getCurrentUser()
    .then((res) => {
      this.currentUser = res.result
      if(this.currentUser.userId == this.userId){
        this.$router.push('personal')
      }
      // 当前用户是否已经被该用户关注
      isFollow(this.currentUser.userId, this.userId)
      .then((res) => {
        this.isCurrentUserFollower = res.result
      }).catch((error) => {
      })
      // 当前用户是否关注了该用户
      isFollow(this.userId, this.currentUser.userId)
      .then((res) => {
        this.isCurrentUserFollow = res.result
      }).catch((error) => {
      })
    }).catch((error) => {
    })
    getUserInfo(this.userId)
    .then((res) => {
      this.user = res.result
      if(this.user.avatar != null){
        this.imageUrl = BASE_URL + 'image/getImage/' + this.user.avatar
      }
    }).catch((error) => {
    })
    getFollowNum(this.userId)
    .then((res) => {
      this.followNum = res.result
    }).catch((error) => {
    })
    getFollowerNum(this.userId)
    .then((res) => {
      this.followerNum = res.result
    }).catch((error) => {
    })
    getPostCount(this.userId, null, false)
    .then((res) => {
      this.total = res.result
    }).catch((error) => {
    })
    getPostByPage(this.userId, null, null, this.pageSize, this.pageIndex)
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
    getUserBanned(this.userId)
    .then((res) => {
      this.banned = res.result
    }).catch((error) => {
    })
  }
}
</script>

