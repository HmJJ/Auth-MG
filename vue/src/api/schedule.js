import axios from '@/libs/api.request'

export const pause = ({ token }, {name, group}) => {
  return axios.request({
    url: '/quartz/pause',
    params: {
      token,
      name,
      group
    },
    method: 'post'
  })
}
export const resumeJob = ({ token }, {name, group}) => {
  return axios.request({
    url: '/quartz/resumeJob',
    params: {
      token,
      name,
      group
    },
    method: 'post'
  })
}
export const modify = ({ token }, {name, group, cron, lang}) => {
  return axios.request({
    url: '/quartz/modify',
    params: {
      token,
      name,
      group,
      cron,
      lang
    },
    method: 'post'
  })
}
export const getAll = ({ token }) => {
  return axios.request({
    url: '/quartz/getAll',
    params: {
      token
    },
    method: 'post'
  })
}
