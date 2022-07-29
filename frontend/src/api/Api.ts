import {AxiosInstance} from "axios";

export function createAxios(): AxiosInstance {
    let instance = require('axios').default.create()
    instance.defaults.baseURL = "http://localhost:8080"
    // instance.defaults.headers.common = {"Origin": "a"}

    return instance
}

export function login(username: string, password: string) {
    const axios = createAxios()
    return axios.post("/api/login", {username: username, password: password})
}

function displayErrorMessage() {

}

