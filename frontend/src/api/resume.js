import request from './index'

export function listResumes() {
  return request.get('/resume/list')
}

export function createResume(data) {
  return request.post('/resume', data)
}

export function getResume(id) {
  return request.get(`/resume/${id}`)
}

export function updateResumeMeta(id, data) {
  return request.put(`/resume/${id}`, data)
}

export function saveSections(id, sections) {
  return request.put(`/resume/${id}/sections`, sections)
}

export function copyResume(id) {
  return request.post(`/resume/${id}/copy`)
}

export function deleteResume(id) {
  return request.delete(`/resume/${id}`)
}
