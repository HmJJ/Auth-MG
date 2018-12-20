import { addResource, getAllList, deletePermission } from '@/api/resource'
import { getToken } from '@/libs/util'

export default {
  state: {
    token: getToken()
  },
  actions: {
    addResource ({ commit }, { id, title, code, url, description, type, parentId, status, sort }) {
      title = title.trim()
      code = code.trim()
      url = url.trim()
      description = description.trim()
      return new Promise((resolve, reject) => {
        addResource({
          id,
          title,
          code,
          url,
          description,
          type,
          parentId,
          status,
          sort
        }).then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    getAllList ({ commit }) {
      return new Promise((resolve, reject) => {
        getAllList().then(res => {
          if (res !== false) {
            resolve(res)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    deletePermission ({ commit }, { selectList }) {
      return new Promise((resolve, reject) => {
        deletePermission({
          selectList
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
