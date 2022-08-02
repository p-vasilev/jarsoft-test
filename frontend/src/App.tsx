import Layout, {Content, Header} from "antd/es/layout/layout";
import {Menu} from "antd";
import {ItemType} from "antd/es/menu/hooks/useItems";
import React, {useCallback, useEffect, useState} from "react";
import {BannerDto, CategoryDto, IdName} from "./api/Dtos";
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


function App() {
    const [authenticated, setAuthenticated] = useState(false)
    const [authenticatedApi, setAuthenticatedApi] = useState<AuthenticatedApi>()
    const [justAuthenticated, setJustAuthenticated] = useState(false)

    const [currentView, setCurrentView] = useState<"login" | "category" | "banner">("login")
    const [creatingNewItem, setCreatingNewItem] = useState(false)
    const [formIsOpen, setFormIsOpen] = useState(false)

    const [categories, setCategories] = useState<CategoryDto[]>()
    const [catForm] = Form.useForm<{
        id?: number
        name: string
        requestId: string
    }>()

    const [bannerIdNames, setBannerIdNames] = useState<IdName[]>()
    const [bannerForm] = Form.useForm<{
        id?: number,
        name: string,
        price: number,
        categories: number[],
        text: string
    }>()

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
        setFormIsOpen(false)
        setCreatingNewItem(false)
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
        if (!creatingNewItem) {
            authenticatedApi?.updateCategory(catForm.getFieldsValue() as CategoryDto)
                .then((value) => {
                    console.log("received update category response")
                    console.log(value.status)
                    console.log(value.statusText)
                    updateCategories()
                })
        } else if (creatingNewItem) {
            authenticatedApi?.createCategory(catForm.getFieldsValue())
                .then((value) => {
                    console.log("received update category response")
                    console.log(value.status)
                    console.log(value.data)
                    catForm.setFieldValue("id", value.data)
                    updateCategories()
                })
        }
    }

    const handleCategoryFormDelete = () => {
        if (!creatingNewItem) {
            const id = catForm.getFieldsValue().id
            if (!id)
                return
            authenticatedApi?.deleteCategory(id)
                .then(()=>{
                    console.log("deleted category:")
                    console.log(catForm.getFieldsValue())
                    setFormIsOpen(false)
                    updateCategories()
                })
        } else if (creatingNewItem) {
            setFormIsOpen(false)
            setCreatingNewItem(false)
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

    const handleBannerFormSave = () => {
        const b = bannerForm.getFieldsValue()
        if (!creatingNewItem && b.id && categories) {
            authenticatedApi?.updateBanner({
                id: b.id,
                name: b.name,
                price: b.price,
                text: b.text,
                categories: categories.filter((c)=>b.categories.includes(c.id))
            })
                .then((value) => {
                    console.log("Got response to update banner request")
                    console.log(value)
                    updateBannerIdNames()
                })
        } else if (creatingNewItem) {
            authenticatedApi?.createBanner({
                name: b.name,
                price: b.price,
                text: b.text,
                categories: b.categories
            })
                .then((value) => {
                    console.log("Created banner")
                    console.log(value)
                    setCreatingNewItem(false)
                    bannerForm.setFieldValue("id", value.data)
                    updateBannerIdNames()
                })

        }
    }

    const handleBannerFormDelete = () => {
        if(!creatingNewItem) {
            const id = bannerForm.getFieldsValue().id
            if (!id)
                return
            authenticatedApi?.deleteBanner(id)
                .then(()=>{
                    console.log("Deleted banner")
                    setFormIsOpen(false)
                    updateBannerIdNames()
                })
        } else if (creatingNewItem) {
            setFormIsOpen(false)
            setCreatingNewItem(false)
        }
    }

    const handleBannerFormCatTagClose = (index: number) => {
        const b = bannerForm.getFieldsValue()
        bannerForm.setFieldValue(
            "categories",
            b.categories.filter((v, i) => i !== index)
        )
    }

    const handleBannerFormCatMenuClick = (i: ItemType) => {
        if (!categories || !i)
            return
        console.log(i)
        console.log(categories)
        const cat = categories.find((c) => c.id.toString() === i.key)
        if (!cat) {
            console.log("couldn't find category")
            console.log(cat)
            return
        }
        const newCats = bannerForm.getFieldsValue().categories.concat(cat.id)
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
                return (formIsOpen && categories ?
                    <BannerForm
                        form={bannerForm}
                        onDelete={handleBannerFormDelete}
                        onSave={handleBannerFormSave}
                        skipPopConfirm={creatingNewItem}
                        categories={categories}
                        onCatTagClose={handleBannerFormCatTagClose}
                        onCatMenuClick={handleBannerFormCatMenuClick}
                    /> : <div/>
                )
            case "category":
                return (formIsOpen ?
                    <CategoryForm
                        form={catForm}
                        onSave={handleCategoryFormSave}
                        onDelete={handleCategoryFormDelete}
                        skipPopConfirm={creatingNewItem}
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
            setFormIsOpen(true)
            catForm.setFields([
                {name: "id", value: t.id},
                {name: "name", value: t.name},
                {name: "requestId", value: t.requestId}
            ])
            return
        }
        if (bannerIdNames && currentView === "banner") {
            const t = bannerIdNames[item.key as number - 1]
            console.log("getting banner:")
            console.log(t)
            authenticatedApi?.getBanner(t.id)
                .then((value)=>{
                    const banner: BannerDto = value.data
                    bannerForm.setFields([
                        {name: "id", value: banner.id},
                        {name: "name", value: banner.name},
                        {name: "price", value: banner.price},
                        {name: "categories", value: banner.categories.map((c)=>c.id)},
                        {name: "text", value: banner.text},
                    ])
                    setFormIsOpen(true)
                })
        }
    }

    const handleNewItemClick = () => {
        if (currentView === "category") {
            setCreatingNewItem(true)
            setFormIsOpen(true)
            catForm.setFields([
                {name: "id", value: ""},
                {name: "name", value: ""},
                {name: "requestId", value: ""}
            ])
        }
        if (currentView === "banner") {
            setCreatingNewItem(true)
            setFormIsOpen(true)
            bannerForm.setFields([
                {name:"id", value:""},
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
        if (currentView === "category") {
            const arr =
                stringArrayToMenuItems(getSidebarLabels())
                    .filter(value => value.label === catForm.getFieldsValue().name)
            if (arr.length !== 1)
                return []
            return [arr[0].key]
        }
        if (currentView === "banner") {
            const arr =
                stringArrayToMenuItems(getSidebarLabels())
                    .filter(value => value.label === bannerForm.getFieldsValue().name)
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
