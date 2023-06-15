<template>
  <div class="logo">
    <router-link to="index">
      MapReduce成绩分析
    </router-link>
    
  </div>
  <RouterView></RouterView>
</template>

<style>
.logo {
  height: 4rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 900;
  box-shadow: 10px 5px 5px rgb(213, 211, 211);

  @supports (-webkit-background-clip: text) or (background-clip: text) {
    background-image: linear-gradient(to right, #eea2a2 0%, #bbc1bf 19%, #57c6e1 42%, #b49fda 79%, #7ac5d8 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}
</style>

