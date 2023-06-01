<template>
    <h1>卢裕中是傻逼</h1>
    <h1>{{userStore.id}}</h1>
    <h1>{{userStore.username}}</h1>
    <h1>{{userStore.is_login}}</h1>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { useUserStore } from '../store/user';

  const userStore = useUserStore()

  export default defineComponent({
    
    components: {
    },
    setup() {
      const init = () => {
        console.log(userStore.id, userStore.username, userStore.is_login)
      }
      init()
      return {
        userStore,
      };
    },
  });
  </script>