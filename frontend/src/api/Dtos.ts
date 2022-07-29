
export interface IdName {
    id: string
    name: string
}

export interface BannerDto {
    id: string
    name: string
    text: string
    price: number
    categories: CategoryDto[]
}

export interface CategoryDto {
    id: string
    name: string
    requestId: string
}

export interface BannerWithoutId {
    name: string
    text: string
    price: number
    categories: number[]
}

export interface CategoryWithoutId {
    name: string
    requestId: string
}
