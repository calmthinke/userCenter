import Footer from '@/components/Footer';
import {register} from '@/services/ant-design-pro/api';
import {LockOutlined, UserOutlined,} from '@ant-design/icons';
import {LoginForm, ProFormText,} from '@ant-design/pro-components';
import {Alert, Divider, message, Tabs} from 'antd';
import React, {useState} from 'react';
import {history, Link, useModel} from 'umi';
import styles from './index.less';
import {PLANET_LINK, SYSTEM_LOGO} from "@/constant";

const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => (
  <Alert
    style={{
      marginBottom: 24,
    }}
    message={content}
    type="error"
    showIcon
  />
);
const Register: React.FC = () => {
  const [userLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');

  // @ts-ignore
  const { initialState, setInitialState } = useModel('@@initialState');
  // 表单提交
  const handleSubmit = async (values: API.RegisterParams) => {
    //校验
    const {password,checkPassword} = values;
    if(password!==checkPassword)
    {
      message.error("两次输入的密码不一致");
      return;
    }
    try {
      // 注册
      const id = await register({
        ...values,
        // type,
      });

      if(id)
      {
        //注册成功
        const defaultRegisterSuccessMessage="注册成功";
        message.success(defaultRegisterSuccessMessage);

        /** 此方法会跳转到 redirect 参数所在的位置 */
        if (!history) return;
        const { query } = history.location;
        // const { redirect } = query as {
        //   redirect: string;
        // };
        //当没有登录便访问redirect的页面,重定向到login页面
        // history.push('/user/login?redirect='+redirect);
        history.push({
          pathname:"/user/login",
          query
        })
        return;
      }
      // else{
      //   throw new Error(res.description);
      // }

    }
    catch (error)
    {
      const defaultLoginFailureMessage = '注册失败，请重试！';
      // @ts-ignore
      message.error(error.message ?? defaultLoginFailureMessage);
    }
  };
  const { status, type: loginType } = userLoginState;
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          submitter={{
            searchConfig:{
              submitText:"注册"
            }
          }}
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="用户中心"
          subTitle={<a href={PLANET_LINK} target="_blank">FIND ME</a>}
          initialValues={{
            autoLogin: true,
          }}

        //点击按钮后触发这个方法
          onFinish={async (values) => {
            await handleSubmit(values as API.RegisterParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType}>
            <Tabs.TabPane key="account" tab={'账号密码注册'} />
          </Tabs>

          {status === 'error' && loginType === 'account' && (
            <LoginMessage content={'错误的账号和密码'} />
          )}


          {type === 'account' && (
            <>
              <ProFormText
                name="planetCode"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入星球编号'}
                rules={[
                  {
                    required: true,
                    message: '星球编号是必填项！',
                  },
                  // {
                  //   max:5,
                  //   type:'string',
                  //   message:'星球编号不超过5位'
                  // },
                ]}
              />
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入账号'}
                rules={[
                  {
                    required: true,
                    message: '账号是必填项！',
                  },
                  {
                    min:5,
                    type:'string',
                    message:'账号长度不小于5位'
                  },
                ]}
              />
              <ProFormText.Password
                name="password"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入密码'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  {
                    min:8,
                    type:'string',
                    message:'密码长度不小于8位'
                  },
                ]}
              />
              <ProFormText.Password
              name="checkPassword"
              fieldProps={{
                size: 'large',
                prefix: <LockOutlined className={styles.prefixIcon} />,
              }}
              placeholder={'请再次输入密码'}
              rules={[
                {
                  required: true,
                  message: '确认密码是必填项！',
                },
                {
                  min:8,
                  type:'string',
                  message:'密码长度不小于8位'
                },
              ]}
              />

            </>
          )}

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <Divider>
              <Link to="/user/login">返回登录页</Link>
            </Divider>

          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Register;
