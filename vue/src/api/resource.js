import axios from '@/libs/api.request'

export const addResource = ({ id, title, code, url, description, type, parentId, status, sort }) => {
  const data = {
    'id': id,
    'title': title,
    'code': code,
    'url': url,
    'description': description,
    'type': type,
    'parentId': parentId,
    'status': status,
    'sort': sort
  }
  return axios.request({
    url: 'resource/addOrModifyResource',
    data,
    method: 'POST'
  })
}

export const getAllList = () => {
  return axios.request({
    url: 'resource/queryResource',
    method: 'POST'
  })
}

export const deletePermission = ({ selectList }) => {
  return axios.request({
    url: 'resource/deletePermission',
    params: {
      selectList
    },
    method: 'POST'
  })
}
