import request from '@/request/request'

export function getNotifyMessageByPage(pageSize, pageNum){
  return request({
    url: '/notifyMessage/getNotifyMessageByPage',
    method: 'POST',
    data: {
      pageSize, 
      pageNum
    }
  })
}

export function getNotifyMessageCount(viewed){
  return request({
    url: '/notifyMessage/getNotifyMessageCount',
    method: 'POST',
    data: {
      viewed
    }
  })
}