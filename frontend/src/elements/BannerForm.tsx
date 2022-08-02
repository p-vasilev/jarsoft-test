import Form, {FormInstance} from "antd/es/form";
import React, {ReactNode, useState} from "react";
import {MyLabel} from "../Util";
import Input from "antd/es/input";
import {Col, Menu, Popconfirm, Row, Tag} from "antd";
import Button from "antd/es/button";
import TextArea from "antd/es/input/TextArea";
import Modal from "antd/es/modal/Modal";
import {CategoryDto} from "../api/Dtos";
import {ItemType} from "antd/es/menu/hooks/useItems";

function BannerForm(props: {
    form: FormInstance,
    onSave: () => void,
    onDelete: () => void,
    skipPopConfirm?: boolean,
    categories: CategoryDto[],
    onCatTagClose: (e: number) => void,
    onCatMenuClick: (i: ItemType) => void
}) {
    const [popVisible, setPopVisible] = useState(false)
    const [catModalVisible, setCatModalVisible] = useState(false)

    const handleVisibleChange = (newVisible: boolean) => {
        if (!newVisible) {
            setPopVisible(newVisible)
            return
        }

        if (props.skipPopConfirm) {
            props.onDelete()
        } else {
            setPopVisible(newVisible)
        }
    }

    return(
        <Form form={props.form}>
            <div className="Login-form">
                <Form.Item name="id" label={<MyLabel value="ID"/>}>
                    <Input disabled/>
                </Form.Item>
                <Form.Item name="name" label={<MyLabel value="Name"/>}>
                    <Input/>
                </Form.Item>
                <Form.Item name="price" label={<MyLabel value="Price"/>}>
                    <Input/>
                </Form.Item>
                <Row>
                    <Form.Item name="categories">
                        <Form.List name="categories" >
                            {(fields): ReactNode => (
                                <Row>
                                    <MyLabel value="Categories:"/>
                                    {fields.map((field) => (
                                        <Col key={field.key}>
                                            <Form.Item
                                                {...field}
                                            >
                                                <Tag closable style={{fontSize:"14px"}} onClose={(_) => {
                                                    props.onCatTagClose(field.key)
                                                }}>
                                                    {props.categories.find(
                                                        (c) => c.id ===
                                                            props.form.getFieldValue("categories")[field.key]
                                                    )?.name}
                                                </Tag>
                                            </Form.Item>
                                        </Col>
                                    ))}
                                </Row>
                                )
                            }
                        </Form.List>
                    </Form.Item>
                    <Col>
                        <Button type="primary" onClick={()=>{setCatModalVisible(true)}}>
                            Add
                        </Button>
                        <Modal
                            visible={catModalVisible}
                            footer={null}
                            onCancel={()=>setCatModalVisible(false)}
                            style={{
                                height: "50vh"
                            }}
                        >
                            <p>Choose category:</p>
                            <Input></Input>
                            <Menu
                                style={{
                                    overflow: "auto",
                                    height: "50vh"
                                }}
                                items={
                                    props.categories.map((c) => ({
                                        key: c.id,
                                        label: c.name
                                    }))
                                }
                                onClick={(item: ItemType) => {
                                    props.onCatMenuClick(item)
                                    setCatModalVisible(false)
                                    }
                                }
                            />
                        </Modal>
                    </Col>
                </Row>
                <Form.Item name="text" label={<MyLabel value="Text"/>}>
                    <TextArea cols={64}/>
                </Form.Item>
            </div>
            <Row>
                <Col style={{paddingLeft:"100px"}}>
                    <Button type='primary' onClick={props.onSave}>
                        Save
                    </Button>
                </Col>
                <Col style={{position:"absolute", right:"100px"}}>
                    <Popconfirm
                        title={"Do you really want to delete this banner?"}
                        onConfirm={props.onDelete}
                        onCancel={() => setPopVisible(false)}
                        okText="Yes"
                        cancelText="No"
                        visible={popVisible}
                        onVisibleChange={handleVisibleChange}
                    >
                        <Button
                            type="primary"
                            style={{backgroundColor: "red", borderColor:"red"}}
                        >
                            Delete
                        </Button>
                    </Popconfirm>
                </Col>
            </Row>
        </Form>
    )
}

export default BannerForm
