<template>
  <div>
    <el-table :data="userList" style="width: 100%">
      <el-table-column
        prop="frontName"
        label="用户名"
        width="180">
        <template slot-scope="scope">
          <a :href="scope.row.userUrl">{{ scope.row.frontName }}</a>
        </template>
      </el-table-column>
      <el-table-column
        prop="info"
        label="简介"
        width="540">
      </el-table-column>
    </el-table>
    <el-pagination
      layout="prev, pager, next"
      :total="total"
      :page-size='pageSize'
      @current-change='pageChange'>
    </el-pagination>
  </div>
</template>

<script>
import { getFollowNum, getFollowByPage } from '@/api/userFollowApi'
import router from '@/router'

export default {
  name: 'UserFollowList',
  props: ['userId'],
  methods: {
    handleClick(row) {
      let url = "user?id=" + row.userId
      router.push(url)
    },
    // 翻页
    pageChange(pageIndex){
      this.pageIndex = pageIndex
      getFollowByPage(this.userId, this.pageSize, this.pageIndex)
      .then((res) => {
        this.userList = res.result
      }).catch((error) => {
      })
    },
  },
  data() {
    return {
      pageSize: 10,
      pageIndex: 1,
      total: 0,
      userList: []
    }
  },
  created() {
    getFollowNum(this.userId)
    .then((res) => {
      this.total = res.result
    }).catch((error) => {
    })
    getFollowByPage(this.userId, this.pageSize, this.pageIndex)
    .then((res) => {
      this.userList = res.result
      for (var i = 0; i < this.userList.length; i++){ 
        this.userList[i].userUrl = 'user?id=' + this.userList[i].userId
      }
    }).catch((error) => {
    })
  }
}
</script>
