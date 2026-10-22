import request from './index'

export function login(username, password) {
  return request.post('/user/login', { username, password })
}

export function register(username, password, nickname) {
  return request.post('/user/register', { username, password, nickname })
}

export function logout() {
  return request.post('/user/logout')
}

export function getInfo() {
  return request.get('/user/info')
}
