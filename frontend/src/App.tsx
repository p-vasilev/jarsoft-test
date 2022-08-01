import Layout, {Content, Header} from "antd/es/layout/layout";
import {Menu} from "antd";
import {ItemType} from "antd/es/menu/hooks/useItems";
import React, {FormEvent, useCallback, useEffect, useState} from "react";
import {BannerDto, BannerWithoutId, CategoryDto, CategoryWithoutId, IdName} from "./api/Dtos";
import Form from "antd/es/form";
import {login} from "./api/Api";
import {AxiosResponse} from "axios";
import {AuthenticatedApi} from "./api/AuthenticatedApi";

import './App.css';
import "antd/dist/antd.css"
import LoginForm from "./elements/LoginForm";
import CategoryForm from "./elements/CategoryForm";
import SidebarMenu from "./elements/SidebarMenu";
import {stringArrayToMenuItems} from "./Util";
import BannerForm from "./elements/BannerForm";
import {carryValue} from "@testing-library/user-event/dist/keyboard/shared";
import MenuItem from "antd/es/menu/MenuItem";


function App() {
    const [authenticated, setAuthenticated] = useState(false)
    const [authenticatedApi, setAuthenticatedApi] = useState<AuthenticatedApi>()
    const [justAuthenticated, setJustAuthenticated] = useState(false)

    const [currentView, setCurrentView] = useState<"login" | "category" | "banner">("login")

    const [categories, setCategories] = useState<CategoryDto[]>()
    const [currentCategory, setCurrentCategory] = useState<CategoryDto>()
    const [newCategory, setNewCategory] = useState<CategoryWithoutId>()
    const [catForm] = Form.useForm<{
        id?: number,
        name: string,
        requestId: string
    }>()

    const [bannerIdNames, setBannerIdNames] = useState<IdName[]>()
    const [currentBanner, setCurrentBanner] = useState<BannerDto>()
    const [newBanner, setNewBanner] = useState<BannerWithoutId>()
    const [bannerForm] = Form.useForm()

    const [sidebarSearchString, setSidebarSearchString] = useState("")

    const updateCategories = useCallback(() => {
        console.log("Getting categories...")
        authenticatedApi?.getAllCategories()
            .then((value: AxiosResponse) => {
                console.log(value)
                if (value.status === 200)
                    setCategories(value.data)
            })
    }, [authenticatedApi])

    useEffect(function updateWhenJustAuthenticated() {
        if (justAuthenticated) {
            updateCategories()
            setJustAuthenticated(false)
        }
    }, [justAuthenticated, updateCategories])

    const updateBannerIdNames = () => {
        console.log("Getting banner ids and names...")
        authenticatedApi?.getBannerIdNames()
            .then((value: AxiosResponse) => {
                console.log(value)
                if (value.status === 200)
                    setBannerIdNames(value.data)
            })
    }

    const clearFormState = () => {
        setCurrentCategory(undefined)
        setNewCategory(undefined)
        setCurrentBanner(undefined)
        setNewBanner(undefined)
        setSidebarSearchString("")
    }

    const handleHeaderMenuClick = (item: ItemType) => {
        if (!item)
            return
        let newPart: "login" | "category" | "banner" | undefined
        switch (item.key) {
            case '1':
                newPart = "login"
                clearFormState()
                break;
            case '2':
                newPart = "category"
                clearFormState()
                updateCategories()
                break;
            case '3':
                newPart = "banner"
                clearFormState()
                updateBannerIdNames()
                break;
        }
        if (!newPart)
            return
        setCurrentView(newPart)
    }

    const handleCategoryFormSave = () => {
        console.log(currentCategory)
        if (currentCategory) {
            authenticatedApi?.updateCategory(currentCategory)
                .then((value) => {
                    console.log("received update category response")
                    console.log(value.status)
                    console.log(value.statusText)
                    updateCategories()
                })
        } else if (newCategory) {
            authenticatedApi?.createCategory(newCategory)
                .then((value) => {
                    console.log("received update category response")
                    console.log(value.status)
                    console.log(value.data)
                    if (newCategory) {
                        setNewCategory(undefined)
                        setCurrentCategory({
                            id: value.data,
                            name: newCategory.name,
                            requestId: newCategory.requestId
                        })
                    }
                    catForm.setFieldValue("id", value.data)
                    updateCategories()
                })
        }
    }

    const handleCategoryFormChange = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        const name = catForm.getFieldValue("name")
        const requestId = catForm.getFieldValue("requestId")
        if (currentCategory) {
            setCurrentCategory({
                id: currentCategory.id,
                name: name,
                requestId: requestId
            })
        } else if (newCategory) {
            setNewCategory({
                name: name,
                requestId: requestId
            })
        }
    }

    const handleCategoryFormDelete = () => {
        if (currentCategory) {
            authenticatedApi?.deleteCategory(currentCategory.id)
                .then(()=>{
                    console.log("deleted category:")
                    console.log(currentCategory)
                    setCurrentCategory(undefined)
                    updateCategories()
                })
        } else if (newCategory) {
            setNewCategory(undefined)
        }
    }

    const handleLoginFormFinish = (data: any) => {
        login(data.username, data.password)
            .then((value: AxiosResponse) => {
                console.log("Login response... get!")
                console.log(value)
                console.log(value.status)
                let jwt = value.data
                setAuthenticated(true)
                setAuthenticatedApi(new AuthenticatedApi(jwt))
                setCurrentView("category")
                setJustAuthenticated(true)
                console.log(jwt)
            })
    }

    const handleBannerFormChange = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        const name = bannerForm.getFieldValue("name")
        const price = bannerForm.getFieldValue("price")
        const text = bannerForm.getFieldValue("text")
        const cats = bannerForm.getFieldValue("categories")
        if (currentBanner) {
            setCurrentBanner({
                id: currentBanner.id,
                name: name,
                price: price,
                text: text,
                categories: cats
            })
        } else if (newBanner) {
            setNewBanner({
                name: name,
                price: price,
                text: text,
                categories: cats.map((c: CategoryDto)=>(c.id))
            })
        }
    }

    const handleBannerFormSave = () => {
        console.log(currentBanner)
        if (currentBanner) {
            authenticatedApi?.updateBanner(currentBanner)
                .then((value) => {
                    console.log("Got response to update banner request")
                    console.log(value)
                    updateBannerIdNames()
                })
        } else if (newBanner) {
            authenticatedApi?.createBanner(newBanner)
                .then((value) => {
                    console.log("Created banner")
                    console.log(value)
                    if (newBanner) {
                        setCurrentBanner({
                            id: value.data,
                            name: newBanner.name,
                            price: newBanner.price,
                            text: newBanner.text,
                            categories:
                                categories ?
                                    categories?.filter((c)=>(newBanner.categories.includes(c.id))) : []
                        })
                        setNewBanner(undefined)
                    }
                    bannerForm.setFieldValue("id", value.data)
                    updateBannerIdNames()
                })
        }
    }

    const handleBannerFormDelete = () => {
        if(currentBanner) {
            authenticatedApi?.deleteBanner(currentBanner.id)
                .then(()=>{
                    console.log("Deleted banner")
                    console.log(currentBanner)
                    setCurrentBanner(undefined)
                    updateBannerIdNames()
                })
        }
    }

    const handleBannerFormCatTagClose = (index: number) => {
        if (!currentBanner)
            return
        let newCats = currentBanner.categories.filter((v, i) => i !== index)
        console.log(categories)
        console.log(currentBanner.categories[index])
        console.log(newCats)
        setCurrentBanner({
            id: currentBanner.id,
            name: currentBanner.name,
            price: currentBanner.price,
            categories: newCats,
            text: currentBanner.text
        })
        bannerForm.setFieldValue("categories", newCats)
    }

    const handleBannerFormCatMenuClick = (i: ItemType) => {
        if (!currentBanner || !categories || !i)
            return
        console.log(i)
        console.log(categories)
        const cat = categories.find((c) => c.id.toString() === i.key)
        if (!cat) {
            console.log("couldn't find category")
            console.log(cat)
            return
        }
        const newCats = currentBanner.categories.concat(cat)
        setCurrentBanner({
            id: currentBanner.id,
            name: currentBanner.name,
            price: currentBanner.price,
            categories: newCats,
            text: currentBanner.text
        })
        bannerForm.setFieldValue("categories", newCats)
    }

    const MainContent = () => {
        switch(currentView) {
            case "login":
                return(
                    <LoginForm
                        authenticated={authenticated}
                        handleFinish={handleLoginFormFinish}
                    />
                )
            case "banner":
                return ((currentBanner || newBanner) && categories ?
                    <BannerForm
                        form={bannerForm}
                        onChange={handleBannerFormChange}
                        onDelete={handleBannerFormDelete}
                        onSave={handleBannerFormSave}
                        skipPopConfirm={newBanner !== undefined}
                        categories={categories}
                        onCatTagClose={handleBannerFormCatTagClose}
                        onCatMenuClick={handleBannerFormCatMenuClick}
                    /> : <div/>
                )
            case "category":
                return ((currentCategory || newCategory) ?
                    <CategoryForm
                        form={catForm}
                        onChange={handleCategoryFormChange}
                        onSave={handleCategoryFormSave}
                        onDelete={handleCategoryFormDelete}
                        skipPopConfirm={newCategory !== undefined}
                    /> : <div/>
                )
            default:
                return <div/>
        }
    }

    const handleSidebarMenuClick = (item: ItemType) => {
        if (!item)
            return
        if (categories && currentView === "category") {
            const t = categories[item.key as number - 1]
            console.log(t)
            setNewCategory(undefined)
            setCurrentCategory(t)
            catForm.setFields([
                {name: "id", value: t.id},
                {name: "name", value: t.name},
                {name: "requestId", value: t.requestId}
            ])
            return
        }
        if (bannerIdNames && currentView === "banner") {
            const t = bannerIdNames[item.key as number - 1]
            setNewBanner(undefined)
            authenticatedApi?.getBanner(t.id)
                .then((value)=>{
                    const banner: BannerDto = value.data
                    setCurrentBanner(banner)
                    bannerForm.setFields([
                        {name: "id", value: banner.id},
                        {name: "name", value: banner.name},
                        {name: "price", value: banner.price},
                        {name: "categories", value: banner.categories},
                        {name: "text", value: banner.text},
                    ])
                })
        }
    }

    const handleNewItemClick = () => {
        if (currentView === "category") {
            setCurrentCategory(undefined)
            setNewCategory({
                name: "",
                requestId: ""
            })
            catForm.setFields([
                {name: "id", value: ""},
                {name: "name", value: ""},
                {name: "requestId", value: ""}
            ])
        }
        if (currentView === "banner") {
            setCurrentBanner(undefined)
            setNewBanner({
                name: "",
                price: 0.0,
                text: "",
                categories: []
            })
            bannerForm.setFields([
                {name:"name", value:""},
                {name:"price", value:0.0},
                {name:"text", value:""},
                {name:"categories", value:[]}
            ])
        }
    }

    const getSidebarLabels = () => {
        const searchFilter = (s:string) => s.toLowerCase().includes(sidebarSearchString.toLowerCase())
        if (currentView === "category" && categories)
            return categories.map((c) => c.name)
                .filter(searchFilter)
        if (currentView === "banner" && bannerIdNames)
            return bannerIdNames.map((c) => c.name)
                .filter(searchFilter)
        return []
    }

    const getSidebarSelectedKeys = () => {
        if (currentView === "category" && currentCategory) {
            const arr =
                stringArrayToMenuItems(getSidebarLabels())
                    .filter(value => value.label === currentCategory.name)
            if (arr.length !== 1)
                return []
            return [arr[0].key]
        }
        if (currentView === "banner" && currentBanner) {
            const arr =
                stringArrayToMenuItems(getSidebarLabels())
                    .filter(value => value.label === currentBanner.name)
            if (arr.length !== 1)
                return []
            return [arr[0].key]

        }
        return []
    }

    return (
        <div className="App">
            <Layout>
                <Header className="App-header">
                    <p className="App-title">Banners</p>
                    <Menu
                        items={[
                            {label: 'Home', key: "1"},
                            {label: 'Categories', key: "2", disabled: !authenticated},
                            {label: 'Banner', key: "3", disabled: !authenticated}
                        ]}
                        selectedKeys={[
                            (currentView === "login") ? '1' :
                                (currentView === "category" ? '2' : '3')
                        ]}
                        mode="horizontal"
                        theme="dark"
                        className="App-menu"
                        onClick = {handleHeaderMenuClick}
                    />
                </Header>
                <Layout hasSider>
                    {
                        currentView !== "login" ?
                            <SidebarMenu
                                labels={getSidebarLabels()}
                                onClick={handleSidebarMenuClick}
                                onClickNew={handleNewItemClick}
                                selectedKeys={getSidebarSelectedKeys()}
                                searchString={sidebarSearchString}
                                onSearchChange={(e) => setSidebarSearchString(e.target.value)}
                            /> : null
                    }
                    <Content className="App-content">
                        <MainContent/>
                    </Content>
                </Layout>
            </Layout>
        </div>
    );
}

export default App
