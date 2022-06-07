<template>





	<headbar></headbar>
	<list></list>
	<message></message>

	<el-input id="inp" v-model="textarea" :rows='6' type="textarea" placeholder="输入消息。。。。" />

	<el-button id="send" type="primary" style="width: 80px;" @click="send">发送</el-button>
	<el-button id="cancle" style="width: 80px;" @click="clear">清空</el-button>


	<el-upload
    class="upload"
	:headers="headers"
	:data={targetId:store.state.target.id}
    action="http://localhost:8081/sendMsg/img"
    multiple
	:on-success="uploadsuccess"
  >
    <el-button id="upload" type="primary">Click to upload</el-button>

  </el-upload>


	<el-popover placement="top-start" title="Title" :width="200" trigger="hover">
		<template #reference>
			<el-button id="emojibtn">😀</el-button>
		</template>

		<div class="browBox">

			<ul id="eul">

				<li id="eli" @click="addemoji(item)" v-for="(item, index) in emoji">{{ item.char }}</li>

			</ul>

		</div>

	</el-popover>




</template>


<script setup lang="ts">

import { ElMessage, ElMessageBox } from 'element-plus'
// import Input from '../components/input.vue';
import headbar from '../components/headbar.vue';
import list from '../components/list.vue';
import message from '../components/message.vue';
import { onBeforeMount, reactive, ref } from 'vue';
import axios from 'axios';
import store from '../store';
import global from '../global';
import emoji from '../assets/emojis.json';
const textarea = ref('')


const headers = reactive({
	token:store.state.user.token
})
const data = reactive({
	"targetId":store.state.target.id
})



const addemoji = (item:any)=>{
	textarea.value = textarea.value+item.char;
}


const clear = ()=>{
	textarea.value='';
}


const uploadsuccess = (res:any) =>{
	console.log(res);
	if(res.code==200){
		let m = {
				id: store.state.user.id,
				type: 'img',
				msg: res.data
			}
			// console.log(store.state.msglist);
			// textarea.value = '';
			store.state.msglist.push(m);
			let msgli = localStorage.getItem(store.state.target.id);
			if (msgli != null) {
				let mid = JSON.parse(msgli);
				mid.push(m);
				localStorage.removeItem(store.state.target.id);
				localStorage.setItem(store.state.target.id, JSON.stringify(mid))
			} else {
				let mid = [];
				mid.push(m);
				localStorage.setItem(store.state.target.id, JSON.stringify(mid))
			}
		console.log(store.state.msglist);
	}
}

const send = () => {

	let params = new URLSearchParams();
	params.append('userId', store.state.user.id);
	params.append('targetId', store.state.target.id);
	params.append('msg', textarea.value);

	axios({
		method: 'post',
		params: params,
		headers: {
			token: store.state.user.token
		},
		url: global.baseurl + '/sendMsg/text'

	}).then(res => {
		if (res.data.code == 200) {
			console.log("发送成功");

			let m = {
				id: store.state.user.id,
				type: 'text',
				msg: textarea.value
			}
			console.log(store.state.msglist);
			textarea.value = '';
			store.state.msglist.push(m);

			let msgli = localStorage.getItem(store.state.target.id);
			if (msgli != null) {
				let mid = JSON.parse(msgli);
				mid.push(m);
				localStorage.removeItem(store.state.target.id);
				localStorage.setItem(store.state.target.id, JSON.stringify(mid))
			} else {
				let mid = [];
				mid.push(m);
				localStorage.setItem(store.state.target.id, JSON.stringify(mid))
			}
		} else {
			console.log(res);
			window.alert('发送失败');
		}
	}).catch(e => {
		console.log(e);
	})
}






</script>


<style >
html,
body {
	height: 100%;
	width: 100%;
	margin: 0;
	padding: 0;
}

#send {
	position: absolute;
	right: 10px;
	bottom: 1px;
}

#cancle {
	position: absolute;
	right: 110px;
	bottom: 1px;
}

#app {
	height: 100%;
	width: 100%;
}

#inp {
	position: absolute;
	width: 89%;
	left: 170px;
	top: -152px;

}

.browBox {
	width: 100%;
	height: 200px;
	background: #e6e6e6;
	position: absolute;
	z-index: 100;
	bottom: 0px;
	overflow: scroll;

}

#eul {
	display: flex;
	flex-wrap: wrap;
	padding: 10px;
}


#eli {
	cursor: pointer;
	width: 20%;
	font-size: 26px;
	list-style: none;
	text-align: center;
}

#emojibtn {
	position: absolute;
	right: 210px;
	bottom: 1px;
}

#upload {
	position: absolute;
	right: 310px;
	bottom: 1px;
}

</style>