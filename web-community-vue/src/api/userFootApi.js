import request from '@/request/request'

export function getPostUserFoot(postId, userId){
  return request({
    url: '/userFoot/getPostUserFoot',
    method: 'POST',
    data: {
      postId,
      userId
    }
  })
}

export function modifyPostUserFoot(postId, type, positive){
  return request({
    url: '/userFoot/modifyPostUserFoot',
    method: 'POST',
    data: {
      postId,
      type,
      positive
    }
  })
}

export function getPostUserFootCount(postId){
  return request({
    url: '/userFoot/getPostUserFootCount',
    method: 'POST',
    data: {
      postId
    }
  })  
}

export function getCollectPostByPage(pageSize, pageNum){
  return request({
    url: '/userFoot/getCollectPostByPage',
    method: 'POST',
    data: {
      pageSize, 
      pageNum
    }
  })  
}

export function modifyCommentUserFoot(commentId, positive){
  return request({
    url: '/userFoot/modifyCommentUserFoot',
    method: 'POST',
    data: {
      commentId,
      positive
    }
  })
}