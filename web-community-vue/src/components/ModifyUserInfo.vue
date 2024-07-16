<template>
  <div>
    <el-row>
      <el-form :label-position="'left'" label-width="80px" :model="ModifyInfoForm" ref="ModifyInfoForm">
        <el-form-item label="显示名" prop="frontName">
          <el-input v-model="ModifyInfoForm.frontName"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="info">
          <el-input v-model="ModifyInfoForm.info" type="textarea"></el-input>
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
    </el-row>
  </div>
</template>

<script>
import { getUserInfo, modifyUserInfo } from '@/api/userApi';

export default {
  name: 'ModifyUserInfo',
  props: ['userId'],
  methods: {
    submitModifyForm(){
      modifyUserInfo(this.ModifyInfoForm.frontName, this.ModifyInfoForm.info)
      .then((res) => {
        this.$message.success('修改个人信息成功')
        window.location.reload()
      }).catch((error) => {
      })
    }
  },
  data() {
    return {
      ModifyInfoForm: {
        frontName: '',
        info: ''
      }
    }
  },
  created() {
    getUserInfo(this.userId)
    .then((res) => {
      this.ModifyInfoForm.frontName = res.result.frontName
      this.ModifyInfoForm.info = res.result.info
    }).catch((error) => {
    })
  }
}
</script>
