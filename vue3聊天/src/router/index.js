import { createRouter,createWebHistory} from "vue-router";
import store from "../store";

const routes = [
    { path: "/", redirect: "/login" },
    {
        path: "/home",
        name: "home",
        component: () => import("../pages/home.vue"),
		meta: {
			auth:true
		}
    },
	{
		path: "/login",
		name: "login",
		component: ()=> import("../pages/login.vue"),
		meta: {
			auth:false
		}
	},
	{
		path:"/input",
		name:"input",
		component: ()=> import("../components/input.vue")
	}

]

const router = createRouter({
    history: createWebHistory(),
    routes
});


router.beforeEach((to, from , next)=>{
	const token = store.state.user.token;
	if(to.meta.auth){
		if(token==''){
			return router.replace({
				name:"login"
			})
		}	
		else{
			next();
		}
	}else{
		next();
	}
})

export default router;

