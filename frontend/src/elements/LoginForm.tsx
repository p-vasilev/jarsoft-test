import Form from "antd/es/form";
import Input from "antd/es/input";
import Button from "antd/es/button";
import React from "react";
import {MyLabel} from "../Util";


function LoginForm(props: {
    authenticated: boolean,
    handleFinish?: (a: any)=>void
}) {
    return (
        <div>
            <p>Welcome!</p>
            {!props.authenticated ?
                <Form
                    onFinish={props.handleFinish ? props.handleFinish : (a) => {}}
                    className="Login-form"
                >
                    <Form.Item
                        label={<MyLabel value="Username"/>}
                        name="username"
                        rules={[{required: true, message: 'Enter your username'}]}
                    >
                        <Input placeholder="Username"/>
                    </Form.Item>
                    <Form.Item
                        label={<MyLabel value="Password"/>}
                        name="password"
                        rules={[{required: true, message: 'Enter your password'}]}
                    >
                        <Input.Password placeholder="Password"/>
                    </Form.Item>
                    <Button type='primary' htmlType="submit">Login</Button>
                </Form>
                : <p>You are authenticated!</p>
            }
        </div>
    )
}

export default LoginForm
