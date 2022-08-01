
export interface IdName {
    id: number
    name: string
}

export interface BannerDto {
    id: number
    name: string
    text: string
    price: number
    categories: CategoryDto[]
}

export interface CategoryDto extends CategoryWithoutId {
    id: number
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

export function bannerStripId(b: BannerDto) {
    return {
        name: b.name,
        price: b.price,
        text: b.text,
        categories: b.categories.map((c) => (c.id))
    }
}
