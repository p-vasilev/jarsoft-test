import React, {useState} from "react";
import Form, {FormInstance} from "antd/es/form";
import Input from "antd/es/input";
import {Col, Popconfirm, Row} from "antd";
import Button from "antd/es/button";
import {MyLabel} from "../Util";

function CategoryForm(props: {
    form: FormInstance,
    onSave: () => void,
    onDelete: () => void,
    skipPopConfirm?: boolean
}) {
    const [popVisible, setPopVisible] = useState(false)

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
                <Form.Item name="requestId" label={<MyLabel value="Request ID"/>}>
                    <Input/>
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

export default CategoryForm
