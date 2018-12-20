import axios from '@/libs/api.request'

export const login = ({username, password}) => {
  return axios.request({
    url: 'login',
    params: {
      username,
      password
    },
    method: 'post'
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: 'user/getUserInfo',
    params: {
      token
    },
    method: 'post'
  })
}

export const logout = (token) => {
  return axios.request({
    url: 'logout',
    method: 'post'
  })
}

export const addUser = ({username, name, password, email, phone, status, type}) => {
  console.log(1111)
  const data = {
    'name': name,
    'username': username,
    'password': password,
    'email': email,
    'phone': phone,
    'status': status,
    'type': type
  }
  return axios.request({
    url: 'user/addUser',
    data,
    method: 'POST'
  })
}

export const modifyUser = ({id, username, name, email, phone, status, type}) => {
  const data = {
    'id': id,
    'name': name,
    'username': username,
    'email': email,
    'phone': phone,
    'status': status,
    'type': type
  }
  return axios.request({
    url: 'user/addUser',
    data,
    method: 'POST'
  })
}

export const getUser = ({username, name, pageNum, pageSize}) => {
  return axios.request({
    url: 'user/queryUser',
    params: {
      username,
      name,
      pageNum,
      pageSize
    },
    method: 'POST'
  })
}

export const deleteUser = ({id}) => {
  return axios.request({
    url: 'user/deleteUser',
    params: {
      id
    },
    method: 'POST'
  })
}

export const modifyUserStatus = ({id, status}) => {
  return axios.request({
    url: 'user/modifyUserStatus',
    params: {
      id,
      status
    },
    method: 'POST'
  })
}

export const modifyUserPassword = ({id, password}) => {
  return axios.request({
    url: 'user/modifyUserPassword',
    params: {
      id,
      password
    },
    method: 'POST'
  })
}

export const getUserRole = ({id}) => {
  return axios.request({
    url: 'user/getUserRoleById',
    params: {
      id
    },
    method: 'POST'
  })
}

export const grandRole = ({id, checkAllGroup}) => {
  return axios.request({
    url: 'user/grandRole',
    params: {
      id,
      checkAllGroup
    },
    method: 'POST'
  })
}
