import {createAxios} from "./Api";
import {AxiosInstance} from "axios";
import {CategoryDto, CategoryWithoutId} from "./Dtos";

export class AuthenticatedApi {
    private jwt: string;
    constructor(jwt: string) {
        this.jwt = jwt
    }

    private addAuthorityHeader(axios: AxiosInstance) {
        axios.defaults.headers.common = {"Authorization": "Bearer " + this.jwt}
        return axios
    }

    getAllCategories() {
        return this.addAuthorityHeader(createAxios())
            .get("/category/all")
    }

    updateCategory(c: CategoryDto) {
        return this.addAuthorityHeader(createAxios())
            .put("/category/" + c.id + "?name=" + c.name + "&requestId=" + c.requestId)
    }

    createCategory(c: CategoryWithoutId) {
        return this.addAuthorityHeader(createAxios())
            .post("/category/new?name=" + c.name + "&requestId=" + c.requestId)
    }

    deleteCategory(id: number) {
        return this.addAuthorityHeader(createAxios())
            .delete("/category/" + id)
    }
}
