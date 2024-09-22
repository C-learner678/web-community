import request from '@/request/request'

export function getScore(userId){
  return request({
    url: '/score/getScore',
    method: 'POST',
    data: {
      userId
    }
  })
}

export function getScores(n){
  return request({
    url: '/score/getScores',
    method: 'POST',
    data: {
      n
    }
  })
}