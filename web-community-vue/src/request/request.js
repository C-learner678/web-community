//参考：https://developer.aliyun.com/article/1192031

import axios from 'axios'
import Vue from 'vue'
import BASE_URL from './baseUrl'

const request = axios.create({
  baseURL: BASE_URL,
  timeout: 5000,
})

//请求拦截器
request.interceptors.request.use(
  function (config) {
    const token = sessionStorage.getItem("userToken") ? sessionStorage.getItem("userToken") : null
    if (token) {
      config.headers['userToken'] = token
    }
    return config
  },
  function (error) {
    Vue.prototype.$message.error('出错了')
    return Promise.reject(error)
  }
)

//返回拦截器
request.interceptors.response.use(
  function (response) {
    if(response.data.status.code != 0){
      Vue.prototype.$message.error(response.data.status.msg)
      throw new Error()
    }
    return response.data
  },
  function (error) {
    Vue.prototype.$message.error(error.data.msg)
    return Promise.reject(error)
  }
)
export default request
