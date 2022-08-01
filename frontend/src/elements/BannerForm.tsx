import Form, {FormInstance} from "antd/es/form";
import React, {FormEvent, ReactNode, useState} from "react";
import {MyLabel} from "../Util";
import Input from "antd/es/input";
import {Col, Menu, Popconfirm, Row, Tag} from "antd";
import Button from "antd/es/button";
import TextArea from "antd/es/input/TextArea";
import Modal from "antd/es/modal/Modal";
import {CategoryDto} from "../api/Dtos";

function BannerForm(props: {
    form: FormInstance,
    onChange: (event: FormEvent<HTMLFormElement>) => void,
    onSave: () => void,
    onDelete: () => void,
    skipPopConfirm?: boolean,
    categories: CategoryDto[],
    onCatTagClose?: () => void
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
        <Form form={props.form} onChange={props.onChange}>
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
                        <Form.List name="categories">
                            {(fields, {add, remove}, {errors}): ReactNode => (
                                <Row>
                                    {fields.map((field, index) => (
                                        <Col key={field.key}>
                                            <Form.Item
                                                {...field}
                                                label={ index === 0 ? <MyLabel value="Categories"/> : ''}
                                            >
                                                <Tag closable style={{fontSize:"14px"}}>
                                                    {props.form.getFieldValue("categories")[field.key].name}
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
                            >
                            </Menu>
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
                        title={"Do you really want to delete this category?"}
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
