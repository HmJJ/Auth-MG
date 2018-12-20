import axios from '@/libs/api.request'

export const addRole = ({name, code, description, icon, sort, status, type}) => {
  const data = {
    'name': name,
    'code': code,
    'description': description,
    'icon': icon,
    'sort': sort,
    'status': status,
    'type': type
  }
  return axios.request({
    url: 'role/addRole',
    data,
    method: 'POST'
  })
}

export const modifyRole = ({id, name, code, description, icon, sort, status, type}) => {
  const data = {
    'id': id,
    'name': name,
    'code': code,
    'description': description,
    'icon': icon,
    'sort': sort,
    'status': status,
    'type': type
  }
  return axios.request({
    url: 'role/addRole',
    data,
    method: 'POST'
  })
}

export const getRole = ({name, code, pageNum, pageSize}) => {
  return axios.request({
    url: 'role/queryRolePage',
    params: {
      name,
      code,
      pageNum,
      pageSize
    },
    method: 'POST'
  })
}

export const getRoles = () => {
  return axios.request({
    url: 'role/queryRole',
    method: 'POST'
  })
}

export const deleteRole = ({id}) => {
  return axios.request({
    url: 'role/deleteRole',
    params: {
      id
    },
    method: 'POST'
  })
}

export const getRoleById = ({id}) => {
  return axios.request({
    url: 'role/getRoleById',
    params: {
      id
    },
    method: 'POST'
  })
}

export const modifyRoleStatus = ({id, status}) => {
  return axios.request({
    url: 'role/modifyRoleStatus',
    params: {
      id,
      status
    },
    method: 'POST'
  })
}

export const addOrModifyPermission = ({id, list}) => {
  return axios.request({
    url: 'role/addOrModifyPermission',
    params: {
      id,
      list
    },
    method: 'POST'
  })
}

export const getUserResource = ({id}) => {
  return axios.request({
    url: 'role/getUserResource',
    params: {
      id
    },
    method: 'POST'
  })
}
