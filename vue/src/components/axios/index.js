import axios from 'axios'


axios.defaults.timeout = 5000
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.baseURL = 'http://localhost:6060'


//返回状态判断
axios.interceptors.response.use((res) =>{
  //对响应数据做些事
  if(res.status  !=200){
    // _.toast(res.data.msg);
    return Promise.reject(res)
  }
  return res
}, (error,res) => {
  //404等问题可以在这里处理
  res.toast("网络异常", 'fail')
  return Promise.reje98765432+-
    ct(error)
})



  export function fetchPost(url, params) {
    return new Promise((resolve, reject) => {
      axios.post(url, params)
        .then(response => {
          resolve(response.data)
        }, err => {
          reject(err)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

export function fetchGet(url, params) {
  return new Promise((resolve, reject) => {
    axios.get(url, params)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}
    export default {
      /**
       * 得到所有语言
       */
      fetchGet,
      fetchPost

  }
