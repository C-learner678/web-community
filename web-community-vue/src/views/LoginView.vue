<template>
  <div style="margin-top: 160px;">
    <el-row>
      <el-col :span="9">
        &nbsp;
      </el-col>
      <el-col :span="6">
        <el-card>
          <div slot="header" style="text-align: center; font-size: 24px">
            Community
          </div>
          <el-form :label-position="'left'" label-width="80px" :model="loginForm" :rules="rules" ref="loginForm">
            <el-form-item label="用户名" prop="name">
              <el-input v-model="loginForm.name"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="loginForm.password" show-password></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="captcha">
              <el-col :span="14">
                <el-input v-model="loginForm.captcha"></el-input>
              </el-col>
              <el-col :span="2">&nbsp;</el-col>
              <el-col :span="8">
                <el-image
                style="width: 80px; height: 40px"
                :src="imageUrl"
                @click="getCaptchaImage"></el-image>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="2">
                &nbsp;
              </el-col>
              <el-col :span="10">
                <el-button @click="submitRegisterForm()">注册</el-button>
              </el-col>
              <el-col :span="12">
                <el-button type="primary" @click="submitLoginForm()">登录</el-button>
              </el-col>
            </el-form-item>
          </el-form>
          <div style="text-align: right">
            <a href="main">作为游客访问</a>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Cookies from "js-cookie"
import md5 from 'js-md5'
import {login, register, getCaptcha} from "@/api/userApi"


export default {
  name: 'LoginView',
  methods: {
    submitLoginForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          login(this.loginForm.name, md5(this.loginForm.password), this.loginForm.captcha, this.loginForm.key)
          .then((res) => {
            Cookies.set("username", this.loginForm.name, { expires: 30 })
            sessionStorage.setItem('userToken', res.result)
            this.$router.push("/main")
          }).catch((error) => {
            this.getCaptchaImage()
          })
        } else {
          this.getCaptchaImage()
          return false
        }
      })
    },
    submitRegisterForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          register(this.loginForm.name, md5(this.loginForm.password), this.loginForm.captcha, this.loginForm.key)
          .then((res) => {
            Cookies.set("username", this.loginForm.name, { expires: 30 })
            sessionStorage.setItem('userToken', res.result)
            this.$router.push("/main")
          }).catch((error) => {
            this.getCaptchaImage()
          })
        } else {
          this.getCaptchaImage()
          return false
        }
      });
    },
    getCaptchaImage() {
      getCaptcha()
      .then((res) => {
        this.loginForm.key = res.result.key
        this.imageUrl = "data:image/png;base64," + res.result.captchaImage
      }).catch((error) => {

      })
    }
  },
  data() {
    return {
      loginForm: {
        name: '',
        password: '',
        captcha: '',
        key: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名' },
        ],
        password: [
          { required: true, message: '请输入密码' },
        ],
        captcha: [
          { required: true, message: '请输入验证码' },
        ]
      },
      imageUrl: ''
    }
  },
  created() {
    if(sessionStorage.getItem("userToken")){
      this.$router.push("/main")
    }
    this.loginForm.name = Cookies.get("username")
    this.getCaptchaImage()
  }
}
</script>


