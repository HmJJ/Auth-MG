import { pause, resumeJob, modify, getAll } from '@/api/schedule'
import { getToken } from '@/libs/util'

export default {
  state: {
    token: getToken()
  },
  actions: {
    pause ({state, commit}, {name, group}) {
      return new Promise((resolve, reject) => {
        pause(state.token, {name, group}).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    resumeJob ({state, commit}, {name, group}) {
      return new Promise((resolve, reject) => {
        resumeJob(state.token, {name, group}).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    modify ({state, commit}, {name, group, cron, lang}) {
      return new Promise((resolve, reject) => {
        modify(state.token, {name, group, cron, lang}).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getAll ({state, commit}) {
      return new Promise((resolve, reject) => {
        getAll(state.token).then(res => {
          console.log(res)
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
