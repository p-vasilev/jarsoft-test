import React from 'react';
import './App.css';
import {LoginForm} from "./LoginForm";
import Layout, {Content, Header} from "antd/es/layout/layout";
import Sider from "antd/es/layout/Sider";
import {Alert, Menu, Space} from "antd";
import "antd/dist/antd.css"
import {AuthenticatedApi} from "./api/AuthenticatedApi";
import {login} from "./api/Api";
import {AxiosResponse} from "axios";
import {ItemType} from "antd/es/menu/hooks/useItems";

interface AppState {
    authenticated: boolean
    jwt?: string
    authenticatedApi?: AuthenticatedApi
    siderList: string[]
    siderSearchString: string
    errors: string[]
    currentPart: "login" | "category" | "banner"
}

class App extends React.Component<any, AppState> {
    constructor(props: any) {
        super(props)
        this.state = {
            authenticated: false,
            siderList: [],
            siderSearchString: "",
            errors: [],
            currentPart: "login"
        }

        this.handleMenuClick.bind(this)
    }
    renderContent() {
        switch(this.state.currentPart) {
            case "login":
                return(
                    <div>
                        <p>Welcome!</p>
                        <Space direction="vertical" size='large'/>
                        <LoginForm handleFinish={(data: any)=> {
                            login(data.username, data.password)
                                .then((value: AxiosResponse<any>) => {
                                    console.log("Login response... get!")
                                    console.log(value)
                                    console.log(value.status)
                                    console.log(value.data)
                                    console.log(value.headers["JWT"])
                                })
                        }}/>
                    </div>
                )
            case "banner":
                return <div/>
            case "category":
                return <div/>
        }
    }

    handleMenuClick(item: ItemType) {
        if (!item)
            return
        let newPart: "login" | "category" | "banner" | undefined
        switch (item.key) {
            case '1':
                newPart = "login"
                break;
            case '2':
                newPart = "category"
                break;
            case '3':
                newPart = "banner"
                break;
        }
        if (!newPart)
            return
        this.setState({
            currentPart: newPart
        })
    }

    render() {
        return (
            <div className="App">
                <Layout>
                    <Header className="App-header">
                        <p className="App-title">Banners</p>
                        <Menu
                            items={[
                                {key: '1', label: 'Home'},
                                {key: '2', label: 'Categories'},
                                {key: '3', label: 'Banners'}
                            ]}
                            defaultActiveFirst={true}
                            defaultSelectedKeys={['1']}
                            mode="horizontal"
                            theme="dark"
                            className="App-menu"
                            onClick = {(i: ItemType) => this.handleMenuClick(i)}
                        >
                        </Menu>
                    </Header>

                    <Layout hasSider>
                        <Sider className="App-sider">sider</Sider>
                        <Content className="App-content">
                            {this.renderContent()}
                        </Content>
                    </Layout>
                </Layout>
            </div>
        );
    }
}

export default App;
