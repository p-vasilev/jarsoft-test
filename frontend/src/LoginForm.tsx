import React from "react";
import Input from "antd/es/input";
import Form from "antd/es/form";
import Button from "antd/es/button";

interface LoginFormProps {
    handleFinish?: (data:any) => void
}

interface LoginFormState {
    username?: string
    password?: string
}

export class LoginForm extends React.Component<LoginFormProps, LoginFormState> {

    render() {
        const handleFinish = (data: any) => {
            alert(`Submitted: ${data.username}, ${data.password}`)
        }

        return (
            <Form onFinish={this.props.handleFinish ? this.props.handleFinish : handleFinish} className="Login-form">
                <Form.Item name="username" rules={[{required: true, message: 'Enter your username'}]}>
                    <Input placeholder="Username"/>
                </Form.Item>
                <Form.Item name="password" rules={[{required: true, message: 'Enter your password'}]}>
                    <Input placeholder="Password" type="password"/>
                </Form.Item>
                <Button type='primary' htmlType="submit">Login</Button>
            </Form>
        )
    }
}
