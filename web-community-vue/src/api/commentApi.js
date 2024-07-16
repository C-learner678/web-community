import request from '@/request/request'

export function getCommentByPage(postId, userId, pageSize, pageNum){
  return request({
    url: '/comment/getCommentByPage',
    method: 'POST',
    data: {
      postId,
      userId, 
      pageSize, 
      pageNum
    }
  })
}

export function getCommentCount(postId, userId){
  return request({
    url: '/comment/getCommentCount',
    method: 'POST',
    data: {
      postId,
      userId
    }
  })
}

export function addComment(postId, content){
  return request({
    url: '/comment/addComment',
    method: 'POST',
    data: {
      postId,
      content
    }
  })  
}

export function deleteComment(commentId){
  return request({
    url: '/comment/deleteComment',
    method: 'POST',
    data: {
      commentId
    }
  })  
}