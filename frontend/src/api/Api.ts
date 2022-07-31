import {AxiosInstance} from "axios";

export function createAxios(): AxiosInstance {
    let instance = require('axios').default.create()
    instance.defaults.baseURL = "http://localhost:8080/api"

    return instance
}

export function login(username: string, password: string) {
    const axios = createAxios()
    axios.defaults.responseType = "text"
    return axios.post("/login", {username: username, password: password})
}

