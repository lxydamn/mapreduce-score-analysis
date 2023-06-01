<template>

    <div class="login-box">
        <a-form
        :model="formState"
        name="normal_login"
        class="login-form"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >

        <a-form-item
            label="用户账号"
            name="username"
            :rules="[{ required: true, message: '不能输入空用户账号' }]"
      >
        <a-input size="large"  v-model:value="formState.username">
          <template #prefix>
            <UserOutlined class="site-form-item-icon" />
          </template>
        </a-input>
      </a-form-item>
  
      <a-form-item
        label="用户密码"
        name="password"
        :rules="[{ required: true, message: '不能输入空密码' }]"
      >
        <a-input-password size="large" v-model:value="formState.password">
          <template #prefix>
            <LockOutlined class="site-form-item-icon" />
          </template>
        </a-input-password>
      </a-form-item>

        <div class="login-btn-box">
            <a-form-item >
                <a-button type="primary" html-type="submit" class="login-form-button">
                  登录
                </a-button>
              </a-form-item>
      
              <a-form-item>
                  <a-button  type="primary" class="login-form-button">
                    <RouterLink to="register">
                        注册
                    </RouterLink>
                    
                  </a-button>
            </a-form-item>
        </div>
       
      </a-form>

    </div>
    
  </template>
  <script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { useUserStore } from '../store/user';
  import axios from 'axios'
  import {notification} from 'ant-design-vue';
  import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import router from '../router';
  interface FormState {
    username: string;
    password: string;
  }
  const userStore = useUserStore()
  export default defineComponent({
    components: {
      UserOutlined,
      LockOutlined,
    },
    setup() {

      const formState = reactive<FormState>({
        username: '',
        password: '',
      });

      

      const onFinish = (values: any) => {
        axios ({
          url:"http://localhost:8080/api/user/login",
          method:'POST',
          params: {
            username: values.username,
            password: values.password,
          }
        })
        .then((resp) => {
          const data = resp.data;
          console.log(data)
          if (data.error_info === 'success') {
              userStore.$patch({
                username: values.username,
                id: data.id,
                is_login:true,
              })
              sessionStorage.setItem("is_login", 'true')
              router.push('/index')
          } else {
            notification.error({
              message:'登录遇到错误',
              description:data.error_info
            })
          }
        })
        
      };
  
      const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
      };

      return {
        formState,
        onFinish,
        onFinishFailed,
      };
    },
  });
  </script>
  <style scoped>
    .login-box {
        height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background-image: linear-gradient(to top, #accbee 0%, #e7f0fd 100%);
    }
    .login-form {
        width: 350px;
        display: flex;
        flex-direction: column;
        background-color: #fff;
        padding: 20px;
        border-radius: 10px;
    }
    .login-form-button {
        width: 120px;
        margin: 20px;
    }

    .login-btn-box {
        display: flex;
    }
  </style>
  