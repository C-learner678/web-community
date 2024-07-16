import request from '@/request/request'

export function getFollowNum(userId) {
  return request({
    url: '/userFollow/getFollowNum',
    method: 'POST',
    data: {
      userId
    }
  })  
}

export function getFollowerNum(userId) {
  return request({
    url: '/userFollow/getFollowerNum',
    method: 'POST',
    data: {
      userId
    }
  })  
}

export function getFollowByPage(userId, pageSize, pageNum) {
  return request({
    url: '/userFollow/getFollowByPage',
    method: 'POST',
    data: {
      userId,
      pageSize,
      pageNum
    }
  })  
}

export function getFollowerByPage(userId, pageSize, pageNum) {
  return request({
    url: '/userFollow/getFollowerByPage',
    method: 'POST',
    data: {
      userId,
      pageSize,
      pageNum
    }
  })  
}

export function isFollow(userId, followUserId) {
  return request({
    url: '/userFollow/isFollow',
    method: 'POST',
    data: {
      userId,
      followUserId
    }
  })  
}

export function addFollow(userId) {
  return request({
    url: '/userFollow/addFollow',
    method: 'POST',
    data: {
      userId
    }
  })  
}

export function removeFollow(userId) {
  return request({
    url: '/userFollow/removeFollow',
    method: 'POST',
    data: {
      userId
    }
  })  
}