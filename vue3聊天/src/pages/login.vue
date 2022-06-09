<template>
	<div id="loginbox">


		<div class="avatar" > 

			<img id="pic" src="http://localhost:90/imgmsg/imgmsgbb60feae-3031-4e3e-ab3d-bb4c65f8f0fc.jpg">

		</div>

		<el-row class="account">
			<el-input v-model="account" size="large">
				<template #prepend>用户名</template>
			</el-input>
		</el-row>

		<el-row class="password">
			<el-input v-model="password" size="large" type="password" show-password>
				<template #prepend>密码&nbsp&nbsp&nbsp</template>
			</el-input>
		</el-row>


		<el-button class="submit" size="large" type="info" @click="submit(account, password)">登录</el-button>
		<el-button class="clear" size="large" type="danger" @click="dialogVisible = true">注册</el-button>



	</div>


	<el-dialog v-model="dialogVisible" title="注册" width="500px" :before-close="handleClose">


		<el-form :model="form">
			<el-form-item label="账户">
				<el-input v-model="form.account"></el-input>
			</el-form-item>
			<el-form-item label="昵称">
				<el-input v-model="form.nickname"></el-input>
			</el-form-item>
			<el-form-item label="密码">
				<el-input v-model="form.password" type="password" show-password></el-input>
			</el-form-item>

		</el-form>

		<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialogVisible = false">Cancel</el-button>
				<el-button type="primary" @click="dialogVisible = false; register()">Confirm</el-button>
			</span>
		</template>
	</el-dialog>

</template>

<script setup lang="ts">
import { ElMessageBox } from 'element-plus';
import { onMounted, ref } from 'vue';
import axios from 'axios';
import global from '../global';
import { useRouter } from 'vue-router';
import { reactive } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const dialogVisible = ref(false);
const router = useRouter();
const account = ref('');
const password = ref('');
const nickname = ref('');
const form = reactive({
	account: '',
	password: '',
	nickname: ''
})


const submit = (acc: string, pwd: string) => {
	console.log("yes");
	axios({
		url: global.baseurl + '/login',
		method: "post",
		params: {
			"account": acc,
			"password": pwd
		}
	}).then(res => {
		if (res.data.code == 200) {
			store.state.user.account = account.value;
			store.state.user.id = res.data.data.id;
			store.state.user.nickname = res.data.data.nickname;
			store.state.user.token = res.data.data.token;
			store.state.user.avatar = res.data.data.avatar;
			console.log(res);
			console.log(store.state);
			localStorage.setItem('vuetoken', res.data.data.token);
			router.push('/home');
		} else if (res.data.code == 401) {
			window.alert("用户名或密码错误")
		} else {
			console.log(res);
		}
	}).catch(e => {

	})
};

const register = () => {
	const params = new URLSearchParams();
	params.append('account', form.account);
	params.append('password', form.password);
	params.append('nickname', form.nickname);


	axios({
		url: global.baseurl + '/register',
		method: 'post',
		params: params
	}).then(res => {
		console.log(res);
	}).catch(e => {

	})
};


const handleClose = (done: () => void) => {
	ElMessageBox.confirm('Are you sure to close this dialog?')
		.then(() => {
			form.account = '',
				form.nickname = '',
				form.password = ''
			done()
		})
		.catch(() => {
			// catch error
		})
};


onMounted(() => {


	// let token = localStorage.getItem('vuetoken');
	// if(!(token == null)){
	// 	axios({
	// 		method: 'post',
	// 		url: global.baseurl+'/checklogin',
	// 		headers: {
	// 			token: token
	// 		}
	// 	}).then(res=>{
	// 		if(res.data.code==200){
	// 			store.state.user.token=token;
	// 			router.push('/home');
	// 		}else{
	// 			localStorage.removeItem('vuetoken');
	// 		}
	// 	}).catch(e=>{
	// 		console.log(e);
	// 	})
	// }
})


</script>

<style>
body,
html {
	width: 100%;
	height: 100%;
	margin: 0;
	padding: 0;

}

#loginbox {
	position: absolute;
	height: 40%;
	width: 40%;
	transform: translate(-50%, -50%);
	left: 50%;
	top: 40%;
	background-color: #2dbafd;
	border-radius: 20px;
	
}

#pic {
	position: relative;
	width: 150px;
	height: 150px;
	overflow: hidden;
	top: 1px;
	text-align: center;
	
	
}

.dialog-footer button:first-child {
	margin-right: 10px;
}


#app {
	width: 100%;
	height: 100%;
	background: #1DA1F2;
	/* fallback for old browsers */
	background: -webkit-linear-gradient(to right, #009ffc, #1DA1F2);
	/* Chrome 10-25, Safari 5.1-6 */
	background: linear-gradient(to right, #009ffc, #1DA1F2);
	/* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */


}

.account {
	width: 350px;
	position: absolute;
	top: 35%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.password {
	width: 350px;
	position: absolute;
	top: 60%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.nickname {
	width: 350px;
	position: absolute;
	top: 80%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.submit {
	position: absolute;
	left: 20%;
	top: 80%;
	width: 90px;
}

.clear {
	position: absolute;
	right: 20%;
	top: 80%;
	width: 90px;
}

.avatar {
	position: absolute;
	width: 150px;
	height: 150px;
	background-color: #699dc1;
	border-radius: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	text-align: center;
	overflow: hidden;
}
</style>