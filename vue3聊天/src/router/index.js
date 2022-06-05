import { createRouter,createWebHistory} from "vue-router";

const routes = [
    { path: "/", redirect: "/login" },
    {
        path: "/home",
        name: "home",
        component: () => import("../pages/home.vue")
    },
	{
		path: "/login",
		name: "login",
		component: ()=> import("../pages/login.vue")
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

export default router;

