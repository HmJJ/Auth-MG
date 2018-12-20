import { addRole, modifyRole, getRole, deleteRole, getRoleById, modifyRoleStatus, getRoles, addOrModifyPermission, getUserResource } from '@/api/role'
import { getToken } from '@/libs/util'

export default {
  state: {
    token: getToken()
  },
  actions: {
    addRole ({commit}, {name, code, description, icon, sort, status, type}) {
      name = name.trim()
      code = code.trim()
      description = description.trim()
      icon = icon.trim()
      sort = sort.trim()
      type = type.trim()
      return new Promise((resolve, reject) => {
        addRole({
          name,
          code,
          description,
          icon,
          sort,
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
    modifyRole ({commit}, {id, name, code, description, icon, sort, status, type}) {
      name = name.trim()
      code = code.trim()
      description = description.trim()
      icon = icon.trim()
      sort = sort.trim()
      type = type.trim()
      return new Promise((resolve, reject) => {
        modifyRole({
          id,
          name,
          code,
          description,
          icon,
          sort,
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
    getRole ({commit}, {name, code, pageNum, pageSize}) {
      return new Promise((resolve, reject) => {
        getRole({
          name,
          code,
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
    getRoles ({commit}) {
      return new Promise((resolve, reject) => {
        getRoles().then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteRole ({commit}, {id}) {
      return new Promise((resolve, reject) => {
        deleteRole({
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
    getRoleById ({commit}, {id}) {
      return new Promise((resolve, reject) => {
        getRoleById({
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
    modifyRoleStatus ({commit}, {id, status}) {
      return new Promise((resolve, reject) => {
        modifyRoleStatus({
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
    addOrModifyPermission ({commit}, {id, list}) {
      return new Promise((resolve, reject) => {
        addOrModifyPermission({
          id,
          list
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserResource ({commit}, {id}) {
      return new Promise((resolve, reject) => {
        getUserResource({
          id
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
