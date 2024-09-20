<template>
  <div>
    <top-tab :user="user" :show-message="false"></top-tab>
    <div>
      <br>
      <el-row>
        <el-col :span="1">&nbsp;</el-col>
        <el-col :span="22">
          <el-table :data="messages" style="width: 100%" :show-header="false">
            <el-table-column
              prop="content"
              label="内容">
              <template slot-scope="scope">
                <div v-html="scope.row.content"></div>
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
  </div>
</template>

<script>
import { getCurrentUser, logout } from '@/api/userApi'
import { getNotifyMessageCount, getNotifyMessageByPage } from '@/api/notifyMessageApi';
import TopTab from '@/components/TopTab.vue';

export default {
  name: 'MessageView',
  components:{
    TopTab
  },
  methods: {
    // 翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getNotifyMessageByPage(this.pageSize, this.pageIndex)
      .then((res) => {
        this.messages = res.result
        this.messages = this.processMessage(this.messages)
      }).catch((error) => {
      })
    },
    // 生成消息内容
    processMessage(messages){
      for (var i = 0; i < messages.length; i++){ 
        if(messages[i].type == 1){ // 关注
          messages[i].content = '您被用户<a href="user?id=' + messages[i].fromUserId + '">' + messages[i].fromUserName + '</a>关注了。';
        }else if(messages[i].type == 2){ // 喜欢帖子
          messages[i].content = '您的帖子<a href="post?id=' + messages[i].postId + '">' + messages[i].postTitle + '</a>被用户<a href="user?id=' + messages[i].fromUserId + '">' + messages[i].fromUserName + '</a>点赞了。'
        }else if(messages[i].type == 3){ // 评论
          messages[i].content = '您的帖子<a href="post?id=' + messages[i].postId + '">' + messages[i].postTitle + '</a>被用户<a href="user?id=' + messages[i].fromUserId + '">' + messages[i].fromUserName + '</a>评论了：' + messages[i].commentContent
        }else if(messages[i].type == 4){ //封禁
          messages[i].content = '您已被封禁。'
        }else if(messages[i].type == 5){ //解封
          messages[i].content = '您已被解除封禁。'
        }else if(messages[i].type == 6){ // 喜欢评论
          messages[i].content = '您在帖子<a href="post?id=' + messages[i].postId + '">' + messages[i].postTitle + '</a>下的评论“' + messages[i].commentContent + '”被用户<a href="user?id=' + messages[i].fromUserId + '">' + messages[i].fromUserName + '</a>点赞了。'
        }else if(messages[i].type == 7){ // 删除帖子
          messages[i].content = '您的帖子' + messages[i].postTitle + '被删除了。'
        }else if(messages[i].type == 8){ // 删除评论
          messages[i].content = '您在帖子<a href="post?id=' + messages[i].postId + '">' + messages[i].postTitle + '</a>下的评论“' + messages[i].commentContent + '”被删除了。'
        }
      }
      return messages
    }
  },
  data() {
    return {
      user: {},
      pageIndex: 1,
      pageSize: 12,
      total: 0,
      messages: [],
    }
  },
  created() {
    // 获取当前用户
    getCurrentUser()
    .then((res) => {
      this.user = res.result
      // 获取通知数量
      getNotifyMessageCount(true)
      .then((res) => {
        this.total = res.result
      }).catch((error) => {
      })
      // 获取通知
      getNotifyMessageByPage(this.pageSize, this.pageIndex)
      .then((res) => {
        this.messages = res.result
        this.messages = this.processMessage(this.messages)
      }).catch((error) => {
      })
    }).catch((error) => {
    })
  }
}
</script>