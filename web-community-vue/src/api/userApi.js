import request from '@/request/request'

export function getCaptcha(){
  return request({
    url: '/user/getCaptcha',
    method: 'POST'
  })
}

export function login(name, password, captcha, captchaKey) {
  return request({
    url: '/user/login',
    method: 'POST',
    data: {
      name,
      password,
      captcha,
      captchaKey
    }
  })
}

export function register(name, password, captcha, captchaKey) {
  return request({
    url: '/user/register',
    method: 'POST',
    data: {
      name,
      password,
      captcha,
      captchaKey
    }
  })
}

export function getCurrentUser() {
  return request({
    url: '/user/getCurrentUser',
    method: 'POST',
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'POST',    
  })
}

export function modifyPassword(oldPassword, newPassword) {
  return request({
    url: '/user/modifyPassword',
    method: 'POST',
    data: {
      oldPassword, 
      newPassword
    }
  })
}

export function getUserInfo(userId) {
  return request({
    url: '/userInfo/getUserInfo',
    method: 'POST',
    data: {
      userId
    }
  })
}

export function modifyUserInfo(frontName, info) {
  return request({
    url: '/userInfo/modifyUserInfo',
    method: 'POST',
    data: {
      frontName,
      info
    }
  })  
}

export function modifyAvatar(avatar) {
  return request({
    url: '/userInfo/modifyAvatar',
    method: 'POST',
    data: {
      avatar
    }
  })  
}

export function modifyUserStatus(userId, ban){
  return request({
    url: '/user/modifyUserStatus',
    method: 'POST',
    data: {
      userId,
      ban
    }
  })  
}
