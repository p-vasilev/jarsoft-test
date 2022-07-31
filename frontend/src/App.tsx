import Layout, {Content, Header} from "antd/es/layout/layout";
import {Menu} from "antd";
import {ItemType} from "antd/es/menu/hooks/useItems";
import React, {FormEvent, useCallback, useEffect, useState} from "react";
import {CategoryDto, CategoryWithoutId, IdName} from "./api/Dtos";
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


function App() {
    const [authenticated, setAuthenticated] = useState(false)
    const [currentPart, setCurrentPart] = useState<"login" | "category" | "banner">("login")
    const [categories, setCategories] = useState<CategoryDto[]>()
    const [currentCategory, setCurrentCategory] = useState<CategoryDto>()
    const [catForm] = Form.useForm()
    const [bannerIdNames, setBannerIdNames] = useState<IdName[]>()
    const [authenticatedApi, setAuthenticatedApi] = useState<AuthenticatedApi>()
    const [newCategory, setNewCategory] = useState<CategoryWithoutId>()
    const [creatingNewItem, setCreatingNewItem] = useState(false)

    const [justAuthenticated, setJustAuthenticated] = useState(false)

    const updateCategories = useCallback(() => {
        console.log("Getting categories...")
        console.log(authenticatedApi)
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

    const handleHeaderMenuClick = (item: ItemType) => {
        if (!item)
            return
        let newPart: "login" | "category" | "banner" | undefined
        switch (item.key) {
            case '1':
                newPart = "login"
                break;
            case '2':
                newPart = "category"
                updateCategories()
                break;
            case '3':
                newPart = "banner"
                break;
        }
        if (!newPart)
            return
        setCurrentPart(newPart)
    }

    const BannerForm = () => {
        return (
            <></>
        )
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
                        setCreatingNewItem(false)
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

    const renderContent = () => {
        switch(currentPart) {
            case "login":
                return(
                    <LoginForm authenticated={authenticated} handleFinish={(data: any) => {
                        login(data.username, data.password)
                            .then((value: AxiosResponse) => {
                                console.log("Login response... get!")
                                console.log(value)
                                console.log(value.status)
                                let jwt = value.data
                                setAuthenticated(true)
                                setAuthenticatedApi(new AuthenticatedApi(jwt))
                                setCurrentPart("category")
                                setJustAuthenticated(true)
                                console.log(jwt)
                            })
                    }}/>
                )
            case "banner":
                return BannerForm()
            case "category":
                return ((currentCategory || newCategory) &&
                    <CategoryForm
                        form={catForm}
                        onChange={handleCategoryFormChange}
                        onSave={handleCategoryFormSave}
                        onDelete={handleCategoryFormDelete}
                        skipPopConfirm={creatingNewItem}
                    />
                )
            default:
                return <></>
        }
    }

    const updateCatForm = (t: CategoryDto) => {
        catForm.setFields([
            {name: "id", value: t.id},
            {name: "name", value: t.name},
            {name: "requestId", value: t.requestId}
        ])
    }

    const handleSidebarMenuClick = (item: ItemType) => {
        if (!item)
            return
        if (categories && currentPart === "category") {
            const t = categories[item.key as number - 1]
            console.log(t)
            setCreatingNewItem(false)
            setNewCategory(undefined)
            setCurrentCategory(t)
            updateCatForm(t)
        }
    }

    const handleNewItemClick = () => {
        if (currentPart === "category") {
            setCreatingNewItem(true)
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
    }

    const getSidebarLabels = () => {
        if (currentPart === "category" && categories)
            return categories.map((c) => c.name)
        if (currentPart === "banner" && bannerIdNames)
            return bannerIdNames.map((c) => c.name)
        return []
    }

    const getSidebarSelectedKeys = () => {
        if (currentPart === "category" && currentCategory) {
            const arr =
                stringArrayToMenuItems(getSidebarLabels())
                    .filter(value => value.label === currentCategory.name)
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
                            (currentPart === "login") ? '1' :
                                (currentPart === "category" ? '2' : '3')
                        ]}
                        mode="horizontal"
                        theme="dark"
                        className="App-menu"
                        onClick = {(i: ItemType) => handleHeaderMenuClick(i)}
                    />
                </Header>
                <Layout hasSider>
                    {
                        currentPart !== "login" ?
                            <SidebarMenu
                                labels={getSidebarLabels()}
                                onClick={handleSidebarMenuClick}
                                onClickNew={handleNewItemClick}
                                selectedKeys={getSidebarSelectedKeys()}
                            /> : null
                    }
                    <Content className="App-content">
                        {renderContent()}
                    </Content>
                </Layout>
            </Layout>
        </div>
    );
}

export default App
