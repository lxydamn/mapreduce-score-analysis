import { defineStore } from "pinia";
import router from "../router";

export const useUserStore = defineStore('user', {
    persist:true,
    state: () => {
        return {
            id:"",
            username:"",
            is_login:false
        }
    },
    actions: {
       logout() {
            this.id=""
            this.username=""
            this.is_login=false
            sessionStorage.removeItem("is_login")
            router.push('/login')
       },
    }
})