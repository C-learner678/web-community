import request from '@/request/request'

export function getPostByPage(userId, sectionId, top, pageSize, pageNum){
  return request({
    url: '/post/getPostByPage',
    method: 'POST',
    data: {
      userId, 
      sectionId, 
      top, 
      pageSize, 
      pageNum
    }
  })
}

export function getPostCount(userId, sectionId, top){
  return request({
    url: '/post/getPostCount',
    method: 'POST',
    data: {
      userId, 
      sectionId, 
      top, 
    }
  })
}

export function getPost(postId){
  return request({
    url: '/post/getPost',
    method: 'POST',
    data: {
      postId
    }
  })
}

export function addPost(sectionId, title, content){
  return request({
    url: '/post/addPost',
    method: 'POST',
    data: {
      sectionId,
      title,
      content
    }
  })
}

export function modifyPost(postId, title, content){
  return request({
    url: '/post/modifyPost',
    method: 'POST',
    data: {
      postId,
      title,
      content
    }
  })
}

export function deletePost(postId){
  return request({
    url: '/post/deletePost',
    method: 'POST',
    data: {
      postId
    }
  })
}

export function adminDeletePost(postId){
  return request({
    url: '/post/adminDeletePost',
    method: 'POST',
    data: {
      postId
    }
  })
}