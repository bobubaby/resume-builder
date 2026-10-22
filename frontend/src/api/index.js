import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

request.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body.code === 0) {
      return body.data
    }
    ElMessage.error(body.msg || '请求失败')
    return Promise.reject(new Error(body.msg))
  },
  (err) => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem('token')
      location.hash = '#/login'
    }
    ElMessage.error((err.response && err.response.data && err.response.data.msg) || '网络异常')
    return Promise.reject(err)
  }
)

export default request
