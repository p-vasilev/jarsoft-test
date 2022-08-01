import {createAxios} from "./Api";
import {AxiosInstance} from "axios";
import {BannerDto, bannerStripId, BannerWithoutId, CategoryDto, CategoryWithoutId} from "./Dtos";

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

    getBannerIdNames() {
        return this.addAuthorityHeader(createAxios())
            .get("/banner/ids_and_names")
    }

    getBanner(id: number) {
        return this.addAuthorityHeader(createAxios())
            .get("/banner/" + id)
    }

    deleteBanner(id: number) {
        return this.addAuthorityHeader(createAxios())
            .delete("/banner/" + id)
    }

    updateBanner(b: BannerDto) {
        return this.addAuthorityHeader(createAxios())
            .put("/banner/" + b.id, bannerStripId(b))
    }

    createBanner(b: BannerWithoutId) {
        return this.addAuthorityHeader(createAxios())
            .post("/banner/new", b)
    }
}
