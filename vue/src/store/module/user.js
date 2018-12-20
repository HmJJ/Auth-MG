import { login, logout, getUserInfo, addUser, modifyUser, deleteUser, getUser, modifyUserStatus, getUserRole, grandRole, modifyUserPassword } from '@/api/user'
import { setToken, getToken } from '@/libs/util'

export default {
  state: {
    username: '',
    name: '',
    userId: '',
    avatorImgPath: '',
    token: getToken(),
    access: ''
  },
  mutations: {
    setAvator (state, avatorPath) {
      state.avatorImgPath = avatorPath
    },
    setUserId (state, id) {
      state.userId = id
    },
    setUsername (state, username) {
      state.username = username
    },
    setName (state, name) {
      state.name = name
    },
    setAccess (state, access) {
      state.access = access
    },
    setToken (state, token) {
      state.token = token
      setToken(token)
    }
  },
  actions: {
    // 登录
    handleLogin ({commit}, {username, password}) {
      username = username.trim()
      return new Promise((resolve, reject) => {
        login({
          username,
          password
        }).then(res => {
          const data = res.data
          commit('setToken', data.token)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 退出登录
    handleLogOut ({state, commit}) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('setToken', '')
          commit('setAccess', [])
          resolve()
        }).catch(err => {
          reject(err)
        })
        // 如果你的退出登录无需请求接口，则可以直接使用下面三行代码而无需使用logout调用接口
        // commit('setToken', '')
        // commit('setAccess', [])
        // resolve()
      })
    },
    // 获取用户相关信息
    getUserInfo ({state, commit}) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(res => {
          const data = res.data
          commit('setAvator', data.avator)
          commit('setUsername', data.username)
          commit('setName', data.name)
          commit('setUserId', data.user_id)
          commit('setAccess', data.access)
          resolve(data)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addUser ({commit}, {username, name, password, email, phone, status, type}) {
      name = name.trim()
      username = username.trim()
      password = password.trim()
      email = email.trim()
      phone = phone.trim()
      type = type.trim()
      return new Promise((resolve, reject) => {
        addUser({
          username,
          name,
          password,
          email,
          phone,
          status,
          type
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    modifyUser ({commit}, {id, username, name, email, phone, status, type}) {
      name = name.trim()
      username = username.trim()
      email = email.trim()
      phone = phone.trim()
      type = type.trim()
      return new Promise((resolve, reject) => {
        modifyUser({
          id,
          username,
          name,
          email,
          phone,
          status,
          type
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUser ({commit}, {username, name, pageNum, pageSize}) {
      return new Promise((resolve, reject) => {
        getUser({
          username,
          name,
          pageNum,
          pageSize
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteUser ({commit}, {id}) {
      return new Promise((resolve, reject) => {
        deleteUser({
          id
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    modifyUserStatus ({commit}, {id, status}) {
      return new Promise((resolve, reject) => {
        modifyUserStatus({
          id,
          status
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    modifyUserPassword ({commit}, {id, password}) {
      return new Promise((resolve, reject) => {
        modifyUserPassword({
          id,
          password
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserRole ({commit}, {id}) {
      return new Promise((resolve, reject) => {
        getUserRole({
          id
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    grandRole ({commit}, {id, checkAllGroup}) {
      return new Promise((resolve, reject) => {
        grandRole({
          id,
          checkAllGroup
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
