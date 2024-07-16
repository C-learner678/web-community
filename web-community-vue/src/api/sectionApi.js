import request from '@/request/request'

export function getSection(sectionId){
  return request({
    url: '/section/getSection',
    method: 'POST',
    data: {
      sectionId
    }
  })
}

export function getSections(){
  return request({
    url: '/section/getSections',
    method: 'POST'
  })
}