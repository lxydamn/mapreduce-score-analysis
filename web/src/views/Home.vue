<template>
    <a-layout>
      <a-layout-header
        :style="{ background: '#fff', height: '10vh' }"
      >
      <headerBar></headerBar>
      </a-layout-header>
      <a-layout>
        <a-layout-sider
           width="256" 
          :style="{ background: '#fff', height: '90vh'}"
        >
          <sideBar />
        </a-layout-sider>
        <a-layout style="padding: 10px;">
          <a-layout-content
            :style="{ background: '#fff', height: '90vh' }"
          >
            <RouterView v-slot="{ Component }">
                <component :is="Component"></component>
            </RouterView>
          </a-layout-content>
        </a-layout>
      </a-layout>
    </a-layout>
  </template>
  <script lang="ts">
  import sideBar from '../components/sideBar.vue';
  import headerBar from '../components/headerBar.vue';
  import { UserOutlined, LaptopOutlined, NotificationOutlined } from '@ant-design/icons-vue';
  import { defineComponent } from 'vue';
  export default defineComponent({
    components: {
      UserOutlined,
      LaptopOutlined,
      NotificationOutlined,
      sideBar,
      headerBar
    },
    setup() {
      return {
      };
    },
  });
  </script>
  
  <style>
  .ant-layout-header {
    margin: 0;
    padding: 0;
  }
  </style>
  