<template>
  <div>
    <el-row>
      <el-col :span="6">&nbsp;</el-col>
      <el-col :span="12">
        <el-form :label-position="'left'" label-width="80px" :model="ModifyPasswordForm" :rules="rules" ref="ModifyPasswordForm">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="ModifyPasswordForm.oldPassword" show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="ModifyPasswordForm.newPassword" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-col :span="18">
              &nbsp;
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="submitModifyForm()">确认</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { modifyPassword } from '@/api/userApi'

export default {
  name: 'ModifyPassword',
  props: ['userId'],
  methods: {
    submitModifyForm(){
      this.$refs.ModifyPasswordForm.validate((valid) => {
        if (valid) {
          modifyPassword(this.ModifyPasswordForm.oldPassword, this.ModifyPasswordForm.newPassword)
          .then((res) => {
            this.$message.success('修改密码成功')
            window.location.reload()
          }).catch((error) => {
          })
        }
      })
    }
  },
  data() {
    return {
      ModifyPasswordForm: {
        oldPassword: '',
        newPassword: ''
      },
      rules: {
        oldPassword: [
          { required: true, message: '请输入旧密码' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码' },
        ]
      },
    }
  },
  created() {
    
  }
}
</script>
