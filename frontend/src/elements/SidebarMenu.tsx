import Sider from "antd/es/layout/Sider";
import Input from "antd/es/input";
import {Menu} from "antd";
import Button from "antd/es/button";
import React from "react";
import {ItemType} from "antd/es/menu/hooks/useItems";
import {stringArrayToMenuItems} from "../Util";

const SidebarMenu = (props: {
    labels: string[],
    onClick: (item: ItemType) => void,
    selectedKeys?: string[],
    onClickNew: () => void,
    searchString: string,
    onSearchChange: (e: React.ChangeEvent<HTMLInputElement>) => void
}) => {
    return (
        <Sider className="App-sider">
            <Input
                placeholder="Search..."
                value={props.searchString}
                onChange={props.onSearchChange}
            />
            <Menu
                items={stringArrayToMenuItems(props.labels)}
                theme="dark"
                onClick={props.onClick}
                selectedKeys={props.selectedKeys? props.selectedKeys : []}
            />
            <Button
                onClick={props.onClickNew}
            >
                Create New +
            </Button>
        </Sider>
    )
}

export default SidebarMenu
